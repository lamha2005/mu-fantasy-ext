package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.managers.HeroClassManager;

/**
 * @author LamHM
 *
 */
public class SummonRequestHandler extends BaseClientRequestHandler {

	private HeroClassManager heroManager;


	public SummonRequestHandler() {
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();
		List<HeroClass> heroes = heroManager.summon(gameHeroId);
		QAntObject response = QAntObject.newInstance();
		IQAntArray heroArr = QAntArray.newInstance();
		for (HeroClass heroClass : heroes) {
			heroArr.addQAntObject(QAntObject.newFromObject(heroClass));
		}

		response.putQAntArray("heroes", heroArr);

		send(ExtensionEvent.CMD_SUMMON, response, user);
	}

}
