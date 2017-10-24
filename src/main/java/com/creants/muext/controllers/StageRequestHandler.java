package com.creants.muext.controllers;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.Team;
import com.creants.muext.entities.item.HeroItem;
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
	public static final String STAGE_INDEX = "stgid";
	public static final int STAMINA_REHI_TIME_MILI = 60000;// 6 phút rehi
															// stamina 1 lần
	public static final int STAMINA_REHI_VALUE = 1;
	private MatchManager matchManager;
	private GameHeroRepository repository;
	private HeroClassManager heroManager;
	private BattleTeamRepository battleTeamRep;


	public StageRequestHandler() {
		matchManager = Creants2XApplication.getBean(MatchManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		battleTeamRep = Creants2XApplication.getBean(BattleTeamRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject matchInfo) {
		String gameHeroId = user.getName();
		int stageIndex = matchInfo.getInt(STAGE_INDEX);
		Stage stage = StageConfig.getInstance().getStage(stageIndex);
		if (stage == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_JOIN_STAGE, GameErrorCode.STAGE_NOT_FOUND),
					user);
			return;
		}

		GameHero gameHero = repository.findOne(gameHeroId);
		rehiStamina(gameHero);
		// trừ stamina khi vào bàn chơi
		gameHero.decrStamina(stage.getStaminaCost());

		repository.save(gameHero);

		// gửi thông tin stamina thay đổi
		IQAntObject assets = QAntObject.newInstance();
		assets.putInt("stamina", gameHero.getStamina());
		send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, assets, user);

		List<Integer[]> roundList = stage.getRoundList();
		List<Monster> monsterList = matchManager.getMonsterList(roundList);

		int roundNo = roundList.size();
		matchInfo = QAntObject.newInstance();
		matchInfo.putInt(STAGE_INDEX, stageIndex);
		matchInfo.putQAntArray("round", matchManager.getRounds(stage, monsterList));

		IQAntArray monsters = QAntArray.newInstance();
		for (Monster monster : monsterList) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putInt("id", monster.getId());
			obj.putInt("index", monster.getIndex());
			monsters.addQAntObject(obj);
		}
		matchInfo.putQAntArray("monsters", monsters);

		String itemBonus = getItemBonus(stage.getBonus());
		matchInfo.putQAntArray("items_bonus", ItemConfig.getInstance().buildShortItemInfo(itemBonus));

		IQAntObject script = QAntObject.newInstance();
		IQAntArray monsterArr = QAntArray.newInstance();
		for (Monster monster : monsterList) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putInt("id", monster.getId());
			obj.putByteArray("crit", monster.genX2Dam(roundNo));
			monsterArr.addQAntObject(obj);
		}
		script.putQAntArray("monsters", monsterArr);

		BattleTeam battleTeam = battleTeamRep.findOne(gameHeroId);
		Team team = battleTeam.getTeam(0);
		IQAntArray heroArr = QAntArray.newInstance();
		List<HeroClass> heroes = heroManager.findHeroes(team.getIdList());
		for (HeroClass hero : heroes) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putLong("id", hero.getId());
			obj.putByteArray("crit", hero.genCrit(roundNo));
			heroArr.addQAntObject(obj);
		}

		script.putQAntArray("heroes", heroArr);
		matchInfo.putQAntObject("script", script);
		send(ExtensionEvent.CMD_JOIN_STAGE, matchInfo, user);

		matchManager.newMatch(gameHeroId, matchInfo);
	}


	private String getItemBonus(List<HeroItem> items) {
		String result = "";
		for (HeroItem heroItem : items) {
			int rate = RandomUtils.nextInt(100);
			if (rate <= 200) {
				result += "#" + heroItem.getIndex() + "/" + heroItem.getNo();
			}
		}
		return result;
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
