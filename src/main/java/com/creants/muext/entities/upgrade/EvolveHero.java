package com.creants.muext.entities.upgrade;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class EvolveHero {
	@JacksonXmlProperty(localName = "ClassGroup", isAttribute = true)
	private int classGroup;
	@JacksonXmlProperty(localName = "Rank", isAttribute = true)
	private int rank;
	@JacksonXmlProperty(localName = "SpecialMaterial", isAttribute = true)
	private String material;
	@JacksonXmlProperty(localName = "ZenCost", isAttribute = true)
	private int zenCost;
	@JacksonXmlProperty(localName = "SuccessRatePercent", isAttribute = true)
	private int successRate;


	public int getClassGroup() {
		return classGroup;
	}


	public void setClassGroup(int classGroup) {
		this.classGroup = classGroup;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public String getMaterial() {
		return material;
	}


	public void setMaterial(String material) {
		this.material = material;
	}


	public int getZenCost() {
		return zenCost;
	}


	public void setZenCost(int zenCost) {
		this.zenCost = zenCost;
	}


	public int getSuccessRate() {
		return successRate;
	}


	public void setSuccessRate(int successRate) {
		this.successRate = successRate;
	}

}
