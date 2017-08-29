package com.creants.muext.entities.npc;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class NPCHeroClass {
	@JacksonXmlProperty(localName = "ClassIndex", isAttribute = true)
	private int classIndex;
	@JacksonXmlProperty(localName = "CharRank", isAttribute = true)
	private int charRank;
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	private int level;


	public int getClassIndex() {
		return classIndex;
	}


	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}


	public int getCharRank() {
		return charRank;
	}


	public void setCharRank(int charRank) {
		this.charRank = charRank;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}

}
