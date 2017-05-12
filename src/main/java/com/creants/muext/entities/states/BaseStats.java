package com.creants.muext.entities.states;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Những chỉ số chính của nhân vật
 * 
 * @author LamHM
 * 
 */
public class BaseStats implements SerializableQAntType{
	@JacksonXmlProperty(localName = "PhysicAttack")
	private int atk;
	@JacksonXmlProperty(localName = "MagicPower")
	private int mag;
	@JacksonXmlProperty(localName = "HealthPoint")
	private int hp;
	@JacksonXmlProperty(localName = "ManaPoint")
	private int mp;
	@JacksonXmlProperty(localName = "Defense")
	private int def;
	@JacksonXmlProperty(localName = "MagicResistance")
	private int res;
	@JacksonXmlProperty(localName = "Speed")
	private int spd;


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


	public int getMp() {
		return mp;
	}


	public void setMp(int mp) {
		this.mp = mp;
	}


	public int getDef() {
		return def;
	}


	public void setDef(int def) {
		this.def = def;
	}


	public int getRes() {
		return res;
	}


	public void setRes(int res) {
		this.res = res;
	}


	public int getSpd() {
		return spd;
	}


	public void setSpd(int spd) {
		this.spd = spd;
	}


	@Override
	public String toString() {
		return "[BaseStats] {PhysicAttack:" + atk + ", MagicPower:" + mag + ", HealthPoint:" + hp + ", ManaPoint:" + mp
				+ ", Defense:" + def + ", MagicResistance:" + res + "}";
	}

}
