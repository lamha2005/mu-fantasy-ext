package com.creants.muext.entities.item;

import org.springframework.data.annotation.Transient;

import com.creants.muext.entities.Item;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Equipment extends Item {
	@JacksonXmlProperty(localName = "EquipSlot", isAttribute = true)
	private int equipSlot;

	@JacksonXmlProperty(localName = "LVRequire", isAttribute = true)
	private int levelRequire;
	@JacksonXmlProperty(localName = "ATK", isAttribute = true)
	private Integer atk;
	@JacksonXmlProperty(localName = "HP", isAttribute = true)
	private Integer hp;
	@JacksonXmlProperty(localName = "MP", isAttribute = true)
	private Integer mp;
	@JacksonXmlProperty(localName = "DEF", isAttribute = true)
	private Integer def;
	private Boolean lock;
	private Boolean wearing;
	@JacksonXmlProperty(localName = "AvailableClass", isAttribute = true)
	private transient String availableHeroesString;
	@Transient
	private int[] availableHeroes;


	public int getEquipSlot() {
		return equipSlot;
	}


	public void setEquipSlot(int equipSlot) {
		this.equipSlot = equipSlot;
	}


	public int getLevelRequire() {
		return levelRequire;
	}


	public void setLevelRequire(int levelRequire) {
		this.levelRequire = levelRequire;
	}


	public Integer getAtk() {
		return atk;
	}


	public void setAtk(Integer atk) {
		this.atk = atk;
	}


	public Integer getHp() {
		return hp;
	}


	public void setHp(Integer hp) {
		this.hp = hp;
	}


	public Integer getMp() {
		return mp;
	}


	public void setMp(Integer mp) {
		this.mp = mp;
	}


	public Integer getDef() {
		return def;
	}


	public void setDef(Integer def) {
		this.def = def;
	}


	public Boolean getLock() {
		return lock;
	}


	public void setLock(Boolean lock) {
		this.lock = lock;
	}


	public Boolean getWearing() {
		return wearing;
	}


	public void setWearing(Boolean wearing) {
		this.wearing = wearing;
	}


	public String getAvailableHeroesString() {
		return availableHeroesString;
	}


	public void setAvailableHeroesString(String availableHeroesString) {
		this.availableHeroesString = availableHeroesString;
	}


	public int[] getAvailableHeroes() {
		return availableHeroes;
	}


	public void setAvailableHeroes(int[] availableHeroes) {
		this.availableHeroes = availableHeroes;
	}

}
