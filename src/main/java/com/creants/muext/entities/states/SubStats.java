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


	public int getDefpe() {
		return defpe;
	}


	public void setDefpe(int defpe) {
		this.defpe = defpe;
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

}
