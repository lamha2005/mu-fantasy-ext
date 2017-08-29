package com.creants.muext.entities.chaos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "chaos-castle")
public class ChaosCastleInfo implements SerializableQAntType {
	@Id
	private transient String gameHeroId;
	public int ticketNo;
	public int chaosPoint;
	public Long[] battleTeam;
	private transient long beginTime;
	private ChaosCastleStage unlockStage;
	private transient int lastStageFinish;
	public String rank;
	private transient int completedNo;
	private transient int maxPower;
	private int teamPower;
	private boolean chaosCastleUnlock;
	private transient boolean isNPC;


	public ChaosCastleInfo() {
		battleTeam = new Long[] { -1L, -1L, -1L, -1L };
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public int getTicketNo() {
		return ticketNo;
	}


	public void setTicketNo(int ticketNo) {
		this.ticketNo = ticketNo;
	}


	public int getChaosPoint() {
		return chaosPoint;
	}


	public void setChaosPoint(int chaosPoint) {
		this.chaosPoint = chaosPoint;
	}


	public Long[] getBattleTeam() {
		return battleTeam;
	}


	public void setBattleTeam(Long[] battleTeam) {
		this.battleTeam = battleTeam;
	}


	public long getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}


	public ChaosCastleStage getUnlockStage() {
		return unlockStage;
	}


	public void setUnlockStage(ChaosCastleStage unlockStage) {
		this.unlockStage = unlockStage;
	}


	public int getLastStageFinish() {
		return lastStageFinish;
	}


	public void setLastStageFinish(int lastStageFinish) {
		this.lastStageFinish = lastStageFinish;
	}


	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public int getCompletedNo() {
		return completedNo;
	}


	public void setCompletedNo(int completedNo) {
		this.completedNo = completedNo;
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


	public boolean isChaosCastleUnlock() {
		return chaosCastleUnlock;
	}


	public void setChaosCastleUnlock(boolean chaosCastleUnlock) {
		this.chaosCastleUnlock = chaosCastleUnlock;
	}


	public boolean isNPC() {
		return isNPC;
	}


	public void setNPC(boolean isNPC) {
		this.isNPC = isNPC;
	}

}
