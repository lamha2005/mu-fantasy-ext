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
	private int skillType;
	@JacksonXmlProperty(localName = "TargetType", isAttribute = true)
	private int targetType;
	@JacksonXmlProperty(localName = "Affects", isAttribute = true)
	private int affects;
	@JacksonXmlProperty(localName = "EffectIndex", isAttribute = true)
	private String effectIndex;
	@JacksonXmlProperty(localName = "EffectValue", isAttribute = true)
	private String effectValue;
	@JacksonXmlProperty(localName = "ValueGrowth", isAttribute = true)
	private String valueGrowth;
	@JacksonXmlProperty(localName = "CastImage", isAttribute = true)
	private String castImage;
	@JacksonXmlProperty(localName = "EffectImage", isAttribute = true)
	private String effectImage;
	@JacksonXmlProperty(localName = "HitImage", isAttribute = true)
	private String hitImage;
	@JacksonXmlProperty(localName = "StatusImage", isAttribute = true)
	private String statusImage;
	@JacksonXmlProperty(localName = "CastSound", isAttribute = true)
	private String castSound;
	@JacksonXmlProperty(localName = "EffectSound", isAttribute = true)
	private String effectSound;
	@JacksonXmlProperty(localName = "HitSound", isAttribute = true)
	private String hitSound;


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


	public int getSkillType() {
		return skillType;
	}


	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}


	public int getTargetType() {
		return targetType;
	}


	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}


	public int getAffects() {
		return affects;
	}


	public void setAffects(int affects) {
		this.affects = affects;
	}


	public String getEffectIndex() {
		return effectIndex;
	}


	public void setEffectIndex(String effectIndex) {
		this.effectIndex = effectIndex;
	}


	public String getEffectValue() {
		return effectValue;
	}


	public void setEffectValue(String effectValue) {
		this.effectValue = effectValue;
	}


	public String getValueGrowth() {
		return valueGrowth;
	}


	public void setValueGrowth(String valueGrowth) {
		this.valueGrowth = valueGrowth;
	}


	public String getCastImage() {
		return castImage;
	}


	public void setCastImage(String castImage) {
		this.castImage = castImage;
	}


	public String getEffectImage() {
		return effectImage;
	}


	public void setEffectImage(String effectImage) {
		this.effectImage = effectImage;
	}


	public String getHitImage() {
		return hitImage;
	}


	public void setHitImage(String hitImage) {
		this.hitImage = hitImage;
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

}
