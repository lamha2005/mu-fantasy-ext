package com.creants.muext.entities;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Monster implements SerializableQAntType {
	/**
	 * id của monster trong trận đấu
	 */
	public int id;

	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	public int index;

	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	public String name;
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	public int level;

	@JacksonXmlProperty(localName = "Image", isAttribute = true)
	public String img;
	@JacksonXmlProperty(localName = "MonsterX2DamageChance", isAttribute = true)
	public float damX2Change;

	@JacksonXmlProperty(localName = "Dam", isAttribute = true)
	public int dam;
	@JacksonXmlProperty(localName = "HealthPoint", isAttribute = true)
	public int hp;
	@JacksonXmlProperty(localName = "Defense", isAttribute = true)
	public int def;

	@JacksonXmlProperty(localName = "MagicResistance", isAttribute = true)
	public int res;
	@JacksonXmlProperty(localName = "Speed", isAttribute = true)
	public int spd;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


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


	public int[] genCritRate() {
		
		return null;
	}
	
	public int[] genX2Dam(){
		return null;
	}
}
