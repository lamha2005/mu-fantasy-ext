package com.creants.muext.entities;

import com.creants.muext.entities.states.BaseStats;
import com.creants.muext.entities.states.SubStats;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Character {
	protected String name;
	protected int level;

	@JacksonXmlProperty(localName = "BaseStats")
	private BaseStats baseStats;
	@JacksonXmlProperty(localName = "SubStats")
	private SubStats subStats;


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
