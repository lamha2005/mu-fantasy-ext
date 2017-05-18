package com.creants.muext.controllers;

import java.util.List;

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
import com.creants.muext.config.WorldConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.HeroRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.world.Mission;
import com.creants.muext.managers.MatchManager;
import com.creants.muext.util.UserHelper;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class MissionRequestHandler extends BaseClientRequestHandler {
	private MatchManager matchManager;
	private HeroRepository heroRepository;
	private GameHeroRepository repository;


	public MissionRequestHandler() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		heroRepository = Creants2XApplication.getBean(HeroRepository.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String gameHeroId = UserHelper.getGameHeroId(user);
		int missionIndex = params.getInt("msid");
		Mission mission = WorldConfig.getInstance().getMission(missionIndex);
		if (mission == null) {
			// TODO throw exception
			return;
		}

		GameHero gameHero = repository.findOne(gameHeroId);
		gameHero.setStamina(gameHero.getStamina() - mission.getStaminaCost());
		repository.save(gameHero);

		List<Integer[]> roundList = mission.getRoundList();
		List<Monster> monsterList = matchManager.getMonsterList(roundList);

		int roundNo = roundList.size();
		params = QAntObject.newInstance();
		params.putInt("msid", missionIndex);
		params.putQAntArray("round", matchManager.getRounds(mission, monsterList));

		IQAntArray monsters = QAntArray.newInstance();
		for (Monster monster : monsterList) {
			monsters.addQAntObject(QAntObject.newFromObject(monster));
		}
		params.putQAntArray("monsters", monsters);

		IQAntObject reward = QAntObject.newInstance();
		reward.putInt("exp", 100);
		reward.putInt("zen", 1000);
		params.putQAntObject("reward", reward);

		IQAntObject script = QAntObject.newInstance();
		IQAntArray monsterArr = QAntArray.newInstance();
		for (Monster monster : monsterList) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putInt("id", monster.getId());
			obj.putByteArray("x2", monster.genX2Dam(roundNo));
			monsterArr.addQAntObject(obj);
		}
		script.putQAntArray("monsters", monsterArr);

		IQAntArray heroArr = QAntArray.newInstance();
		List<HeroClass> heroes = heroRepository.findHeroesByGameHeroId(gameHeroId);
		for (HeroClass hero : heroes) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putLong("id", hero.getId());
			obj.putByteArray("crit", hero.genCrit(roundNo));
			heroArr.addQAntObject(obj);
		}
		script.putQAntArray("heroes", heroArr);

		params.putQAntObject("script", script);
		sendExtResponse("cmd_mission", params, user);
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
