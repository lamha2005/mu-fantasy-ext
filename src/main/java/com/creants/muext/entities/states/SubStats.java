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
	@JacksonXmlProperty(localName = "CRIT", isAttribute = true)
	private float critch;
	@JacksonXmlProperty(localName = "MaxMP", isAttribute = true)
	private int maxMp;
	@JacksonXmlProperty(localName = "MPRec", isAttribute = true)
	private int mpRec;


	public float getCritch() {
		return critch;
	}


	public void setCritch(float critch) {
		this.critch = critch;
	}


	public int getMaxMp() {
		return maxMp;
	}


	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}


	public int getMpRec() {
		return mpRec;
	}


	public void setMpRec(int mpRec) {
		this.mpRec = mpRec;
	}

}
