package com.creants.muext.entities;

import com.creants.muext.entities.states.AdditionLevelUpStats;
import com.creants.muext.entities.states.BaseStats;
import com.creants.muext.entities.states.SubStats;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Character {
	@JacksonXmlProperty(localName = "Name")
	private String name;

	@JacksonXmlProperty(localName = "BaseStats")
	private transient BaseStats baseStats;
	@JacksonXmlProperty(localName = "SubStats")
	private SubStats subStats;
	@JacksonXmlProperty(localName = "LevelUpStats")
	private AdditionLevelUpStats levelUpStats;


	public Character() {
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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


	public AdditionLevelUpStats getLevelUpStats() {
		return levelUpStats;
	}

}
