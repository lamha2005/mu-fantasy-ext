package com.creants.muext.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.services.QuestManager;
import com.creants.muext.util.UserHelper;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class GetQuestListRequestHandler extends BaseClientRequestHandler {

	@Autowired
	private QuestStatsRepository questStateRepository;


	public GetQuestListRequestHandler() {
		questStateRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer groupId = params.getInt("gid");
		if (groupId == null) {
			groupId = QuestManager.GROUP_MAIN_QUEST;
		}

		String gameHeroId = UserHelper.getGameHeroId(user);
		List<HeroQuest> quests = questStateRepository.getQuests(gameHeroId, groupId, false);

		IQAntArray questArr = QAntArray.newInstance();
		for (HeroQuest questStats : quests) {
			questArr.addQAntObject(QAntObject.newFromObject(questStats));
		}

		params = new QAntObject();
		params.putQAntArray("quests", questArr);
		params.putInt("gid", groupId);
		sendExtResponse("cmd_get_quests", params, user);
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
