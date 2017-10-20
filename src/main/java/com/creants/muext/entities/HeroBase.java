package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.creants.muext.entities.skill.Skill;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class HeroBase {
	@JacksonXmlProperty(localName = "ClassGroup", isAttribute = true)
	private int classGroup;

	@JacksonXmlProperty(localName = "ClassIndex", isAttribute = true)
	private int index;

	@JacksonXmlProperty(localName = "CharClass")
	private String name;

	@JacksonXmlProperty(localName = "CRIT", isAttribute = true)
	private float critch;
	@JacksonXmlProperty(localName = "MaxMP", isAttribute = true)
	private int maxMp;
	@JacksonXmlProperty(localName = "MPRec", isAttribute = true)
	private int mpRec;

	@JacksonXmlProperty(localName = "PhysicAttack", isAttribute = true)
	private int atk;
	@JacksonXmlProperty(localName = "HealthPoint", isAttribute = true)
	private int hp;
	@JacksonXmlProperty(localName = "Defense", isAttribute = true)
	private int def;
	@JacksonXmlProperty(localName = "Recovery", isAttribute = true)
	private int rec;

	@JacksonXmlProperty(localName = "LvUpATK", isAttribute = true)
	private int lvUpATK;
	@JacksonXmlProperty(localName = "LvUpHP", isAttribute = true)
	private int lvUpHP;
	@JacksonXmlProperty(localName = "LvUpDEF", isAttribute = true)
	private int lvUpDEF;
	@JacksonXmlProperty(localName = "LvUpREC", isAttribute = true)
	private int lvUpREC;

	@JacksonXmlProperty(localName = "CharRank", isAttribute = true)
	private int rank;
	@JacksonXmlProperty(localName = "MaxRank", isAttribute = true)
	private int maxRank;
	@JacksonXmlProperty(localName = "EvolveTo", isAttribute = true)
	private Integer evolveTo;

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


	public int getMaxRank() {
		return maxRank;
	}


	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public float getCritch() {
		return critch;
	}


	public void setCritch(float critch) {
		this.critch = critch;
	}


	public int getMaxMp() {
		return maxMp;
	}


	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}


	public int getMpRec() {
		return mpRec;
	}


	public void setMpRec(int mpRec) {
		this.mpRec = mpRec;
	}


	public int getAtk() {
		return atk;
	}


	public void setAtk(int atk) {
		this.atk = atk;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public int getDef() {
		return def;
	}


	public void setDef(int def) {
		this.def = def;
	}


	public int getRec() {
		return rec;
	}


	public void setRec(int rec) {
		this.rec = rec;
	}


	public int getLvUpATK() {
		return lvUpATK;
	}


	public void setLvUpATK(int lvUpATK) {
		this.lvUpATK = lvUpATK;
	}


	public int getLvUpHP() {
		return lvUpHP;
	}


	public void setLvUpHP(int lvUpHP) {
		this.lvUpHP = lvUpHP;
	}


	public float getLvUpDEF() {
		return lvUpDEF;
	}


	public void setLvUpDEF(int lvUpDEF) {
		this.lvUpDEF = lvUpDEF;
	}


	public float getLvUpREC() {
		return lvUpREC;
	}


	public void setLvUpREC(int lvUpREC) {
		this.lvUpREC = lvUpREC;
	}


	public Integer getEvolveTo() {
		return evolveTo;
	}


	public void setEvolveTo(Integer evolveTo) {
		this.evolveTo = evolveTo;
	}


	public int[] getSkills() {
		String[] split = StringUtils.split(skillIndex, "#");
		int[] skills = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			skills[i] = Integer.parseInt(split[i]);
		}

		return skills;
	}


	public List<Skill> getSkillList() {
		List<Skill> skills = new ArrayList<>();
		int[] skillArr = getSkills();
		if (skillArr.length > 0) {
			for (int i = 0; i < skillArr.length; i++) {
				skills.add(new Skill(skillArr[0], 1));
			}
		}
		return skills;
	}
}
