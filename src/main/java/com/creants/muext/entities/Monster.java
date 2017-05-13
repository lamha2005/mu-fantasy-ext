package com.creants.muext.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Monster {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;

	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	public String name;
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	public int level;

	@JacksonXmlProperty(localName = "Image", isAttribute = true)
	private String img;
	@JacksonXmlProperty(localName = "MonsterX2DamageChance", isAttribute = true)
	private float damX2Change;

	@JacksonXmlProperty(localName = "Dam", isAttribute = true)
	private int dam;
	@JacksonXmlProperty(localName = "HealthPoint", isAttribute = true)
	private int hp;
	@JacksonXmlProperty(localName = "Defense", isAttribute = true)
	private int def;

	@JacksonXmlProperty(localName = "MagicResistance", isAttribute = true)
	private int res;
	@JacksonXmlProperty(localName = "Speed", isAttribute = true)
	private int spd;


	public int getIndex() {
		return index;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public float getDamX2Change() {
		return damX2Change;
	}


	public void setDamX2Change(float damX2Change) {
		this.damX2Change = damX2Change;
	}


	public int getDam() {
		return dam;
	}


	public int getHp() {
		return hp;
	}


	public int getDef() {
		return def;
	}


	public int getRes() {
		return res;
	}


	public int getSpd() {
		return spd;
	}

}
