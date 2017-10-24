package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.creants.muext.config.ItemConfig;
import com.creants.muext.config.QuestConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.QuestBase;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.MessageFactory;
import com.creants.muext.services.QuestManager;

/**
 * @author LamHM
 *
 */
public class QuestRequestHandler extends BaseClientRequestHandler {
	private static final QuestConfig questConfig = QuestConfig.getInstance();
	private static final int GET_LIST = 1;
	private static final int CLAIM = 2;
	private static final int FINISH = 3;
	private static final int SEEN_GROUP = 4;
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

			case FINISH:
				finishQuest(user, params);
				break;

			case SEEN_GROUP:
				notifyQuestNotSeenAndNotClaimYet(user, params);
				break;

			default:
				break;
		}

	}


	private void notifyQuestNotSeenAndNotClaimYet(QAntUser user, IQAntObject params) {
		String groupId = params.getUtfString("group");
		List<HeroQuest> quests = questManager.getNewQuests(user.getName(), groupId);
		if (quests == null || quests.size() <= 0) {
			return;
		}

		for (HeroQuest heroQuest : quests) {
			heroQuest.setSeen(true);
		}
		questManager.saveQuests(quests);

		Map<String, Integer> countMap = new HashMap<>();
		countMap.put(groupId, 0);

		send(ExtensionEvent.CMD_NOTIFICATION, MessageFactory.buildNotificationCountQuest(countMap), user);
	}


	private void finishQuest(QAntUser user, IQAntObject params) {
		Long questId = params.getLong("qid");
		if (questId == null)
			return;

		HeroQuest quest = questManager.getQuest(user.getName(), questId);
		if (quest == null || !quest.isClaim() || quest.isFinish())
			return;

		Integer no = params.getInt("value");
		if (no == null)
			no = 1;

		boolean isFinish = quest.incr(no);
		if (isFinish) {
			quest.setClaim(true);
		}

		questManager.save(quest);
		IQAntObject response = MessageFactory.buildNotification(ExtensionEvent.NTF_GROUP_QUEST,
				ExtensionEvent.NTF_TYPE_FINISH);
		response.putQAntObject("quest", QAntObject.newFromObject(quest));
		send(ExtensionEvent.CMD_NOTIFICATION, response, user);
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
			if (quests == null || quests.size() <= 0) {
				quests = questConfig.getDailyQuestList();
				questManager.registerQuests(gameHeroId, quests);
			}
		}

		IQAntArray questArr = QAntArray.newInstance();
		for (HeroQuest questStats : quests) {
			questArr.addQAntObject(QAntObject.newFromObject(questStats));
		}

		params.putQAntArray("quests", questArr);
		send(ExtensionEvent.CMD_QUEST, params, user);

		notifyQuestNotSeenAndNotClaimYet(user, params);
	}


	private void claimReward(QAntUser user, IQAntObject params) {
		Long questId = params.getLong("qid");
		HeroQuest heroQuest = questManager.getQuest(user.getName(), questId);
		if (!heroQuest.isFinish() && heroQuest.isClaim()) {
			QuestBase quest = questConfig.getQuest(heroQuest.getQuestIndex());
			GameHero gameHero = repository.findOne(user.getName());
			processReward(user, quest, gameHero, params);
			if (heroQuest.getGroupId().equals(QuestManager.GROUP_DAILY_QUEST)) {
				heroQuest.setClaim(false);
				heroQuest.setFinish(true);
				questManager.save(heroQuest);
			} else {
				questManager.delete(heroQuest);
			}
			send(ExtensionEvent.CMD_QUEST, params, user);

			params.putUtfString("group", heroQuest.getGroupId());
			notifyQuestNotSeenAndNotClaimYet(user, params);
		} else {
			QAntTracer.warn(this.getClass(), "Bad request! gameHeroId:" + user.getName());
		}
	}


	private void processReward(QAntUser user, QuestBase quest, GameHero gameHero, IQAntObject params) {
		IQAntObject assets = QAntObject.newInstance();
		int exp = quest.getExpReward();
		if (exp > 0) {
			assets.putInt("exp", gameHero.getExp());
			if (gameHero.incrExp(exp)) {
				assets.putInt("maxExp", gameHero.getMaxExp());
				assets.putInt("level", gameHero.getLevel());
			}
		}

		if (StringUtils.isBlank(quest.getItemRewardString())) {
			QAntTracer.debug(this.getClass(),
					"Quest reward: {user:" + user.getName() + ", rewardItem:" + quest.getItemRewardString() + "}");
			List<HeroItem> addItems = heroItemManager.addItems(user.getName(), quest.getItemRewardString());
			QAntTracer.debug(this.getClass(), "size: " + addItems.size());
			QAntObject updateObj = QAntObject.newInstance();
			QAntArray itemArr = QAntArray.newInstance();
			for (HeroItem heroItem : addItems) {
				itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
			}
			updateObj.putQAntArray("items", itemArr);
			params.putQAntObject("update", updateObj);

			IQAntObject reward = QAntObject.newInstance();
			reward.putQAntArray("items", ItemConfig.getInstance().buildShortItemInfo(quest.getFullReward()));
			params.putQAntObject("reward", reward);
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
