package com.creants.muext.entities;

import com.creants.muext.entities.states.AdditionLevelUpStats;
import com.creants.muext.entities.states.AdditionStats;
import com.creants.muext.entities.states.BaseStats;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public abstract class HeroClass extends Character {
	public int id;
	public int exp;
	public int rank;

	public int atk;
	public int mag;
	public int hp;
	public int mp;
	public int def;
	public int res;
	public int spd;

	private AdditionStats additionStats;
	@JacksonXmlProperty(localName = "LevelUpStats")
	private AdditionLevelUpStats levelUpStats;


	public HeroClass() {
		init();
	}


	public abstract void init();


	@SuppressWarnings("unchecked")
	public <T> T setDefaultStats() {
		BaseStats baseStats = getBaseStats();
		rank = 1;
		atk = baseStats.getAtk();
		mag = baseStats.getMag();
		hp = baseStats.getHp();
		mp = baseStats.getMp();
		def = baseStats.getDef();
		res = baseStats.getRes();
		spd = baseStats.getSpd();
		return (T) this;
	}


	public int getId() {
		return id;
	}


	public int getExp() {
		return exp;
	}


	public void setExp(int exp) {
		this.exp = exp;
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


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public AdditionStats getAdditionStats() {
		return additionStats;
	}


	public void setAdditionStats(AdditionStats additionStats) {
		this.additionStats = additionStats;
	}


	public AdditionLevelUpStats getLevelUpStats() {
		return levelUpStats;
	}


	public void setLevelUpStats(AdditionLevelUpStats levelUpStats) {
		this.levelUpStats = levelUpStats;
	}


	public int getAtk() {
		return atk;
	}


	public int getMag() {
		return mag;
	}


	public int getHp() {
		return hp;
	}


	public int getMp() {
		return mp;
	}


	public int getDef() {
		return def;
	}


	public int getRes() {
		return res;
	}


	public int getSpd() {
		return spd;
	}


	public int getExpPoolSize() {
		return -1;
	}


	public void levelUp(int levelUp) {
		atk = getBaseStats().getAtk() + (levelUp - 1) * levelUpStats.getAtk();
		mag = getBaseStats().getMag() + (levelUp - 1) * levelUpStats.getMag();
		hp = getBaseStats().getHp() + (levelUp - 1) * levelUpStats.getHp();
		mp = (int) (getBaseStats().getMp() + (levelUp - 1) * levelUpStats.getMp());
		def = (int) (getBaseStats().getDef() + (levelUp - 1) * levelUpStats.getDef());
		res = (int) (getBaseStats().getRes() + (levelUp - 1) * levelUpStats.getRes());
		spd = (int) (getBaseStats().getSpd() + (levelUp - 1) * levelUpStats.getSpd());

	}

}
