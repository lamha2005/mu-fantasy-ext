package com.creants.muext.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.GameHeroManager;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class SummonRequestHandler extends BaseClientRequestHandler {
	private static final int SUMMON_1 = 1;
	private static final int SUMMON_10 = 10;
	private HeroClassManager heroManager;
	private GameHeroManager gameHeroManager;


	public SummonRequestHandler() {
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		gameHeroManager = Creants2XApplication.getBean(GameHeroManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		if (action == null) {
			action = SUMMON_1;
		}

		switch (action) {
			case SUMMON_1:
				summonX1(user, params);
				break;
			case SUMMON_10:
				summonX10(user, params);
				break;

			default:
				break;
		}

	}


	private void summonX10(QAntUser user, IQAntObject params) {
		GameHero gameHero = gameHeroManager.getHero(user.getName());
		if (gameHero.getBless() < 10) {
			IQAntObject errorMsg = MessageFactory.createErrorMsg(ExtensionEvent.CMD_SUMMON,
					GameErrorCode.NOT_ENOUGH_BLESS);
			errorMsg.putInt("act", SUMMON_10);
			sendError(errorMsg, user);
			return;
		}

		gameHero = gameHeroManager.incrBless(gameHero, -10);
		notifyAssetChange(user, gameHero);
		response(user, heroManager.summonX10(user.getName()), params);
	}


	private void summonX1(QAntUser user, IQAntObject params) {
		GameHero gameHero = gameHeroManager.getHero(user.getName());
		if (gameHero.getBless() < 1) {
			IQAntObject errorMsg = MessageFactory.createErrorMsg(ExtensionEvent.CMD_SUMMON,
					GameErrorCode.NOT_ENOUGH_BLESS);
			errorMsg.putInt("act", SUMMON_1);
			sendError(errorMsg, user);
			return;
		}

		gameHero = gameHeroManager.incrBless(gameHero, -1);
		notifyAssetChange(user, gameHero);

		response(user, heroManager.summonX1(user.getName()), params);
	}


	private void notifyAssetChange(QAntUser user, GameHero gameHero) {
		Map<String, Object> assetMap = new HashMap<>();
		assetMap.put("bless", gameHero.getBless());
		send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, MessageFactory.buildAssetsChange(assetMap), user);
	}


	private void response(QAntUser user, List<HeroClass> heroes, IQAntObject params) {
		IQAntArray heroArr = QAntArray.newInstance();
		for (HeroClass heroClass : heroes) {
			heroArr.addQAntObject(QAntObject.newFromObject(heroClass));
		}

		params.putQAntArray("heroes", heroArr);
		send(ExtensionEvent.CMD_SUMMON, params, user);
	}

}
