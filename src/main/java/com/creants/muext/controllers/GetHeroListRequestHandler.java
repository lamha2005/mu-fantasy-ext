package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Team;
import com.creants.muext.managers.HeroClassManager;

/**
 * @author LamHM
 *
 */
public class GetHeroListRequestHandler extends BaseClientRequestHandler {
	private BattleTeamRepository battleTeamRep;
	private HeroClassManager heroManager;


	public GetHeroListRequestHandler() {
		battleTeamRep = Creants2XApplication.getBean(BattleTeamRepository.class);
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		params = QAntObject.newInstance();
		String gameHeroId = user.getName();
		List<HeroClass> heroes = heroManager.findHeroesByGameHeroId(gameHeroId);

		IQAntArray arr = QAntArray.newInstance();
		for (HeroClass heroClass : heroes) {
			arr.addQAntObject(QAntObject.newFromObject(heroClass));
		}
		params.putQAntArray("heroes", arr);

		BattleTeam battleTeam = battleTeamRep.findOne(gameHeroId);
		List<Team> teamList = battleTeam.getTeamList();
		IQAntArray teamArr = QAntArray.newInstance();
		for (Team team : teamList) {
			QAntObject teamObj = QAntObject.newInstance();
			int index = team.getIndex();
			teamObj.putInt("index", index > 0 ? index : -1);
			teamObj.putUtfString("name", team.getName());
			teamObj.putLongArray("heroes", new ArrayList<Long>(Arrays.asList(team.getHeroes())));
			teamArr.addQAntObject(teamObj);
		}
		params.putQAntArray("teams", teamArr);

		send("cmd_heroes", params, user);
	}

}
