package com.creants.muext.entities.upgrade;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class LevelUpEquipment {
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	private int level;
	@JacksonXmlProperty(localName = "CombineMaterial", isAttribute = true)
	private String combineMaterial;
	@JacksonXmlProperty(localName = "ZenCost", isAttribute = true)
	private int zenCost;
	@JacksonXmlProperty(localName = "SuccessRatePercent", isAttribute = true)
	private int successRatePercent;


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getCombineMaterial() {
		return combineMaterial;
	}


	public void setCombineMaterial(String combineMaterial) {
		this.combineMaterial = combineMaterial;
	}


	public int getZenCost() {
		return zenCost;
	}


	public void setZenCost(int zenCost) {
		this.zenCost = zenCost;
	}


	public int getSuccessRatePercent() {
		return successRatePercent;
	}


	public void setSuccessRatePercent(int successRatePercent) {
		this.successRatePercent = successRatePercent;
	}

}
