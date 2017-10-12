package com.creants.muext.entities.skill;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "durationsString", "effectIndexString", "effectValueString" })
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
	@JacksonXmlProperty(localName = "SkillLevel", isAttribute = true)
	private int skillLevel;
	@JacksonXmlProperty(localName = "UpgradeTo", isAttribute = true)
	private int upgradeTo;
	@JacksonXmlProperty(localName = "TargetType", isAttribute = true)
	private String targetType;
	@JacksonXmlProperty(localName = "Affects", isAttribute = true)
	private String affects;
	@JacksonXmlProperty(localName = "Damage", isAttribute = true)
	private int damage;
	@JacksonXmlProperty(localName = "Durations", isAttribute = true)
	private String durationsString;
	@JacksonXmlProperty(localName = "EffectIndex", isAttribute = true)
	private String effectIndexString;
	@JacksonXmlProperty(localName = "EffectValue", isAttribute = true)
	private String effectValueString;
	@JacksonXmlProperty(localName = "BattlePower", isAttribute = true)
	private int battlePower;
	@JacksonXmlProperty(localName = "StatusImage", isAttribute = true)
	private String statusImage;
	@JacksonXmlProperty(localName = "CastSound", isAttribute = true)
	private String castSound;
	@JacksonXmlProperty(localName = "EffectSound", isAttribute = true)
	private String effectSound;
	@JacksonXmlProperty(localName = "HitSound", isAttribute = true)
	private String hitSound;
	@JacksonXmlProperty(localName = "Icon", isAttribute = true)
	private String icon;

	private int[] effectIndex;
	private float[] effectValue;
	private int[] durations;


	public void convertBaseInfo() {
		effectIndex = convertToIntArray(effectIndexString);
		durations = convertToIntArray(durationsString);
		effectValue = convertToFloatArray(effectValueString);

	}


	// TODO move to util
	private int[] convertToIntArray(String value) {
		String[] items = StringUtils.split(value, "#");
		int[] result = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			result[i] = Integer.parseInt(items[i]);
		}
		return result;
	}


	private float[] convertToFloatArray(String value) {
		String[] items = StringUtils.split(value, "#");
		float[] result = new float[items.length];
		for (int i = 0; i < items.length; i++) {
			result[i] = Float.parseFloat(items[i]);
		}
		return result;
	}


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


	public int getBattlePower() {
		return battlePower;
	}


	public void setBattlePower(int battlePower) {
		this.battlePower = battlePower;
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


	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}


	public int getSkillLevel() {
		return skillLevel;
	}


	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}


	public int getUpgradeTo() {
		return upgradeTo;
	}


	public void setUpgradeTo(int upgradeTo) {
		this.upgradeTo = upgradeTo;
	}


	public String getEffectIndexString() {
		return effectIndexString;
	}


	public void setEffectIndexString(String effectIndexString) {
		this.effectIndexString = effectIndexString;
	}


	public int[] getEffectIndex() {
		return effectIndex;
	}


	public void setEffectIndex(int[] effectIndex) {
		this.effectIndex = effectIndex;
	}


	public String getDurationsString() {
		return durationsString;
	}


	public void setDurationsString(String durationsString) {
		this.durationsString = durationsString;
	}


	public String getEffectValueString() {
		return effectValueString;
	}


	public void setEffectValueString(String effectValueString) {
		this.effectValueString = effectValueString;
	}


	public float[] getEffectValue() {
		return effectValue;
	}


	public void setEffectValue(float[] effectValue) {
		this.effectValue = effectValue;
	}


	public int[] getDurations() {
		return durations;
	}


	public void setDurations(int[] durations) {
		this.durations = durations;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}

}
