package com.creants.muext.controllers;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.QAntEventParam;
import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.admin.managers.AdminManager;
import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.config.MailConfig;
import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.GiftEventRepository;
import com.creants.muext.dao.HeroStageRepository;
import com.creants.muext.dao.MailRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Mail;
import com.creants.muext.entities.Team;
import com.creants.muext.entities.event.GiftEventBase;
import com.creants.muext.entities.event.GiftInfo;
import com.creants.muext.entities.event.HeroGift;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.world.HeroStage;
import com.creants.muext.entities.world.Stage;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.AutoIncrementService;
import com.creants.muext.services.QuestManager;
import com.creants.muext.util.UserHelper;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class JoinZoneEventHandler extends BaseServerEventHandler {
	private static final String HERO_NAME_PREFIX = "Mu Hero ";
	public static final int STAMINA_REHI_TIME_MILI = 60000;
	public static final int STAMINA_REHI_VALUE = 1;
	public static final GiftEventConfig giftEventConfig = GiftEventConfig.getInstance();

	private GameHeroRepository gameHeroRep;
	private QuestManager questManager;
	private HeroClassManager heroManager;
	private HeroStageRepository stageRepository;
	private AdminManager adminManager;
	private BattleTeamRepository battleTeamRepository;
	private AutoIncrementService autoIncrementService;
	private HeroItemManager heroItemManager;
	private GiftEventRepository giftEventRepository;
	private MailRepository mailRepository;


	public JoinZoneEventHandler() {
		gameHeroRep = Creants2XApplication.getBean(GameHeroRepository.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		autoIncrementService = Creants2XApplication.getBean(AutoIncrementService.class);
		stageRepository = Creants2XApplication.getBean(HeroStageRepository.class);
		adminManager = Creants2XApplication.getBean(AdminManager.class);
		battleTeamRepository = Creants2XApplication.getBean(BattleTeamRepository.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
		giftEventRepository = Creants2XApplication.getBean(GiftEventRepository.class);
		mailRepository = Creants2XApplication.getBean(MailRepository.class);
	}


	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		QAntUser user = (QAntUser) event.getParameter(QAntEventParam.USER);
		user.setLoginTime(System.currentTimeMillis());
		long creantsUserId = user.getCreantsUserId();
		String gameHeroId = user.getName();

		IQAntObject response = new QAntObject();
		boolean isNewAccount = false;
		GameHero gameHero = gameHeroRep.findOne(gameHeroId);
		if (gameHero == null) {
			gameHero = createNewGameHero(gameHeroId, creantsUserId);
			user.setProperty(UserHelper.GAME_HERO_NAME, gameHero.getName());
			isNewAccount = true;
		} else {
			List<HeroClass> heroes = heroManager.findHeroesByGameHeroId(gameHeroId);
			gameHero.setHeroes(heroes);

			List<HeroItem> items = heroItemManager.getItems(gameHeroId);
			gameHero.setItems(items);
		}

		processMail(user, response, isNewAccount);
		updateStamina(user, gameHero);

		// main quest
		List<HeroQuest> mainQuests = questManager.getQuests(gameHeroId, QuestManager.GROUP_MAIN_QUEST, false);
		response.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));
		IQAntArray questArr = QAntArray.newInstance();
		IQAntObject mainQuest = QAntObject.newInstance();
		mainQuest.putInt("gid", QuestManager.GROUP_MAIN_QUEST);
		mainQuest.putUtfString("n", "Nhiệm vụ chính");
		mainQuest.putInt("no", mainQuests == null ? 0 : mainQuests.size());
		questArr.addQAntObject(mainQuest);

		// daily quest
		List<HeroQuest> dailyQuestList = questManager.findDailyQuests(gameHeroId, getStartOfDateMilis(),
				getEndOfDateMilis());
		if (dailyQuestList == null || dailyQuestList.size() <= 0) {
			dailyQuestList = questManager.resetDailyQuest(gameHero);
		}

		IQAntObject dailyQuest = QAntObject.newInstance();
		dailyQuest.putInt("gid", QuestManager.GROUP_DAILY_QUEST);
		dailyQuest.putUtfString("n", "Nhiệm vụ hàng ngày");
		dailyQuest.putInt("no", dailyQuestList.size());
		questArr.addQAntObject(dailyQuest);
		response.putQAntArray("quests", questArr);

		// current chapter
		HeroStage curStage = stageRepository.findStageByHeroIdAndClearIsFalse(gameHeroId);
		IQAntObject curChapter = QAntObject.newInstance();
		curChapter.putInt("chapter_index", curStage.getChapterIndex());
		curChapter.putInt("stage_index", curStage.getIndex());
		response.putQAntObject("cur_chapter", curChapter);

		// team
		BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
		List<Team> teamList = battleTeam.getTeamList();
		IQAntArray teamArr = QAntArray.newInstance();
		for (Team team : teamList) {
			QAntObject teamObj = QAntObject.newInstance();
			teamObj.putUtfString("name", team.getName());
			teamObj.putInt("index", team.getIndex());
			teamObj.putLongArray("heroes", new ArrayList<Long>(Arrays.asList(team.getHeroes())));
			teamArr.addQAntObject(teamObj);
		}
		response.putQAntArray("teams", teamArr);

		// event
		response.putInt("event_no", 10);
		response.putLong("login_time", user.getLoginTime());

		// process gift
		processGiftEvent(user, response);
		send("join_game", response, user);

		adminManager.incrAndNotifyCCU();
		return;
	}


	private void processMail(QAntUser user, IQAntObject response, boolean isNewAccount) {
		if (isNewAccount) {
			List<Mail> mailList = new ArrayList<>(2);
			MailConfig instance = MailConfig.getInstance();
			String gameHeroId = user.getName();
			Mail welcomeMail = new Mail(gameHeroId, instance.getWelcomeMail());
			welcomeMail.setGameHeroId(gameHeroId);
			welcomeMail.setId(autoIncrementService.genMailId());
			mailList.add(welcomeMail);

			Mail welcomeGift = new Mail(gameHeroId, instance.getWelcomeGiftMail());
			welcomeGift.setGameHeroId(gameHeroId);
			welcomeGift.setId(autoIncrementService.genMailId());
			mailList.add(welcomeGift);
			mailRepository.save(mailList);
			return;
		}

		List<Mail> mails = mailRepository.findByGameHeroIdAndReadIsFalse(user.getName());
		QAntObject mailObj = QAntObject.newInstance();

		if (mails != null && mails.size() > 0) {
			mailObj.putInt("noti", mails.size());
			Collection<Long> mailCol = new ArrayList<>(mails.size());
			for (Mail mail : mails) {
				mailCol.add(mail.getId());
			}

			mailObj.putLongArray("ids", mailCol);
		}

		response.putQAntObject("mails", mailObj);
	}


	private void processGiftEvent(QAntUser user, IQAntObject response) {
		long currentTimeMillis = System.currentTimeMillis();
		HeroGift heroGift = giftEventRepository.findOne(user.getName());
		if (heroGift == null || heroGift.isEmpty()) {
			heroGift = new HeroGift();
			heroGift.setGameHeroId(user.getName());

			// Daily gift
			GiftInfo giftInfo = new GiftInfo(giftEventConfig.getDailyGiftEvent());
			giftInfo.setAnchorTime(currentTimeMillis);
			heroGift.addGiftInfo(giftInfo);
		}

		heroGift.setRevision(giftEventConfig.getRevison());

		IQAntObject giftEventObj = QAntObject.newInstance();
		response.putQAntObject("gift_event", giftEventObj);

		Map<Integer, GiftInfo> giftEventMap = heroGift.getGiftEventMap();
		int giftNotification = 0;
		IQAntArray giftArr = QAntArray.newInstance();
		for (GiftInfo giftInfo : giftEventMap.values()) {
			GiftEventBase event = giftEventConfig.getEvent(giftInfo.getGiftIndex());
			switch (event.getCategoryId()) {
				// Daily gift
				case 0:
					boolean over7Day = isOver7Day(new Date(giftInfo.getAnchorTime()), new Date(currentTimeMillis));
					if (over7Day) {
						giftInfo.reset();
					}

					long curDate = DateUtils.truncate(new Date(), Calendar.DATE).getTime();
					long preDate = DateUtils.truncate(new Date(giftInfo.getLastUpdateTime()), Calendar.DATE).getTime();
					if (curDate != preDate) {
						giftInfo.setClaim(true);
						giftInfo.claimNextPackage();
					}

					if (giftInfo.isClaim()) {
						giftNotification++;
					}
					break;

				default:
					break;
			}
			giftInfo.setLastUpdateTime(currentTimeMillis);

			giftArr.addQAntObject(giftInfo.convertToQAntObject());
		}
		giftEventObj.putQAntArray("gifts", giftArr);
		giftEventObj.putInt("noti", giftNotification);

		response.putQAntObject("daily_gift", QAntObject.newFromObject(giftEventConfig.getDailyGiftEvent()));
		giftEventRepository.save(heroGift);
	}


	private boolean isOver7Day(Date firstDate, Date secondDate) {
		long betweenDates = 0;
		try {
			betweenDates = betweenDates(firstDate, secondDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return betweenDates > 7;
	}


	private long betweenDates(Date firstDate, Date secondDate) throws IOException {
		return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
	}


	private void updateStamina(QAntUser user, GameHero gameHero) {
		long updateTime = System.currentTimeMillis();
		if (gameHero.getStamina() < gameHero.getMaxStamina()) {
			long deltaTime = updateTime - gameHero.getStaUpdTime();
			int incrStamina = (int) (deltaTime / STAMINA_REHI_TIME_MILI) * STAMINA_REHI_VALUE;

			System.out.println("[ERROR] *********************** INCR STAMINA: " + incrStamina);
			if (incrStamina > 0) {
				int newStamina = gameHero.getStamina() + incrStamina;
				gameHero.setStamina(newStamina > gameHero.getMaxStamina() ? gameHero.getMaxStamina() : newStamina);
				gameHero.setStaUpdTime(updateTime);
			}
		} else if (gameHero.getStamina() >= gameHero.getMaxExp()) {
			gameHero.setStaUpdTime(updateTime);
		}
		gameHeroRep.save(gameHero);
	}


	private GameHero createNewGameHero(String gameHeroId, long creantsUserId) {
		GameHero gameHero = new GameHero();
		gameHero.setId(gameHeroId);
		gameHero.setUserId(creantsUserId);
		gameHero.setName(HERO_NAME_PREFIX + creantsUserId);
		gameHero.setExp(0);
		gameHero.setLevel(1);
		gameHero.setSoul(10);
		gameHero.setStamina(100);
		gameHero.setZen(20000);
		gameHero.setMaxExp(10000);
		gameHero.setVipLevel(1);
		gameHero.setVipPoint(0);
		gameHero.setMaxVipPoint(100);
		gameHeroRep.save(gameHero);

		QAntTracer.debug(this.getClass(), "Create new hero: " + gameHeroId);

		// cho trước 2 nhân vật
		List<HeroClass> endowHeroes = heroManager.endowHeroes(gameHeroId);
		gameHero.setHeroes(endowHeroes);

		// tạo team battle
		BattleTeam battleTeam = new BattleTeam();
		battleTeam.setGameHeroId(gameHeroId);
		Team team = new Team();
		team.setIndex(1);
		battleTeam.addTeam(team);
		team.setName("Team 1");
		for (HeroClass heroClass : endowHeroes) {
			team.addHero(heroClass.getId());
		}
		battleTeamRepository.save(battleTeam);

		// tạo nhiệm vụ cho hero
		questManager.registerQuestsFromHero(gameHero);

		// mở world, chapter, stage, mission
		Stage firstStage = StageConfig.getInstance().getFirstStage();
		HeroStage heroStage = new HeroStage(firstStage);
		heroStage.setHeroId(gameHeroId);
		heroStage.setId(autoIncrementService.genHeroStageId());
		heroStage.setUnlock(true);
		stageRepository.save(heroStage);
		return gameHero;
	}


	private long getStartOfDateMilis() {
		return DateUtils.truncate(new Date(), Calendar.DATE).getTime();
	}


	private long getEndOfDateMilis() {
		return DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1).getTime();
	}

}
