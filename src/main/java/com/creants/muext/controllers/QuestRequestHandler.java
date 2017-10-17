package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.QuestConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.QuestManager;

/**
 * @author LamHM
 *
 */
public class QuestRequestHandler extends BaseClientRequestHandler {
	private static final QuestConfig questConfig = QuestConfig.getInstance();
	private static final int GET_LIST = 1;
	private static final int CLAIM = 2;
	private static final int SEEN = 3;
	private GameHeroRepository repository;
	private HeroItemManager heroItemManager;
	private QuestManager questManager;


	public QuestRequestHandler() {
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		if (action == null) {
			action = GET_LIST;
		}

		switch (action) {
			case GET_LIST:
				getList(user, params);
				break;

			case CLAIM:
				claimReward(user, params);
				break;

			case SEEN:
				seenQuest(user, params);

			default:
				break;
		}

	}


	private void seenQuest(QAntUser user, IQAntObject params) {
		Collection<Long> ids = params.getLongArray("ids");
		List<HeroQuest> quests = questManager.getQuests(ids);
		for (HeroQuest heroQuest : quests) {
			heroQuest.setSeen(true);
		}
		questManager.saveQuests(quests);
		send(ExtensionEvent.CMD_QUEST, params, user);
	}


	private void getList(QAntUser user, IQAntObject params) {
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

		params.putQAntArray("quests", questArr);
		send(ExtensionEvent.CMD_QUEST, params, user);
	}


	private void claimReward(QAntUser user, IQAntObject params) {
		Long questId = Long.parseLong(params.getUtfString("qid"));
		HeroQuest heroQuest = questManager.getQuest(user.getName(), questId);
		if (!heroQuest.isFinish() && heroQuest.isClaim()) {
			Quest quest = questConfig.getQuest(heroQuest.getQuestIndex());
			GameHero gameHero = repository.findOne(user.getName());
			processReward(user, quest, gameHero);
			params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));
			heroQuest.setFinish(true);
			questManager.save(heroQuest);
			send(ExtensionEvent.CMD_QUEST, params, user);
		} else {
			QAntTracer.warn(this.getClass(), "Bad request! gameHeroId:" + user.getName());
		}
	}


	private void processReward(QAntUser user, Quest quest, GameHero gameHero) {
		IQAntObject assets = QAntObject.newInstance();
		int exp = quest.getZenReward();
		if (exp > 0) {
			boolean isLevelUp = gameHero.incrExp(exp);
			assets.putInt("exp", gameHero.getExp());
			if (isLevelUp) {
				assets.putInt("maxExp", gameHero.getMaxExp());
				assets.putInt("level", gameHero.getLevel());
			}
		}

		if (StringUtils.isBlank(quest.getItemRewardString())) {
			heroItemManager.addItems(user.getName(), quest.getItemRewardString());
		}

		gameHero.incrZen(quest.getZenReward());
		assets.putLong("zen", gameHero.getZen());
		send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, assets, user);
		repository.save(gameHero);

	}


	public static void main(String[] args) {
		Date truncate = DateUtils.truncate(new Date(), Calendar.DATE);
		String startOfDate = DateFormatUtils.format(truncate, "dd/MM/yyyy HH:mm:ss");
		System.out.println(startOfDate + "/" + truncate.getTime());

		Date addMilliseconds = DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1);

		String endOfDate = DateFormatUtils.format(addMilliseconds, "dd/MM/yyyy HH:mm:ss");
		System.out.println(endOfDate + "/" + addMilliseconds.getTime());
	}

}
