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
	public String rank;
	public int ticketNo;
	public int chaosPoint;
	private transient long beginTime;
	private transient int completedNo;


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


	public long getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
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

}
