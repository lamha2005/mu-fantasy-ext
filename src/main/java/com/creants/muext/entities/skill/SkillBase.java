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
@JsonIgnoreProperties(value = { "lvUpValueString", "effectIndexString", "effectValueString" })
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
	private String lvUpValueString;
	@JacksonXmlProperty(localName = "EffectIndex", isAttribute = true)
	private String effectIndexString;
	@JacksonXmlProperty(localName = "EffectValue", isAttribute = true)
	private String effectValueString;
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

	private float[] lvUpValue;
	private int[] effectIndex;
	private int[] effectValue;


	public void convertBaseInfo() {
		String[] lvUpValueItems = StringUtils.split(lvUpValueString, "#");
		if (lvUpValueItems == null) {
			lvUpValue = new float[] { 0 };
		} else {
			lvUpValue = new float[lvUpValueItems.length];
			for (int i = 0; i < lvUpValueItems.length; i++) {
				lvUpValue[i] = Float.parseFloat(lvUpValueItems[i]);
			}
		}

		String[] effectIndexItems = StringUtils.split(effectIndexString, "#");
		if (effectIndexItems == null) {
			effectIndex = new int[] { 0 };
		} else {
			effectIndex = new int[effectIndexItems.length];
			for (int i = 0; i < effectIndexItems.length; i++) {
				effectIndex[i] = Integer.parseInt(effectIndexItems[i]);
			}
		}

		String[] effectValueItems = StringUtils.split(effectValueString, "#");
		if (effectValueItems == null) {
			effectValue = new int[] { 0 };
		} else {
			effectValue = new int[effectValueItems.length];
			for (int i = 0; i < effectValueItems.length; i++) {
				effectValue[i] = Integer.parseInt(effectValueItems[i]);
			}
		}
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


	public int getLvUpDamage() {
		return lvUpDamage;
	}


	public void setLvUpDamage(int lvUpDamage) {
		this.lvUpDamage = lvUpDamage;
	}


	public String getLvUpValueString() {
		return lvUpValueString;
	}


	public void setLvUpValueString(String lvUpValueString) {
		this.lvUpValueString = lvUpValueString;
	}


	public String getEffectIndexString() {
		return effectIndexString;
	}


	public void setEffectIndexString(String effectIndexString) {
		this.effectIndexString = effectIndexString;
	}


	public String getEffectValueString() {
		return effectValueString;
	}


	public void setEffectValueString(String effectValueString) {
		this.effectValueString = effectValueString;
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


	public float[] getLvUpValue() {
		return lvUpValue;
	}


	public int[] getEffectIndex() {
		return effectIndex;
	}


	public int[] getEffectValue() {
		return effectValue;
	}

}
