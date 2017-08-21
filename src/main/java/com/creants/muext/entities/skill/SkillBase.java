package com.creants.muext.entities.skill;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class SkillBase {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	@Transient
	private String name;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	@Transient
	private String desc;
	@JacksonXmlProperty(localName = "ManaCost", isAttribute = true)
	private int manaCost;
	@JacksonXmlProperty(localName = "Cooldown", isAttribute = true)
	private int cooldown;
	@JacksonXmlProperty(localName = "SkillType", isAttribute = true)
	private String skillType;
	@JacksonXmlProperty(localName = "TargetType", isAttribute = true)
	private String targetType;
	@JacksonXmlProperty(localName = "Affects", isAttribute = true)
	private String affects;
	@JacksonXmlProperty(localName = "MaxLVofSkill", isAttribute = true)
	private int maxLVofSkill;
	@JacksonXmlProperty(localName = "Damage", isAttribute = true)
	private int damage;
	@JacksonXmlProperty(localName = "LvUpDamage", isAttribute = true)
	private int lvUpDamage;
	@JacksonXmlProperty(localName = "Durations", isAttribute = true)
	private int durations;
	@JacksonXmlProperty(localName = "LvUpValue", isAttribute = true)
	private float lvUpValue;
	@JacksonXmlProperty(localName = "EffectIndex", isAttribute = true)
	private int effectIndex;
	@JacksonXmlProperty(localName = "EffectValue", isAttribute = true)
	private int effectValue;
	@JacksonXmlProperty(localName = "BattlePower", isAttribute = true)
	private int battlePower;
	@JacksonXmlProperty(localName = "LvUpBattlePower", isAttribute = true)
	private int lvUpBattlePower;
	@JacksonXmlProperty(localName = "ValueGrowth", isAttribute = true)
	private String valueGrowth;
	@JacksonXmlProperty(localName = "StatusImage", isAttribute = true)
	private String statusImage;
	@JacksonXmlProperty(localName = "CastSound", isAttribute = true)
	private String castSound;
	@JacksonXmlProperty(localName = "EffectSound", isAttribute = true)
	private String effectSound;
	@JacksonXmlProperty(localName = "HitSound", isAttribute = true)
	private String hitSound;
	@JacksonXmlProperty(localName = "SkillRank", isAttribute = true)
	private String skillRank;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public int getManaCost() {
		return manaCost;
	}


	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}


	public int getCooldown() {
		return cooldown;
	}


	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}


	public String getSkillType() {
		return skillType;
	}


	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}


	public String getTargetType() {
		return targetType;
	}


	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}


	public String getAffects() {
		return affects;
	}


	public void setAffects(String affects) {
		this.affects = affects;
	}


	public int getLvUpDamage() {
		return lvUpDamage;
	}


	public void setLvUpDamage(int lvUpDamage) {
		this.lvUpDamage = lvUpDamage;
	}


	public float getLvUpValue() {
		return lvUpValue;
	}


	public void setLvUpValue(float lvUpValue) {
		this.lvUpValue = lvUpValue;
	}


	public int getEffectIndex() {
		return effectIndex;
	}


	public void setEffectIndex(int effectIndex) {
		this.effectIndex = effectIndex;
	}


	public int getEffectValue() {
		return effectValue;
	}


	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}


	public String getValueGrowth() {
		return valueGrowth;
	}


	public void setValueGrowth(String valueGrowth) {
		this.valueGrowth = valueGrowth;
	}


	public int getDurations() {
		return durations;
	}


	public void setDurations(int durations) {
		this.durations = durations;
	}


	public int getBattlePower() {
		return battlePower;
	}


	public void setBattlePower(int battlePower) {
		this.battlePower = battlePower;
	}


	public int getLvUpBattlePower() {
		return lvUpBattlePower;
	}


	public void setLvUpBattlePower(int lvUpBattlePower) {
		this.lvUpBattlePower = lvUpBattlePower;
	}


	public String getStatusImage() {
		return statusImage;
	}


	public void setStatusImage(String statusImage) {
		this.statusImage = statusImage;
	}


	public String getCastSound() {
		return castSound;
	}


	public void setCastSound(String castSound) {
		this.castSound = castSound;
	}


	public String getEffectSound() {
		return effectSound;
	}


	public void setEffectSound(String effectSound) {
		this.effectSound = effectSound;
	}


	public String getHitSound() {
		return hitSound;
	}


	public void setHitSound(String hitSound) {
		this.hitSound = hitSound;
	}


	public String getSkillRank() {
		return skillRank;
	}


	public void setSkillRank(String skillRank) {
		this.skillRank = skillRank;
	}


	public int getMaxLVofSkill() {
		return maxLVofSkill;
	}


	public void setMaxLVofSkill(int maxLVofSkill) {
		this.maxLVofSkill = maxLVofSkill;
	}


	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}

}
