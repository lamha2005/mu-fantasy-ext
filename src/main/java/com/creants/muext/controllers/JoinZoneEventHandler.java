package com.creants.muext.controllers;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.QAntEventParam;
import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author LamHM
 *
 */
public class JoinZoneEventHandler extends BaseServerEventHandler {
	private GameHeroRepository repository;


	public JoinZoneEventHandler() {
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
	}


	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		QAntUser user = (QAntUser) event.getParameter(QAntEventParam.USER);
		String serverId = "mus1";
		long creantsUserId = user.getCreantsUserId();
		String id = serverId + "#" + creantsUserId;
		GameHero gameHero = repository.findOne(id);
		if (gameHero == null) {
			gameHero = new GameHero(serverId, creantsUserId);
			gameHero.setName("Mu Hero " + creantsUserId);
			gameHero.setExp(0);
			gameHero.setLevel(1);
			gameHero.setSoul(0);
			gameHero.setStatmina(60);
			gameHero.setZen(0);
			gameHero.setVipLevel(1);
			gameHero.setVipPoint(0);
			gameHero = repository.save(gameHero);
		}

		try {
			IQAntObject params = new QAntObject();
			ObjectMapper mapper = new ObjectMapper();
			params.putUtfString("game_hero", mapper.writeValueAsString(gameHero));
			sendExtResponse("join_game", params, user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return;
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

}
