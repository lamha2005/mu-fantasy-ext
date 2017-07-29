package com.creants.muext.controllers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.HeroStageRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Team;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.world.HeroStage;
import com.creants.muext.managers.HeroClassManager;
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

	private GameHeroRepository repository;
	private QuestStatsRepository questStatsRepository;
	private QuestManager questManager;
	private HeroClassManager heroManager;
	private SequenceRepository sequenceRepository;
	private HeroStageRepository stageRepository;
	private AdminManager adminManager;
	private BattleTeamRepository battleTeamRepository;


	public JoinZoneEventHandler() {
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		questStatsRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		sequenceRepository = Creants2XApplication.getBean(SequenceRepository.class);
		stageRepository = Creants2XApplication.getBean(HeroStageRepository.class);
		adminManager = Creants2XApplication.getBean(AdminManager.class);
		battleTeamRepository = Creants2XApplication.getBean(BattleTeamRepository.class);
	}


	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		QAntUser user = (QAntUser) event.getParameter(QAntEventParam.USER);
		user.setLoginTime(System.currentTimeMillis());
		long creantsUserId = user.getCreantsUserId();
		String gameHeroId = user.getName();

		GameHero gameHero = repository.findOne(gameHeroId);
		if (gameHero == null) {
			gameHero = createNewGameHero(gameHeroId, creantsUserId);
			user.setProperty(UserHelper.GAME_HERO_NAME, gameHero.getName());
		} else {
			List<HeroClass> heroes = heroManager.findHeroesByGameHeroId(gameHeroId);
			gameHero.setHeroes(heroes);
		}

		updateStamina(user, gameHero);

		// main quest
		List<HeroQuest> mainQuests = questStatsRepository.getQuests(gameHeroId, QuestManager.GROUP_MAIN_QUEST, false);
		IQAntObject params = new QAntObject();
		params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));
		IQAntArray questArr = QAntArray.newInstance();
		IQAntObject mainQuest = QAntObject.newInstance();
		mainQuest.putInt("gid", QuestManager.GROUP_MAIN_QUEST);
		mainQuest.putUtfString("n", "Nhiệm vụ chính");
		mainQuest.putInt("no", mainQuests == null ? 0 : mainQuests.size());
		questArr.addQAntObject(mainQuest);

		// daily quest
		List<HeroQuest> dailyQuestList = questStatsRepository.findDailyQuests(gameHeroId, getStartOfDateMilis(),
				getEndOfDateMilis());
		if (dailyQuestList == null || dailyQuestList.size() <= 0) {
			dailyQuestList = questManager.resetDailyQuest(gameHero);
		}

		IQAntObject dailyQuest = QAntObject.newInstance();
		dailyQuest.putInt("gid", QuestManager.GROUP_DAILY_QUEST);
		dailyQuest.putUtfString("n", "Nhiệm vụ hàng ngày");
		dailyQuest.putInt("no", dailyQuestList.size());
		questArr.addQAntObject(dailyQuest);
		params.putQAntArray("quests", questArr);

		// team
		BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
		List<Team> teamList = battleTeam.getTeamList();
		IQAntArray teamArr = QAntArray.newInstance();
		for (Team team : teamList) {
			QAntObject teamObj = QAntObject.newInstance();
			teamObj.putUtfString("name", team.getName());
			List<Long> collect = Arrays.stream(team.getHeroes()).boxed().collect(Collectors.toList());
			teamObj.putLongArray("heroes", collect);
			teamArr.addQAntObject(teamObj);
		}
		params.putQAntArray("teams", teamArr);

		// event
		params.putInt("event_no", 10);
		params.putLong("login_time", user.getLoginTime());
		send("join_game", params, user);

		adminManager.incrAndNotifyCCU();
		return;
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
		repository.save(gameHero);
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
		repository.save(gameHero);

		QAntTracer.debug(this.getClass(), "Create new hero: " + gameHeroId);

		// cho trước 2 nhân vật
		List<HeroClass> endowHeroes = heroManager.endowHeroes(gameHeroId);
		gameHero.setHeroes(endowHeroes);

		// tạo team battle
		BattleTeam battleTeam = new BattleTeam();
		battleTeam.setGameHeroId(gameHeroId);
		Team team = new Team();
		battleTeam.addTeam(team);
		team.setName("Team 1");
		for (HeroClass heroClass : endowHeroes) {
			team.addHero(heroClass.getId());
		}
		battleTeamRepository.save(battleTeam);

		// tạo nhiệm vụ cho hero
		questManager.registerQuestsFromHero(gameHero);

		// mở world, chapter, stage, mission
		HeroStage heroStage = new HeroStage(StageConfig.getInstance().getStage(100));
		heroStage.setHeroId(gameHeroId);
		heroStage.setId(sequenceRepository.getNextSequenceId("hero_stage_id"));
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
