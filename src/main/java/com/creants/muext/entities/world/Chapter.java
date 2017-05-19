package com.creants.muext.entities.world;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class Chapter implements SerializableQAntType {
	private int index;
	private String name;
	private String bg;
	private boolean unlock;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getBg() {
		return bg;
	}


	public void setBg(String bg) {
		this.bg = bg;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isUnlock() {
		return unlock;
	}


	public void setUnlock(boolean unlock) {
		this.unlock = unlock;
	}

}
