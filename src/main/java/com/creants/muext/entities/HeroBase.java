package com.creants.muext.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class HeroBase extends Character {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "Ranger", isAttribute = true)
	private boolean ranger;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public boolean isRanger() {
		return ranger;
	}


	public void setRanger(boolean ranger) {
		this.ranger = ranger;
	}

}
