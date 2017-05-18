package com.creants.muext.controllers;

import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.managers.MatchManager;
import com.creants.muext.util.UserHelper;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class MissionFinishRequestHandler extends BaseClientRequestHandler {
	private MatchManager matchManager;
	private GameHeroRepository repository;


	public MissionFinishRequestHandler() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		// Integer missionId = params.getInt("msid");
		Boolean isWin = params.getBool("win");
		params = QAntObject.newInstance();
		params.putByte("result", (byte) 1);
		if (!isWin) {
			sendExtResponse("cmd_mission_finish", params, user);
			return;
		}

		String gameHeroId = UserHelper.getGameHeroId(user);
		IQAntObject match = matchManager.getMatch(gameHeroId);

		GameHero gameHero = repository.findOne(gameHeroId);
		IQAntObject reward = match.getQAntObject("reward");
		processReward(reward, gameHero);
		params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));
		sendExtResponse("cmd_mission_finish", params, user);
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
			}
		}

		gameHero.setZen(gameHero.getZen() + reward.getInt("zen"));
		repository.save(gameHero);
	}

}
