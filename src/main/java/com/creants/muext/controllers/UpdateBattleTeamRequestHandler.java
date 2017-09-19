package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.Team;

/**
 * @author LamHM
 *
 */
public class UpdateBattleTeamRequestHandler extends BaseClientRequestHandler {
	private BattleTeamRepository battleTeamRep;


	public UpdateBattleTeamRequestHandler() {
		battleTeamRep = Creants2XApplication.getBean(BattleTeamRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		IQAntArray teamArr = params.getCASArray("teams");
		if (teamArr == null)
			return;

		// TODO check có sở hữu các hero đó ko
		BattleTeam battleTeam = new BattleTeam();
		battleTeam.setGameHeroId(user.getName());
		for (int i = 0; i < teamArr.size(); i++) {
			IQAntObject teamObj = teamArr.getQAntObject(i);
			Team team = new Team();
			String name = teamObj.getUtfString("name");
			team.setName(StringUtils.isBlank(name) ? ("Team " + (i + 1)) : name);
			Integer index = teamObj.getInt("index");
			team.setIndex(index != null ? index : -1);

			Collection<Integer> heroList = teamObj.getIntArray("heroes");
			Long[] heroArr = new Long[4];
			int count = 0;
			for (long l : heroList) {
				heroArr[count] = l;
				count++;
			}
			team.setHeroes(heroArr);

			battleTeam.addTeam(team);
		}

		battleTeamRep.save(battleTeam);

		params = QAntObject.newInstance();
		List<Team> teamList = battleTeam.getTeamList();
		teamArr = QAntArray.newInstance();
		for (Team team : teamList) {
			QAntObject teamObj = QAntObject.newInstance();
			teamObj.putInt("index", team.getIndex());
			teamObj.putUtfString("name", team.getName());
			teamObj.putLongArray("heroes", new ArrayList<Long>(Arrays.asList(team.getHeroes())));
			teamArr.addQAntObject(teamObj);
		}
		params.putQAntArray("teams", teamArr);

		send(ExtensionEvent.CMD_UPD_BATTLE_TEAM, params, user);
	}

}
