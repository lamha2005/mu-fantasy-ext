package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "battle-team")
public class BattleTeam {
	@Id
	private String gameHeroId;
	private List<Team> teamList;


	public BattleTeam() {
		teamList = new ArrayList<>();
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public List<Team> getTeamList() {
		return teamList;
	}


	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}


	public void addTeam(Team team) {
		teamList.add(team);
	}


	public Team getTeam(int index) {
		return teamList.get(index);
	}


	public Team getMainTeam() {
		return getTeam(0);
	}


	public List<Long> getMainTeamIds() {
		return getTeam(0).getIdList();
	}

}
