package com.creants.muext.entities.states;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Những chỉ số chính của nhân vật
 * 
 * @author LamHM
 * 
 */
public class BaseStats implements SerializableQAntType {
	@JacksonXmlProperty(localName = "PhysicAttack", isAttribute = true)
	private int atk;
	@JacksonXmlProperty(localName = "HealthPoint", isAttribute = true)
	private int hp;
	@JacksonXmlProperty(localName = "Defense", isAttribute = true)
	private int def;
	@JacksonXmlProperty(localName = "Recovery", isAttribute = true)
	private int rec;


	public int getAtk() {
		return atk;
	}


	public void setAtk(int atk) {
		this.atk = atk;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public int getDef() {
		return def;
	}


	public void setDef(int def) {
		this.def = def;
	}


	public int getRec() {
		return rec;
	}


	public void setRec(int rec) {
		this.rec = rec;
	}


	@Override
	public String toString() {
		return "[BaseStats] {PhysicAttack:" + atk + ", HealthPoint:" + hp + ", Defense:" + def + ", Recovery:" + rec
				+ "}";
	}

}
