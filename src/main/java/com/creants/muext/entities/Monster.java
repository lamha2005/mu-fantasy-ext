package com.creants.muext.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Monster extends Character {
	@JacksonXmlProperty(localName = "Index")
	private int id;
	@JacksonXmlProperty(localName = "Image")
	private String img;
	@JacksonXmlProperty(localName = "MonsterX2DamageChance")
	private float damX2Change;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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

}
