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
import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.dao.impl.SequenceRepositoryImpl;
import com.creants.muext.entities.quest.QuestStats;
import com.creants.muext.services.QuestManager;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class GetQuestListRequestHandler extends BaseClientRequestHandler {
	private static final String SERVER_ID = "mus1";

	@Autowired
	private QuestStatsRepository questStateRepository;
	private SequenceRepository sequenceRepository;


	public GetQuestListRequestHandler() {
		questStateRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
		sequenceRepository = Creants2XApplication.getBean(SequenceRepositoryImpl.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer groupId = params.getInt("gid");
		if (groupId == null) {
			groupId = QuestManager.GROUP_MAIN_QUEST;
		}

		List<QuestStats> quests = questStateRepository.getQuests(SERVER_ID + "#" + user.getCreantsUserId(), groupId,
				false);

		IQAntArray questArr = QAntArray.newInstance();
		for (QuestStats questStats : quests) {
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
