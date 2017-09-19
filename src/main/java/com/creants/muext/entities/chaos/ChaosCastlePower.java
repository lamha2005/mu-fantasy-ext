package com.creants.muext.entities.chaos;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "chaos-castle-power")
public class ChaosCastlePower implements SerializableQAntType {
	@Id
	private transient String gameHeroId;
	public Long[] battleTeam;
	private transient int maxPower;
	private int teamPower;
	private transient boolean isNPC;


	public ChaosCastlePower() {
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


	public int getMaxPower() {
		return maxPower;
	}


	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
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


	public void setBattleTeam(Collection<Long> heroes) {
		int index = 0;
		for (Long id : heroes) {
			this.battleTeam[index++] = id;
		}
	}

}
