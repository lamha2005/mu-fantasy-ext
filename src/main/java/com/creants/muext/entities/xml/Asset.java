package com.creants.muext.entities.xml;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonIgnoreProperties({ "spineHitString", "hitTimingString", "spineSkillHitString", "soundString", "soundTimingString",
		"soundSkillString", "soundTimingSkillString", "skillTimingString" })
public class Asset {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "ResChar", isAttribute = true)
	private String resChar;
	@JacksonXmlProperty(localName = "Animations", isAttribute = true)
	private String animations;
	@JacksonXmlProperty(localName = "SpineHit", isAttribute = true)
	private String spineHitString;
	@JacksonXmlProperty(localName = "HitTiming", isAttribute = true)
	private String hitTimingString;
	@JacksonXmlProperty(localName = "SpineSkill", isAttribute = true)
	private int spineSkill;
	@JacksonXmlProperty(localName = "SpineSkillHit", isAttribute = true)
	private String spineSkillHitString;
	@JacksonXmlProperty(localName = "SkillTiming", isAttribute = true)
	private String skillTimingString;

	@JacksonXmlProperty(localName = "Sound", isAttribute = true)
	private String soundString;
	@JacksonXmlProperty(localName = "SoundTiming", isAttribute = true)
	private String soundTimingString;
	@JacksonXmlProperty(localName = "SoundHurt", isAttribute = true)
	private String soundHurt;
	@JacksonXmlProperty(localName = "SkillAnimations", isAttribute = true)
	private String skillAnimations;
	@JacksonXmlProperty(localName = "SoundSkill", isAttribute = true)
	private String soundSkillString;
	@JacksonXmlProperty(localName = "SoundTimingSkill", isAttribute = true)
	private String soundTimingSkillString;

	private String[] spineHit;
	private float[] hitTiming;
	private String[] spineSkillHit;
	private float[] skillTiming;

	private String[] sound;
	private float[] soundTiming;
	private String[] soundSkill;
	private float[] soundTimingSkill;


	public void convertBase() {
		spineHit = convertToStringArr(spineHitString);
		sound = convertToStringArr(soundString);
		spineSkillHit = convertToStringArr(spineSkillHitString);
		soundSkill = convertToStringArr(soundSkillString);
		hitTiming = convertToFloatArr(hitTimingString);
		skillTiming = convertToFloatArr(skillTimingString);
		soundTiming = convertToFloatArr(soundTimingString);
		soundTimingSkill = convertToFloatArr(soundTimingSkillString);
	}


	private float[] convertToFloatArr(String value) {
		if (StringUtils.isNotBlank(value)) {
			String[] split = StringUtils.split(value, "#");
			float[] result = new float[split.length];
			for (int i = 0; i < split.length; i++) {
				result[i] = Float.parseFloat(split[i]);
			}
			return result;
		}

		return null;
	}


	private String[] convertToStringArr(String value) {
		if (StringUtils.isNotBlank(value))
			return StringUtils.split(value, "#");
		return null;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getResChar() {
		return resChar;
	}


	public void setResChar(String resChar) {
		this.resChar = resChar;
	}


	public String getSpineHitString() {
		return spineHitString;
	}


	public void setSpineHitString(String spineHitString) {
		this.spineHitString = spineHitString;
	}


	public String getHitTimingString() {
		return hitTimingString;
	}


	public void setHitTimingString(String hitTimingString) {
		this.hitTimingString = hitTimingString;
	}


	public int getSpineSkill() {
		return spineSkill;
	}


	public void setSpineSkill(int spineSkill) {
		this.spineSkill = spineSkill;
	}


	public String getSpineSkillHitString() {
		return spineSkillHitString;
	}


	public void setSpineSkillHitString(String spineSkillHitString) {
		this.spineSkillHitString = spineSkillHitString;
	}


	public String[] getSpineHit() {
		return spineHit;
	}


	public void setSpineHit(String[] spineHit) {
		this.spineHit = spineHit;
	}


	public float[] getHitTiming() {
		return hitTiming;
	}


	public void setHitTiming(float[] hitTiming) {
		this.hitTiming = hitTiming;
	}


	public String getSkillTimingString() {
		return skillTimingString;
	}


	public void setSkillTimingString(String skillTimingString) {
		this.skillTimingString = skillTimingString;
	}


	public String[] getSpineSkillHit() {
		return spineSkillHit;
	}


	public void setSpineSkillHit(String[] spineSkillHit) {
		this.spineSkillHit = spineSkillHit;
	}


	public float[] getSkillTiming() {
		return skillTiming;
	}


	public void setSkillTiming(float[] skillTiming) {
		this.skillTiming = skillTiming;
	}


	public String getSoundString() {
		return soundString;
	}


	public String getSoundTimingString() {
		return soundTimingString;
	}


	public String getSoundHurt() {
		return soundHurt;
	}


	public String getSkillAnimations() {
		return skillAnimations;
	}


	public String getSoundSkillString() {
		return soundSkillString;
	}


	public String getSoundTimingSkillString() {
		return soundTimingSkillString;
	}


	public String[] getSound() {
		return sound;
	}


	public float[] getSoundTiming() {
		return soundTiming;
	}


	public String[] getSoundSkill() {
		return soundSkill;
	}


	public float[] getSoundTimingSkill() {
		return soundTimingSkill;
	}

}
