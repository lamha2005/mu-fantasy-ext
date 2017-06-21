package com.creants.muext.entities.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/**
 * @author LamHM
 *
 */
public class Equipment {
	@Id
	private long id;
	private String gameHeroId;
	private long heroId;
	private int index;
	private int groupId;
	private boolean lock;
	private boolean wearing;
	private int level;
	private Integer gridIndex;
	private Integer no;
	@Transient
	private EquipmentBase equipmentBase;


	public void setEquipmentBase(EquipmentBase equipmentBase) {
		this.equipmentBase = equipmentBase;
		this.index = equipmentBase.getIndex();
		this.groupId = equipmentBase.getGroupId();
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public boolean isLock() {
		return lock;
	}


	public void setLock(boolean lock) {
		this.lock = lock;
	}


	public boolean isWearing() {
		return wearing;
	}


	public void setWearing(boolean wearing) {
		this.wearing = wearing;
	}


	public EquipmentBase getEquipmentBase() {
		return equipmentBase;
	}


	public int getGroupId() {
		return groupId;
	}


	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public Integer getGridIndex() {
		return gridIndex;
	}


	public void setGridIndex(Integer gridIndex) {
		this.gridIndex = gridIndex;
	}


	public Integer getNo() {
		return no;
	}


	public void setNo(Integer no) {
		this.no = no;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public long getHeroId() {
		return heroId;
	}


	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}

}
