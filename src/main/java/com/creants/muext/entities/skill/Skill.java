package com.creants.muext.entities.skill;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class Skill implements SerializableQAntType {
	public int index;
	public int level;


	public Skill() {
	}


	public Skill(int index, int level) {
		this.index = index;
		this.level = level;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public void upgradeLevel(int value) {
		this.level += value;
	}

}
