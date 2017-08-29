package com.creants.muext.entities.chaos;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class ChaosRewardPackage implements SerializableQAntType {
	private int zen;
	private int chaosPoint;
	private String rewardString;


	public int getZen() {
		return zen;
	}


	public void setZen(int zen) {
		this.zen = zen;
	}


	public int getChaosPoint() {
		return chaosPoint;
	}


	public void setChaosPoint(int chaosPoint) {
		this.chaosPoint = chaosPoint;
	}


	public String getRewardString() {
		return rewardString;
	}


	public void setRewardString(String rewardString) {
		this.rewardString = rewardString;
	}

}
