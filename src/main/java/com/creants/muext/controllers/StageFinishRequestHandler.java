package com.creants.muext.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.HeroStageRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.Task;
import com.creants.muext.entities.quest.TaskType;
import com.creants.muext.entities.world.HeroStage;
import com.creants.muext.entities.world.Stage;
import com.creants.muext.managers.MatchManager;
import com.creants.muext.services.QuestManager;
import com.creants.muext.util.UserHelper;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class StageFinishRequestHandler extends BaseClientRequestHandler {
	private MatchManager matchManager;
	private GameHeroRepository repository;
	private QuestManager questManager;
	private QuestStatsRepository questStageRepository;
	private HeroStageRepository heroStageRepository;
	private SequenceRepository sequenceRepository;


	public StageFinishRequestHandler() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
		questStageRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
		heroStageRepository = Creants2XApplication.getBean(HeroStageRepository.class);
		sequenceRepository = Creants2XApplication.getBean(SequenceRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String gameHeroId = UserHelper.getGameHeroId(user);
		Boolean isWin = params.getBool("win");
		params = QAntObject.newInstance();
		params.putByte("result", (byte) ((isWin != null && isWin) ? 1 : 0));
		if (!isWin) {
			matchManager.removeMatch(gameHeroId);
			send("cmd_mission_finish", params, user);
			return;
		}

		IQAntObject match = matchManager.getMatch(gameHeroId);
		if (match == null) {
			QAntTracer.warn(this.getClass(), "Request finish match not found!");
			return;
		}
		matchManager.removeMatch(gameHeroId);

		GameHero gameHero = repository.findOne(gameHeroId);
		IQAntObject reward = match.getQAntObject("reward");

		processReward(user, reward, gameHero);
		params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));

		Integer stageIndex = match.getInt(StageRequestHandler.STAGE_INDEX);
		List<HeroQuest> quests = processQuest(stageIndex, gameHeroId);
		if (quests != null) {
			IQAntArray questArr = QAntArray.newInstance();
			for (HeroQuest questStats : quests) {
				questArr.addQAntObject(QAntObject.newFromObject(questStats));
			}
			params.putQAntArray("quests", questArr);
		}

		HeroStage stage = heroStageRepository.findStageByHeroIdAndIndex(gameHeroId, stageIndex);
		if (!stage.isClear()) {
			stage.setClear(true);
			stage.setStarNo(3);
			stage.setSweepTimes(1);

			// mở 1 stage mới
			HeroStage heroStage = new HeroStage(StageConfig.getInstance().getStage(stageIndex + 1));
			heroStage.setHeroId(gameHero.getId());
			heroStage.setId(sequenceRepository.getNextSequenceId("hero_stage_id"));
			heroStage.setUnlock(true);
			heroStageRepository.save(heroStage);
			params.putQAntObject("new_stage", QAntObject.newFromObject(heroStage));
		} else {
			// TODO nếu sao lớn hơn thì cập nhật lại sao
			stage.setSweepTimes(stage.getSweepTimes() + 1);
		}

		stage.setLastestSweepTime(System.currentTimeMillis());
		heroStageRepository.save(stage);
		send("cmd_stage_finish", params, user);
	}


	/**
	 * Kiểm tra trong trận đấu có những con quái nào, lấy danh sách nhiệm vụ có
	 * những con quái đó, lấy danh sách nhiệm vụ của hero nằm trong các nhiệm vụ
	 * vừa lấy trước đó
	 * 
	 * @param stageIndex
	 *            màn đang chơi
	 * @param gameHeroId
	 */
	private List<HeroQuest> processQuest(int stageIndex, String gameHeroId) {
		Stage stage = StageConfig.getInstance().getStage(stageIndex);
		Set<Integer> monsters = stage.getMonsters();
		Set<Integer> questIds = questManager.getQuestsContainMonster(monsters);
		if (questIds == null || questIds.isEmpty()) {
			return null;
		}

		Integer[] array = questIds.toArray(new Integer[questIds.size()]);
		List<HeroQuest> quests = questStageRepository.getQuests(gameHeroId, array);
		if (quests == null || quests.isEmpty())
			return null;

		try {
			for (HeroQuest heroQuest : quests) {
				int taskType = heroQuest.getTaskType();
				// nhiệm vụ tiêu diệt quái
				if (taskType == TaskType.MonsterKill.getId()) {
					Task task = heroQuest.getTask();
					Map<Object, Object> properties = task.getProperties();
					monsters = stage.getMonsters();
					for (Object monsterIndex : properties.keySet()) {
						int monIndex = Integer.parseInt(String.valueOf(monsterIndex));
						int remainMonster = (Integer) properties.get(monsterIndex) - stage.countMonster(monIndex);
						if (remainMonster > 0) {
							properties.put(monsterIndex, remainMonster);
						} else {
							properties.put(monsterIndex, 0);
							heroQuest.setClaim(true);
						}
					}
				}
			}

			questStageRepository.save(quests);

		} catch (Exception e) {
			QAntTracer.warn(this.getClass(),
					"processQuest fail! gameHeroId:" + gameHeroId + ", stageIndex:" + stageIndex);
			e.printStackTrace();
		}
		return quests;
	}


	private void processReward(QAntUser user, IQAntObject reward, GameHero gameHero) {
		IQAntObject assets = QAntObject.newInstance();
		Integer exp = reward.getInt("exp");
		if (exp > 0) {
			gameHero.setExp(gameHero.getExp() + exp);
			boolean isLevelUp = false;
			while (gameHero.getExp() >= gameHero.getMaxExp()) {
				gameHero.setExp(gameHero.getExp() - gameHero.getMaxExp());
				gameHero.setLevel(gameHero.getLevel() + 1);
				gameHero.setMaxExp(gameHero.getLevel() * 10000);
				isLevelUp = true;
			}

			assets.putInt("exp", gameHero.getExp());
			if (isLevelUp) {
				assets.putInt("maxExp", gameHero.getMaxExp());
				assets.putInt("level", gameHero.getLevel());
			}
		}

		gameHero.setZen(gameHero.getZen() + reward.getInt("zen"));
		assets.putLong("zen", gameHero.getZen());
		send("cmd_assets_change", assets, user);

		repository.save(gameHero);
	}

}
