package com.creants.muext.entities.item;

import org.springframework.data.annotation.Transient;

/**
 * @author LamHM
 *
 */
public class HeroEquipment extends HeroItem {
	private int level;
	private Long heroId;
	private Integer slotIndex;
	@Transient
	private int atk;
	@Transient
	private int hp;
	@Transient
	private int rec;
	@Transient
	private int def;

	private int power;
	@Transient
	private transient EquipmentBase equipmentBase;


	public HeroEquipment() {
	};


	public HeroEquipment(EquipmentBase equipmentBase) {
		this.equipmentBase = equipmentBase;
		setIndex(equipmentBase.getIndex());
		setItemGroup(equipmentBase.getGroupId());
		setElement(equipmentBase.getElemental());
		levelUp(0);
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public Long getHeroId() {
		return heroId;
	}


	public void setHeroId(Long heroId) {
		this.heroId = heroId;
	}


	public Integer getSlotIndex() {
		return slotIndex;
	}


	public void setSlotIndex(Integer slotIndex) {
		this.slotIndex = slotIndex;
	}


	public EquipmentBase getEquipmentBase() {
		return equipmentBase;
	}


	public void setEquipmentBase(EquipmentBase equipmentBase) {
		this.equipmentBase = equipmentBase;
		setIndex(equipmentBase.getIndex());
		setItemGroup(equipmentBase.getGroupId());
		levelUp(0);
	}


	public void takeOn(Long heroId, int slotIndex) {
		this.heroId = heroId;
		this.slotIndex = slotIndex;
	}


	public void takeOff() {
		this.heroId = null;
		this.slotIndex = null;
	}


	public void levelUp(int value) {
		level += value;
		updateBaseStats();
	}


	private void updateBaseStats() {
		atk = equipmentBase.getAtk() + (level - 1) * equipmentBase.getLvUpATK();
		hp = equipmentBase.getHp() + (level - 1) * equipmentBase.getLvUpHP();
		def = (int) (equipmentBase.getDef() + (level - 1) * equipmentBase.getLvUpDEF());
		rec = (int) (equipmentBase.getRec() + (level - 1) * equipmentBase.getLvUpREC());
		power = atk * 3 + def + hp + rec / 5;
	}


	public int getPower() {
		return power;
	}


	public int getAtk() {
		return atk;
	}


	public int getHp() {
		return hp;
	}


	public int getRec() {
		return rec;
	}


	public int getDef() {
		return def;
	}

}
