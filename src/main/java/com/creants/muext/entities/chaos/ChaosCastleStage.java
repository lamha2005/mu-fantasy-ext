package com.creants.muext.entities.chaos;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.entities.ext.ShortItemExt;

/**
 * @author LamHM
 *
 */
public class ChaosCastleStage implements SerializableQAntType {
	public int id;
	public String accName;
	public Long[] battleTeam;
	public int teamPower;
	public int level;
	public List<OpponentHeroClass> heroes;

	@Transient
	public List<ShortItemExt> reward;
	public boolean clamed;


	public ChaosCastleStage() {
		battleTeam = new Long[] { -1L, -1L, -1L, -1L };
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getAccName() {
		return accName;
	}


	public void setAccName(String accName) {
		this.accName = accName;
	}


	public List<OpponentHeroClass> getHeroes() {
		return heroes;
	}


	public void setHeroes(List<OpponentHeroClass> heroes) {
		this.heroes = heroes;
	}


	public int getTeamPower() {
		return teamPower;
	}


	public void setTeamPower(int teamPower) {
		this.teamPower = teamPower;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public List<ShortItemExt> getReward() {
		return reward;
	}


	public void setReward(List<ShortItemExt> reward) {
		this.reward = reward;
	}


	public boolean isClamed() {
		return clamed;
	}


	public void setClamed(boolean clamed) {
		this.clamed = clamed;
	}


	public Long[] getBattleTeam() {
		return battleTeam;
	}


	public void setBattleTeam(Long[] battleTeam) {
		this.battleTeam = battleTeam;
	}

}
