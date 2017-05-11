package com.creants.muext.entities.world;

import java.util.List;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class Stage implements SerializableQAntType {
	private int id;
	private String name;
	private boolean clear;
	private boolean unlock;
	private Integer startNo;
	private transient List<Mission> missions;


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


	public List<Mission> getMissions() {
		return missions;
	}


	public void setMissions(List<Mission> missions) {
		this.missions = missions;
	}


	public boolean isClear() {
		return clear;
	}


	public void setClear(boolean clear) {
		this.clear = clear;
	}


	public Integer getStartNo() {
		return startNo;
	}


	public void setStartNo(Integer startNo) {
		this.startNo = startNo;
	}


	public boolean isUnlock() {
		return unlock;
	}


	public void setUnlock(boolean unlock) {
		this.unlock = unlock;
	}

}
