package com.creants.muext.entities.upgrade;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class LevelUpHeroByHero {
	@JacksonXmlProperty(localName = "Materials", isAttribute = true)
	private int rank;
	@JacksonXmlProperty(localName = "SameElementals", isAttribute = true)
	private int sameElementals;
	@JacksonXmlProperty(localName = "DifferentElementals", isAttribute = true)
	private int differentElementals;
	@JacksonXmlProperty(localName = "SuccessRatePercent", isAttribute = true)
	private int successRatePercent;


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public int getSameElementals() {
		return sameElementals;
	}


	public void setSameElementals(int sameElementals) {
		this.sameElementals = sameElementals;
	}


	public int getDifferentElementals() {
		return differentElementals;
	}


	public void setDifferentElementals(int differentElementals) {
		this.differentElementals = differentElementals;
	}


	public int getSuccessRatePercent() {
		return successRatePercent;
	}


	public void setSuccessRatePercent(int successRatePercent) {
		this.successRatePercent = successRatePercent;
	}

}
