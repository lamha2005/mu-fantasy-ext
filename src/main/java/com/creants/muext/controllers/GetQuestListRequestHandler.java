package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.services.QuestManager;

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

		String gameHeroId = user.getName();
		List<HeroQuest> quests = new ArrayList<>();
		if (groupId == QuestManager.GROUP_MAIN_QUEST) {
			quests = questStateRepository.getQuests(gameHeroId, groupId, false);
		} else if (groupId == QuestManager.GROUP_DAILY_QUEST) {
			quests = questStateRepository.findDailyQuests(gameHeroId, getStartOfDateMilis(), getEndOfDateMilis());
		}

		IQAntArray questArr = QAntArray.newInstance();
		for (HeroQuest questStats : quests) {
			questArr.addQAntObject(QAntObject.newFromObject(questStats));
		}

		params = new QAntObject();
		params.putQAntArray("quests", questArr);
		params.putInt("gid", groupId);

		send(ExtensionEvent.CMD_GET_QUESTS, params, user);
	}


	private long getStartOfDateMilis() {
		return DateUtils.truncate(new Date(), Calendar.DATE).getTime();
	}


	private long getEndOfDateMilis() {
		return DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1).getTime();
	}
}
