package com.creants.muext.entities.chaos;

import java.util.List;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class ChaosCastleStage implements SerializableQAntType {
	private int id;
	private String accName;
	private List<ChaosHeroClass> heroes;
	private int teamPower;
	private int level;
	private String rewardString;
	private ChaosRewardPackage rewardPackage;
	private boolean clamed;


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


	public List<ChaosHeroClass> getHeroes() {
		return heroes;
	}


	public void setHeroes(List<ChaosHeroClass> heroes) {
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


	public String getRewardString() {
		return rewardString;
	}


	public void setRewardString(String rewardString) {
		this.rewardString = rewardString;
	}


	public ChaosRewardPackage getRewardPackage() {
		return rewardPackage;
	}


	public void setRewardPackage(ChaosRewardPackage rewardPackage) {
		this.rewardPackage = rewardPackage;
	}


	public boolean isClamed() {
		return clamed;
	}


	public void setClamed(boolean clamed) {
		this.clamed = clamed;
	}

}
