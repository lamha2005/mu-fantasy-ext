package com.creants.muext.entities.states;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Những chỉ số phụ của nhân vật
 * 
 * @author LamHM
 *
 * 
 */
public class SubStats implements SerializableQAntType {
	@JacksonXmlProperty(localName = "Accuracy", isAttribute = true)
	private float acc;
	@JacksonXmlProperty(localName = "CriticalChance", isAttribute = true)
	private float critch;
	@JacksonXmlProperty(localName = "DefenseensePenetration", isAttribute = true)
	private int defpe;
	@JacksonXmlProperty(localName = "DefenseenseIgnore", isAttribute = true)
	private float defig;
	@JacksonXmlProperty(localName = "MagicResistanceistancePenetration", isAttribute = true)
	private int respen;
	@JacksonXmlProperty(localName = "MagicResistanceistanceIgnore", isAttribute = true)
	private float resign;
	@JacksonXmlProperty(localName = "ManaBreak", isAttribute = true)
	private int manabrk;
	@JacksonXmlProperty(localName = "LifeSteal", isAttribute = true)
	private float lifestl;
	@JacksonXmlProperty(localName = "HealthRegeneration", isAttribute = true)
	private int hpreg;
	@JacksonXmlProperty(localName = "Manaregeneration", isAttribute = true)
	private int mpreg;


	public int getDefpe() {
		return defpe;
	}


	public void setDefpe(int defpe) {
		this.defpe = defpe;
	}


	public int getRespen() {
		return respen;
	}


	public void setRespen(int respen) {
		this.respen = respen;
	}


	public int getManabrk() {
		return manabrk;
	}


	public void setManabrk(int manabrk) {
		this.manabrk = manabrk;
	}


	public int getHpreg() {
		return hpreg;
	}


	public void setHpreg(int hpreg) {
		this.hpreg = hpreg;
	}


	public int getMpreg() {
		return mpreg;
	}


	public void setMpreg(int mpreg) {
		this.mpreg = mpreg;
	}


	public float getAcc() {
		return acc;
	}


	public void setAcc(float acc) {
		this.acc = acc;
	}


	public float getCritch() {
		return critch;
	}


	public void setCritch(float critch) {
		this.critch = critch;
	}


	public float getResign() {
		return resign;
	}


	public void setResign(float resign) {
		this.resign = resign;
	}


	public float getLifestl() {
		return lifestl;
	}


	public void setLifestl(float lifestl) {
		this.lifestl = lifestl;
	}


	public float getDefig() {
		return defig;
	}


	public void setDefig(float defig) {
		this.defig = defig;
	}

}
