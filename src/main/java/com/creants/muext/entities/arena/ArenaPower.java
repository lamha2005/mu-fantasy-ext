package com.creants.muext.entities.arena;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "arena-power")
public class ArenaPower implements SerializableQAntType {
	@Id
	private transient String gameHeroId;
	public Long[] battleTeam;
	private int teamPower;
	private int rank;
	private transient boolean isNPC;


	public ArenaPower() {
		battleTeam = new Long[] { -1L, -1L, -1L, -1L };
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public Long[] getBattleTeam() {
		return battleTeam;
	}


	public void setBattleTeam(Long[] battleTeam) {
		this.battleTeam = battleTeam;
	}


	public int getTeamPower() {
		return teamPower;
	}


	public void setTeamPower(int teamPower) {
		this.teamPower = teamPower;
	}


	public boolean isNPC() {
		return isNPC;
	}


	public void setNPC(boolean isNPC) {
		this.isNPC = isNPC;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	@Override
	public String toString() {
		return "{gameHeroId: " + gameHeroId + ",teamPower:" + teamPower + ", rank:" + rank + "}";
	}

}
