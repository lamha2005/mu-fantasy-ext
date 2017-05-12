package com.creants.muext.entities;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.entities.states.BaseStats;
import com.creants.muext.entities.states.SubStats;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Character implements SerializableQAntType {
	@JacksonXmlProperty(localName = "Name")
	public String name;
	@JacksonXmlProperty(localName = "Level")
	public int level;

	@JacksonXmlProperty(localName = "BaseStats")
	private transient BaseStats baseStats;
	@JacksonXmlProperty(localName = "SubStats")
	private SubStats subStats;


	public Character() {
		level = 1;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public BaseStats getBaseStats() {
		return baseStats;
	}


	public void setBaseStats(BaseStats baseStats) {
		this.baseStats = baseStats;
	}


	public SubStats getSubStats() {
		return subStats;
	}


	public void setSubStats(SubStats subStats) {
		this.subStats = subStats;
	}

}
