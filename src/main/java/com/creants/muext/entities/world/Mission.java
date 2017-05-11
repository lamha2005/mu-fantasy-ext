package com.creants.muext.entities.world;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class Mission implements SerializableQAntType {
	private int id;
	private String name;
	private String desc;
	private int staminaCost;
	private boolean clear;
	private Integer starNo;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public int getStaminaCost() {
		return staminaCost;
	}


	public void setStaminaCost(int staminaCost) {
		this.staminaCost = staminaCost;
	}


	public boolean isClear() {
		return clear;
	}


	public void setClear(boolean clear) {
		this.clear = clear;
	}


	public Integer getStarNo() {
		return starNo;
	}


	public void setStarNo(Integer starNo) {
		this.starNo = starNo;
	}

}
