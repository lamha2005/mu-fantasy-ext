package com.creants.muext.entities.states;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class AdditionLevelUpStats {
	@JacksonXmlProperty(localName = "LvUpATK")
	private int atk;
	@JacksonXmlProperty(localName = "LvUpMAG")
	private int mag;
	@JacksonXmlProperty(localName = "LvUpHP")
	private int hp;
	@JacksonXmlProperty(localName = "LvUpMP")
	private float mp;
	@JacksonXmlProperty(localName = "LvUpDEF")
	private float def;
	@JacksonXmlProperty(localName = "LvUpRES")
	private float res;
	@JacksonXmlProperty(localName = "LvUpSPD")
	private float spd;
	@JacksonXmlProperty(localName = "LvUpACC")
	private float acc;
	@JacksonXmlProperty(localName = "LvUpCritCH")
	private float critch;
	@JacksonXmlProperty(localName = "LvUpHPReg")
	private float hpreg;
	@JacksonXmlProperty(localName = "LvUpMPReg")
	private float mpreg;


	public int getAtk() {
		return atk;
	}


	public void setAtk(int atk) {
		this.atk = atk;
	}


	public int getMag() {
		return mag;
	}


	public void setMag(int mag) {
		this.mag = mag;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public float getMp() {
		return mp;
	}


	public void setMp(float mp) {
		this.mp = mp;
	}


	public float getDef() {
		return def;
	}


	public void setDef(float def) {
		this.def = def;
	}


	public float getRes() {
		return res;
	}


	public void setRes(float res) {
		this.res = res;
	}


	public float getSpd() {
		return spd;
	}


	public void setSpd(float spd) {
		this.spd = spd;
	}


	public float getCritch() {
		return critch;
	}


	public void setCritch(float critch) {
		this.critch = critch;
	}


	public float getHpreg() {
		return hpreg;
	}


	public void setHpreg(float hpreg) {
		this.hpreg = hpreg;
	}


	public float getMpreg() {
		return mpreg;
	}


	public void setMpreg(float mpreg) {
		this.mpreg = mpreg;
	}

}
