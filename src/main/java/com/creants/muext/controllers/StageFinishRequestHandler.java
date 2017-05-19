package com.creants.muext.controllers;

import java.util.List;
import java.util.Set;

import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;
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


	public StageFinishRequestHandler() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
		questStageRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String gameHeroId = UserHelper.getGameHeroId(user);
		Boolean isWin = params.getBool("win");
		params = QAntObject.newInstance();
		params.putByte("result", (byte) ((isWin != null && isWin) ? 1 : 0));
		if (!isWin) {
			matchManager.removeMatch(gameHeroId);
			sendExtResponse("cmd_mission_finish", params, user);
			return;
		}

		IQAntObject match = matchManager.getMatch(gameHeroId);
		matchManager.removeMatch(gameHeroId);

		GameHero gameHero = repository.findOne(gameHeroId);
		IQAntObject reward = match.getQAntObject("reward");

		processReward(reward, gameHero);
		params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));

		processQuest(match.getInt(StageRequestHandler.STAGE_INDEX), gameHeroId);

		sendExtResponse("cmd_stage_finish", params, user);
	}


	private void processQuest(int stageIndex, String gameHeroId) {
		Stage stage = StageConfig.getInstance().getStage(stageIndex);
		Set<Integer> questIds = questManager.getQuestsContainMonster(stage.getMonsters());
		if (questIds == null || questIds.isEmpty()) {
			return;
		}

		List<HeroQuest> quests = questStageRepository.getQuests(gameHeroId,
				questIds.toArray(new Integer[questIds.size()]));
		if (quests == null || quests.isEmpty())
			return;

		for (HeroQuest heroQuest : quests) {
			int taskType = heroQuest.getTaskType();
		}
	}


	public void sendExtResponse(String cmdName, IQAntObject params, QAntUser recipient) {
		IQAntObject resObj = QAntObject.newInstance();
		resObj.putUtfString("c", cmdName);
		resObj.putQAntObject("p", (params != null) ? params : new QAntObject());

		IResponse response = new Response();
		response.setId(SystemRequest.CallExtension.getId());
		response.setTargetController((byte) 1);
		response.setContent(resObj);
		response.setRecipients(recipient.getChannel());
		response.write();
	}


	private void processReward(IQAntObject reward, GameHero gameHero) {
		Integer exp = reward.getInt("exp");
		if (exp > 0) {
			gameHero.setExp(gameHero.getExp() + exp);

			while (gameHero.getExp() > gameHero.getMaxExp()) {
				gameHero.setExp(gameHero.getExp() - gameHero.getMaxExp());
				gameHero.setLevel(gameHero.getLevel() + 1);
				gameHero.setMaxExp(gameHero.getLevel() * 10000);
			}
		}

		gameHero.setZen(gameHero.getZen() + reward.getInt("zen"));
		repository.save(gameHero);
	}

}
