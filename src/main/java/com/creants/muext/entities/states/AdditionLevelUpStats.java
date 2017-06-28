package com.creants.muext.entities.states;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class AdditionLevelUpStats {
	@JacksonXmlProperty(localName = "LvUpATK", isAttribute = true)
	private int atk;
	@JacksonXmlProperty(localName = "LvUpHP", isAttribute = true)
	private int hp;
	@JacksonXmlProperty(localName = "LvUpDEF", isAttribute = true)
	private float def;
	@JacksonXmlProperty(localName = "LvUpREC", isAttribute = true)
	private float rec;


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


	public float getDef() {
		return def;
	}


	public void setDef(float def) {
		this.def = def;
	}

}
