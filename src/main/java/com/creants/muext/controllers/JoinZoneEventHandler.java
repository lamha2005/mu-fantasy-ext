package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.List;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.QAntEventParam;
import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.HeroRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;
import com.creants.muext.entities.quest.QuestStats;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.services.QuestManager;

/**
 * @author LamHM
 *
 */
public class JoinZoneEventHandler extends BaseServerEventHandler {
	private static final String SERVER_ID = "mus1";
	private static final String HERO_NAME_PREFIX = "Mu Hero ";
	private GameHeroRepository repository;
	private HeroRepository heroRepository;
	private QuestStatsRepository questStatsRepository;
	private QuestManager questManager;
	private HeroClassManager heroManager;


	public JoinZoneEventHandler() {
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		questStatsRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
		questManager = Creants2XApplication.getBean(QuestManager.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		heroRepository = Creants2XApplication.getBean(HeroRepository.class);
	}


	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		QAntUser user = (QAntUser) event.getParameter(QAntEventParam.USER);
		long creantsUserId = user.getCreantsUserId();
		String id = SERVER_ID + "#" + creantsUserId;

		GameHero gameHero = repository.findOne(id);
		if (gameHero == null) {
			gameHero = createNewGameHero(creantsUserId);
		}else{
			List<HeroClass> heroes = heroRepository.findHeroesByGameHeroId(id);
			gameHero.setHeroes(heroes);
		}

		List<QuestStats> mainQuests = questStatsRepository.findByHeroIdAndGroupId(id, QuestManager.GROUP_MAIN_QUEST);
		IQAntObject params = new QAntObject();
		params.putQAntObject("game_hero", QAntObject.newFromObject(gameHero));

		IQAntArray questArr = QAntArray.newInstance();
		IQAntObject mainQuest = QAntObject.newInstance();
		mainQuest.putInt("gid", QuestManager.GROUP_MAIN_QUEST);
		mainQuest.putUtfString("n", "Nhiệm vụ chính");
		mainQuest.putInt("no", mainQuests == null ? 0 : mainQuests.size());
		questArr.addQAntObject(mainQuest);
		IQAntObject dailyQuest = QAntObject.newInstance();
		dailyQuest.putInt("gid", QuestManager.GROUP_DAILY_QUEST);
		dailyQuest.putUtfString("n", "Nhiệm vụ hàng ngày");
		dailyQuest.putInt("no", 10);
		questArr.addQAntObject(dailyQuest);
		params.putQAntArray("quests", questArr);

		params.putInt("event_no", 10);
		sendExtResponse("join_game", params, user);

		return;
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


	private GameHero createNewGameHero(long creantsUserId) {
		GameHero gameHero = new GameHero(SERVER_ID, creantsUserId);
		gameHero.setName(HERO_NAME_PREFIX + creantsUserId);
		gameHero.setExp(0);
		gameHero.setLevel(1);
		gameHero.setSoul(0);
		gameHero.setStamina(60);
		gameHero.setZen(0);
		gameHero.setVipLevel(1);
		gameHero.setVipPoint(0);

		List<HeroClass> heroes = new ArrayList<>(3);
		heroes.add(heroManager.createNewHero(gameHero.getId(), HeroClassType.DARK_KNIGHT));
		heroes.add(heroManager.createNewHero(gameHero.getId(), HeroClassType.FAIRY_ELF));
		heroes.add(heroManager.createNewHero(gameHero.getId(), HeroClassType.DARK_WIZARD));
		gameHero.setHeroes(heroes);

		heroRepository.save(heroes);
		gameHero = repository.save(gameHero);

		questManager.registerQuestsFromHero(gameHero);
		return gameHero;
	}

}
