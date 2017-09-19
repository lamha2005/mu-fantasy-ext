package com.creants.muext.entities.xml;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonIgnoreProperties({ "animationsString", "spineHitString", "hitTimingString", "spineSkillHitString",
		"skillTimingString" })
public class Asset {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "ResChar", isAttribute = true)
	private String resChar;

	@JacksonXmlProperty(localName = "Animations", isAttribute = true)
	private String animationsString;
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

	private String[] animations;
	private String[] spineHit;
	private float[] hitTiming;
	private String[] spineSkillHit;
	private float[] skillTiming;


	public void convertBase() {
		String[] animaArr = StringUtils.split(animationsString, "#");
		animations = new String[animaArr.length];
		for (int i = 0; i < animaArr.length; i++) {
			animations[i] = animaArr[i];
		}

		String[] spineHitArr = StringUtils.split(spineHitString, "#");
		spineHit = new String[spineHitArr.length];
		for (int i = 0; i < spineHitArr.length; i++) {
			spineHit[i] = spineHitArr[i];
		}

		if (StringUtils.isNotBlank(spineSkillHitString)) {
			String[] spineSkillHitArr = StringUtils.split(spineSkillHitString, "#");
			spineSkillHit = new String[spineSkillHitArr.length];
			for (int i = 0; i < spineSkillHitArr.length; i++) {
				spineSkillHit[i] = spineSkillHitArr[i];
			}
		}

		String[] hitTimingArr = StringUtils.split(hitTimingString, "#");
		hitTiming = new float[hitTimingArr.length];
		for (int i = 0; i < hitTimingArr.length; i++) {
			hitTiming[i] = Float.parseFloat(hitTimingArr[i]);
		}

		if (StringUtils.isNotBlank(skillTimingString)) {
			String[] skillTimingArr = StringUtils.split(skillTimingString, "#");
			skillTiming = new float[skillTimingArr.length];
			for (int i = 0; i < skillTimingArr.length; i++) {
				skillTiming[i] = Float.parseFloat(skillTimingArr[i]);
			}
		}
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


	public String getAnimationsString() {
		return animationsString;
	}


	public void setAnimationsString(String animationsString) {
		this.animationsString = animationsString;
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


	public String[] getAnimations() {
		return animations;
	}


	public void setAnimations(String[] animations) {
		this.animations = animations;
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

}
