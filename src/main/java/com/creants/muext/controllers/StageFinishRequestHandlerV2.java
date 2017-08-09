package com.creants.muext.controllers;

import java.util.ArrayList;
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
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.HeroStageRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.Task;
import com.creants.muext.entities.quest.TaskType;
import com.creants.muext.entities.world.HeroStage;
import com.creants.muext.entities.world.Stage;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.managers.MatchManager;
import com.creants.muext.services.AutoIncrementService;
import com.creants.muext.services.QuestManager;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class StageFinishRequestHandlerV2 extends BaseClientRequestHandler {
	private static final int UNLOCK_NEW_STAGE = 1;
	private GameHeroRepository repository;
	private MatchManager matchManager;
	private QuestManager questManager;
	private HeroStageRepository heroStageRepository;
	private HeroClassManager heroManager;
	private AutoIncrementService autoIncrService;
	private BattleTeamRepository battleTeamRepository;
	private HeroItemManager heroItemManager;


	public StageFinishRequestHandlerV2() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
		heroStageRepository = Creants2XApplication.getBean(HeroStageRepository.class);
		autoIncrService = Creants2XApplication.getBean(AutoIncrementService.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		battleTeamRepository = Creants2XApplication.getBean(BattleTeamRepository.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();
		Boolean isWin = params.getBool("win");
		if (!isWin) {
			matchManager.removeMatch(gameHeroId);
			send("cmd_mission_finish", QAntObject.newInstance(), user);
			return;
		}

		IQAntObject match = matchManager.getMatch(gameHeroId);
		if (match == null) {
			QAntTracer.warn(this.getClass(), "Request finish match not found!");
			return;
		}

		matchManager.removeMatch(gameHeroId);
		Integer stageIndex = match.getInt(StageRequestHandler.STAGE_INDEX);
		Stage stage = StageConfig.getInstance().getStage(stageIndex);
		HeroStage heroStage = heroStageRepository.findStageByHeroIdAndIndex(gameHeroId, stageIndex);
		boolean isFirstTime = !heroStage.isClear();

		QAntObject response = processReward(user, params, stage, isFirstTime);
		send("cmd_stage_finish", response, user);

		notifyAssetChange(user, stage);
		processHeroStage(user, heroStage);
		// kiểm tra xử lý nhiệm vụ, nên tách ra notification quest
		processQuest(stageIndex, gameHeroId);
	}


	private void processHeroStage(QAntUser user, HeroStage heroStage) {
		String gameHeroId = user.getName();
		if (!heroStage.isClear()) {
			heroStage.setClear(true);
			heroStage.setStarNo(3);
			heroStage.setSweepTimes(1);
			heroStage.getHeroId();

			// mở 1 stage mới
			Stage nextStage = StageConfig.getInstance().getStage(heroStage.getIndex() + 1);
			HeroStage newStage = new HeroStage(nextStage);

			newStage.setHeroId(gameHeroId);
			newStage.setId(autoIncrService.genHeroStageId());
			newStage.setUnlock(true);
			heroStageRepository.save(newStage);

			if (nextStage.getChapterIndex() > newStage.getChapterIndex()) {
				// TODO thông báo mở chương mới
				QAntTracer.debug(this.getClass(), "----------------------------->> Mo chuong moi");
			}

			QAntObject response = QAntObject.newInstance();
			response.putInt("type", UNLOCK_NEW_STAGE);
			response.putQAntObject("data", QAntObject.newFromObject(newStage));
			send("cmd_notification", response, user);

		} else {
			// TODO nếu sao lớn hơn thì cập nhật lại sao
			heroStage.setSweepTimes(heroStage.getSweepTimes() + 1);
		}

		heroStage.setLastestSweepTime(System.currentTimeMillis());
		heroStageRepository.save(heroStage);
	}


	private QAntObject processReward(QAntUser user, IQAntObject params, Stage stage, boolean isFirstTime) {
		System.out.println("[ERROR] is first time: " + isFirstTime);
		long startTime = System.currentTimeMillis();
		QAntObject response = QAntObject.newInstance();
		QAntObject reward = QAntObject.newInstance();
		int starNo = 3;
		List<HeroItem> bonusItems = stage.getBonus();
		int expReward = stage.getExpReward();

		BattleTeam battleTeam = battleTeamRepository.findOne(user.getName());
		List<Long> heroIds = battleTeam.getMainTeamIds();

		reward.putInt("star_no", starNo);
		reward.putInt("zen", stage.getZenReward());
		QAntArray incrExp = QAntArray.newInstance();
		for (Long heroId : heroIds) {
			if (heroId <= 0)
				continue;

			QAntObject obj = QAntObject.newInstance();
			obj.putLong("heroId", heroId);
			obj.putInt("exp", expReward);
			incrExp.addQAntObject(obj);
		}
		reward.putQAntArray("incr_exp", incrExp);

		List<HeroItem> updateItems = new ArrayList<>();
		if (isFirstTime) {
			List<HeroItem> rewardItems = stage.getRewards();
			if (rewardItems.size() > 0) {
				reward.putQAntArray("items", buildShortItemInfo(rewardItems));
				updateItems.addAll(rewardItems);
			}
		}

		if (bonusItems.size() > 0) {
			reward.putQAntArray("items_bonus", buildShortItemInfo(bonusItems));
			updateItems.addAll(bonusItems);
		}

		response.putQAntObject("reward", reward);

		// cập nhật db
		List<HeroItem> addItems = heroItemManager.addItems(user.getName(), updateItems);

		List<HeroClass> heroes = heroManager.findHeroes(heroIds);
		for (HeroClass heroClass : heroes) {
			heroClass.incrExp(expReward);
		}
		heroManager.save(heroes);

		QAntObject updateObj = QAntObject.newInstance();
		QAntArray heroArr = QAntArray.newInstance();
		for (HeroClass heroClass : heroes) {
			heroArr.addQAntObject(QAntObject.newFromObject(heroClass));
		}

		QAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}
		updateObj.putQAntArray("heroes", heroArr);
		updateObj.putQAntArray("items", itemArr);
		response.putQAntObject("update", updateObj);

		System.out.println("-----------------------> delta: " + (System.currentTimeMillis() - startTime));

		return response;
	}


	private QAntArray buildShortItemInfo(List<HeroItem> bonusItems) {
		QAntArray items = QAntArray.newInstance();
		QAntObject obj = null;
		for (HeroItem heroItem : bonusItems) {
			obj = QAntObject.newInstance();
			obj.putInt("itemGroup", heroItem.getItemGroup());
			obj.putInt("index", heroItem.getIndex());
			obj.putInt("no", heroItem.getNo());
			items.addQAntObject(obj);
		}
		return items;
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
		List<HeroQuest> quests = questManager.getQuests(gameHeroId, array);
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

			questManager.save(quests);

		} catch (Exception e) {
			QAntTracer.warn(this.getClass(),
					"processQuest fail! gameHeroId:" + gameHeroId + ", stageIndex:" + stageIndex);
			e.printStackTrace();
		}

		if (quests != null) {
			IQAntArray questArr = QAntArray.newInstance();
			for (HeroQuest questStats : quests) {
				questArr.addQAntObject(QAntObject.newFromObject(questStats));
			}
			// params.putQAntArray("quests", questArr);
		}

		return quests;
	}


	private void notifyAssetChange(QAntUser user, Stage stage) {
		GameHero gameHero = repository.findOne(user.getName());
		IQAntObject assets = QAntObject.newInstance();
		Integer exp = stage.getExpAccReward();
		if (exp > 0) {
			boolean isLevelUp = gameHero.incrExp(exp);
			assets.putInt("exp", gameHero.getExp());
			if (isLevelUp) {
				assets.putInt("maxExp", gameHero.getMaxExp());
				assets.putInt("level", gameHero.getLevel());
			}
		}

		gameHero.incrZen(stage.getZenReward());
		assets.putLong("zen", gameHero.getZen());

		send("cmd_assets_change", assets, user);
		repository.save(gameHero);
	}

}
