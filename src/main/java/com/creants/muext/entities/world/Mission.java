package com.creants.muext.entities.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class Mission implements SerializableQAntType {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	public int index;
	@JacksonXmlProperty(localName = "StageIndex", isAttribute = true)
	public int stageIndex;

	public int maxFarm;

	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	public transient String name;
	@JacksonXmlProperty(localName = "StaminaCost", isAttribute = true)
	public transient int staminaCost;
	public boolean clear;
	public Integer starNo;
	@JacksonXmlProperty(localName = "MonsterIndex", isAttribute = true)
	public transient String roundString;

	@JacksonXmlProperty(localName = "FirstTimeReward", isAttribute = true)
	public int reward1Index;
	@JacksonXmlProperty(localName = "ZenReward", isAttribute = true)
	public int reward2Index;


	public Mission() {
		maxFarm = 3;
	}


	public int getMaxFarm() {
		return maxFarm;
	}


	public void setMaxFarm(int maxFarm) {
		this.maxFarm = maxFarm;
	}


	public int getStageIndex() {
		return stageIndex;
	}


	public void setStageIndex(int stageIndex) {
		this.stageIndex = stageIndex;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getStaminaCost() {
		return staminaCost;
	}


	public void setStaminaCost(int staminaCost) {
		this.staminaCost = staminaCost;
	}


	public boolean isClear() {
		return clear;
	}


	public void setClear(boolean clear) {
		this.clear = clear;
	}


	public Integer getStarNo() {
		return starNo;
	}


	public void setStarNo(Integer starNo) {
		this.starNo = starNo;
	}


	public String getRoundString() {
		return roundString;
	}


	public void setRoundString(String roundString) {
		this.roundString = roundString;
	}


	public List<Integer[]> getRoundList() {
		String[] rounds = StringUtils.split(roundString, "#");

		List<Integer[]> roundList = new ArrayList<>(rounds.length);
		for (int i = 0; i < rounds.length; i++) {
			String[] monsters = StringUtils.split(rounds[i], ",");
			Integer[] monsterIndex = new Integer[monsters.length];
			for (int j = 0; j < monsterIndex.length; j++) {
				monsterIndex[j] = Integer.parseInt(monsters[j]);
			}
			roundList.add(monsterIndex);
		}

		return roundList;
	}
}
