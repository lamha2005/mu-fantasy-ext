package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.world.Stage;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.managers.MatchManager;
import com.creants.muext.services.MessageFactory;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class StageRequestHandler extends BaseClientRequestHandler {
	private static final String CMD = "cmd_join_stage";
	public static final String STAGE_INDEX = "stgid";
	// 6 phút rehi stamina 1 lần
	public static final int STAMINA_REHI_TIME_MILI = 60000;
	public static final int STAMINA_REHI_VALUE = 1;
	private MatchManager matchManager;
	private GameHeroRepository repository;
	private HeroClassManager heroManager;


	public StageRequestHandler() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();
		int stageIndex = params.getInt(STAGE_INDEX);
		Stage stage = StageConfig.getInstance().getStage(stageIndex);
		if (stage == null) {
			sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.STAGE_NOT_FOUND), user);
			return;
		}

		GameHero gameHero = repository.findOne(gameHeroId);
		rehiStamina(gameHero);
		// trừ stamina khi vào bàn chơi
		int stamina = gameHero.getStamina() - stage.getStaminaCost();
		gameHero.setStamina(stamina > 0 ? stamina : 0);

		repository.save(gameHero);

		// gửi thông tin stamina thay đổi
		IQAntObject assets = QAntObject.newInstance();
		assets.putInt("stamina", stamina);
		send("cmd_assets_change", assets, user);

		List<Integer[]> roundList = stage.getRoundList();
		List<Monster> monsterList = matchManager.getMonsterList(roundList);

		int roundNo = roundList.size();
		params = QAntObject.newInstance();
		params.putInt(STAGE_INDEX, stageIndex);
		params.putQAntArray("round", matchManager.getRounds(stage, monsterList));

		IQAntArray monsters = QAntArray.newInstance();
		for (Monster monster : monsterList) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putInt("id", monster.getId());
			obj.putInt("index", monster.getIndex());
			monsters.addQAntObject(obj);
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
			obj.putByteArray("crit", monster.genX2Dam(roundNo));
			monsterArr.addQAntObject(obj);
		}
		script.putQAntArray("monsters", monsterArr);

		IQAntArray heroArr = QAntArray.newInstance();
		List<HeroClass> heroes = heroManager.findHeroesByGameHeroId(gameHeroId);
		for (HeroClass hero : heroes) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putLong("id", hero.getId());
			obj.putByteArray("crit", hero.genCrit(roundNo));
			heroArr.addQAntObject(obj);
		}
		script.putQAntArray("heroes", heroArr);

		params.putQAntObject("script", script);
		send(CMD, params, user);

		matchManager.newMatch(gameHeroId, params);
	}


	// TODO tách dùng chung
	private void rehiStamina(GameHero gameHero) {
		long updateTime = System.currentTimeMillis();
		if (gameHero.getStamina() < gameHero.getMaxStamina()) {
			long deltaTime = updateTime - gameHero.getStaUpdTime();
			int incrStamina = (int) (deltaTime / STAMINA_REHI_TIME_MILI) * STAMINA_REHI_VALUE;
			System.out.println("[ERROR] *********************** rehiStamina INCR STAMINA: " + incrStamina);
			if (incrStamina > 0) {
				int newStamina = gameHero.getStamina() + incrStamina;
				gameHero.setStamina(newStamina > gameHero.getMaxStamina() ? gameHero.getMaxStamina() : newStamina);
				gameHero.setStaUpdTime(updateTime);
			}
		} else {
			System.out.println("[DEBUG] *********************** request stage: " + updateTime);
			gameHero.setStaUpdTime(updateTime);
		}
	}

}
