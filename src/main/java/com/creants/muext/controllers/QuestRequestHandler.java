package com.creants.muext.controllers;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.QuestConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.managers.HeroItemManager;

/**
 * @author LamHM
 *
 */
public class QuestRequestHandler extends BaseClientRequestHandler {
	private static final QuestConfig questConfig = QuestConfig.getInstance();
	private static final int GET_LIST = 1;
	private static final int CLAIM = 2;
	private QuestStatsRepository questRepository;
	private GameHeroRepository repository;
	private HeroItemManager heroItemManager;


	public QuestRequestHandler() {
		questRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
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

			default:
				break;
		}

	}


	private void getList(QAntUser user, IQAntObject params) {
		String group = params.getUtfString("group");
	}


	private void claimReward(QAntUser user, IQAntObject params) {
		Long questId = Long.parseLong(params.getUtfString("qid"));
		HeroQuest heroQuest = questRepository.findOne(questId);
		params = QAntObject.newInstance();
		if (!heroQuest.isFinish() && heroQuest.isClaim()) {
			Quest quest = questConfig.getQuest(heroQuest.getQuestIndex());
			GameHero gameHero = repository.findOne(user.getName());
			processReward(user, quest, gameHero);
			params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));
			heroQuest.setFinish(true);
			questRepository.save(heroQuest);
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
