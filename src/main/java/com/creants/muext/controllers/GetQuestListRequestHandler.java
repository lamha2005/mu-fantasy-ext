package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.List;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.services.QuestManager;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class GetQuestListRequestHandler extends BaseClientRequestHandler {

	private QuestManager questManager;


	public GetQuestListRequestHandler() {
		questManager = Creants2XApplication.getBean(QuestManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {

		String groupId = params.getUtfString("group");
		if (groupId == null) {
			groupId = QuestManager.GROUP_WORLD_QUEST;
		}

		String gameHeroId = user.getName();
		List<HeroQuest> quests = new ArrayList<>();
		if (groupId.equals(QuestManager.GROUP_WORLD_QUEST)) {
			quests = questManager.getQuests(gameHeroId, groupId, false);
		} else if (groupId.equals(QuestManager.GROUP_DAILY_QUEST)) {
			quests = questManager.getDailyQuests(gameHeroId);
		}

		IQAntArray questArr = QAntArray.newInstance();
		for (HeroQuest questStats : quests) {
			questArr.addQAntObject(QAntObject.newFromObject(questStats));
		}

		params = new QAntObject();
		params.putQAntArray("quests", questArr);

		send(ExtensionEvent.CMD_GET_QUESTS, params, user);
	}

}
