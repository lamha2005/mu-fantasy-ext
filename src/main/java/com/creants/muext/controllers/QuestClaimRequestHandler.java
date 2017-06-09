package com.creants.muext.controllers;

import java.util.Calendar;
import java.util.Date;

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
import com.creants.muext.entities.Reward;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.Quest;

/**
 * @author LamHM
 *
 */
public class QuestClaimRequestHandler extends BaseClientRequestHandler {
	private QuestStatsRepository questRepository;
	private GameHeroRepository repository;


	public QuestClaimRequestHandler() {
		questRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Long questId = Long.parseLong(params.getUtfString("qid"));
		HeroQuest heroQuest = questRepository.findOne(questId);
		params = QAntObject.newInstance();
		if (!heroQuest.isFinish() && heroQuest.isClaim()) {
			Quest quest = QuestConfig.getInstance().getQuest(heroQuest.getQuestIndex());
			GameHero gameHero = repository.findOne(user.getName());
			processReward(user, quest.getReward(), gameHero);
			params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));
			heroQuest.setFinish(true);
			questRepository.save(heroQuest);
			send("cmd_quest_claim", params, user);
		} else {
			QAntTracer.warn(this.getClass(), "Bad request! gameHeroId:" + user.getName());
		}

	}


	private void processReward(QAntUser user, Reward reward, GameHero gameHero) {
		IQAntObject assets = QAntObject.newInstance();
		int exp = reward.getExp();
		if (exp > 0) {
			gameHero.setExp(gameHero.getExp() + exp);
			boolean isLevelUp = false;
			while (gameHero.getExp() > gameHero.getMaxExp()) {
				gameHero.setExp(gameHero.getExp() - gameHero.getMaxExp());
				gameHero.setLevel(gameHero.getLevel() + 1);
				gameHero.setMaxExp(gameHero.getLevel() * 10000);
				isLevelUp = true;
			}

			assets.putInt("exp", gameHero.getExp());
			if (isLevelUp) {
				assets.putInt("maxExp", gameHero.getMaxExp());
				assets.putInt("level", gameHero.getLevel());
			}
		}

		gameHero.setZen(gameHero.getZen() + reward.getZen());
		assets.putLong("zen", gameHero.getZen());
		send("cmd_assets_change", assets, user);
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
