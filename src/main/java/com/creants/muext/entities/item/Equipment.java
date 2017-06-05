package com.creants.muext.entities.item;

import com.creants.muext.entities.Item;

/**
 * @author LamHM
 *
 */
public class Equipment extends Item {
	private int equipSlot;
	private int levelRequire;
	private Integer atk;
	private Integer hp;
	private Integer mp;
	private Integer def;
	private Boolean lock;
	private Boolean wearing;
	private int[] availableHeros;


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


	public int[] getAvailableHeros() {
		return availableHeros;
	}


	public void setAvailableHeros(int[] availableHeros) {
		this.availableHeros = availableHeros;
	}

}
