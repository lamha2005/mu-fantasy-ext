package com.creants.muext.entities;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class HeroBase extends Character {
	@JacksonXmlProperty(localName = "ClassIndex", isAttribute = true)
	private int index;

	@JacksonXmlProperty(localName = "ClassGroup", isAttribute = true)
	private int classGroup;

	@JacksonXmlProperty(localName = "CharRank", isAttribute = true)
	private int rank;

	@JacksonXmlProperty(localName = "Element", isAttribute = true)
	private String element;

	@JacksonXmlProperty(localName = "SkillIndex", isAttribute = true)
	private String skillIndex;

	@JacksonXmlProperty(localName = "LeaderSkill", isAttribute = true)
	private String leaderSkill;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public String getElement() {
		return element;
	}


	public void setElement(String element) {
		this.element = element;
	}


	public String getSkillIndex() {
		return skillIndex;
	}


	public void setSkillIndex(String skillIndex) {
		this.skillIndex = skillIndex;
	}


	public int getClassGroup() {
		return classGroup;
	}


	public void setClassGroup(int classGroup) {
		this.classGroup = classGroup;
	}


	public String getLeaderSkill() {
		return leaderSkill;
	}


	public void setLeaderSkill(String leaderSkill) {
		this.leaderSkill = leaderSkill;
	}


	public int[] getSkills() {
		String[] split = StringUtils.split(skillIndex, "#");
		int[] skills = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			skills[i] = Integer.parseInt(split[i]);
		}

		return skills;
	}
}
