package com.creants.muext.entities.chaos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.creants.muext.config.ItemConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonIgnoreProperties({ "chestRewardString", "battlePowserArr", "bonusZenS", "bonusZenA", "bonusZenB", "bonusZenC",
		"bonusZenD" })
public class ChaosStageBase {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "BattlePower", isAttribute = true)
	private String battlePower;
	@JacksonXmlProperty(localName = "ZenReward", isAttribute = true)
	private int zenReward;
	@JacksonXmlProperty(localName = "ChaosPointReward", isAttribute = true)
	private int chaosPointReward;
	@JacksonXmlProperty(localName = "ChestReward", isAttribute = true)
	private String chestRewardString;
	@JacksonXmlProperty(localName = "BonusZenS", isAttribute = true)
	private int bonusZenS;
	@JacksonXmlProperty(localName = "BonusZenA", isAttribute = true)
	private int bonusZenA;
	@JacksonXmlProperty(localName = "BonusZenB", isAttribute = true)
	private int bonusZenB;
	@JacksonXmlProperty(localName = "BonusZenC", isAttribute = true)
	private int bonusZenC;
	@JacksonXmlProperty(localName = "BonusZenD", isAttribute = true)
	private int bonusZenD;

	private List<String> chestReward;
	private int[] battlePowserArr;
	private Map<String, Integer> bonusZenMap;


	public void initBase() {
		String[] split = StringUtils.split(battlePower, "-");
		battlePowserArr = new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]) };
		String[] items = StringUtils.split(chestRewardString, "#");
		chestReward = new ArrayList<>(Arrays.asList(items));
		bonusZenMap = new HashMap<>();
		bonusZenMap.put("S", bonusZenS);
		bonusZenMap.put("A", bonusZenA);
		bonusZenMap.put("B", bonusZenB);
		bonusZenMap.put("C", bonusZenC);
		bonusZenMap.put("D", bonusZenD);

	}


	public String getFullReward(String rank) {
		return ItemConfig.getInstance().combineToItemString(chestRewardString, zenReward + bonusZenMap.get(rank),
				chaosPointReward);
	}


	public int getIndex() {
		return index;
	}


	public String getBattlePower() {
		return battlePower;
	}


	public int getZenReward() {
		return zenReward;
	}


	public int getChaosPointReward() {
		return chaosPointReward;
	}


	public String getChestRewardString() {
		return chestRewardString;
	}


	public int getBonusZenS() {
		return bonusZenS;
	}


	public int getBonusZenA() {
		return bonusZenA;
	}


	public int getBonusZenB() {
		return bonusZenB;
	}


	public int getBonusZenC() {
		return bonusZenC;
	}


	public int getBonusZenD() {
		return bonusZenD;
	}


	public List<String> getChestReward() {
		return chestReward;
	}


	public int[] getBattlePowserArr() {
		return battlePowserArr;
	}


	public Map<String, Integer> getBonusZenMap() {
		return bonusZenMap;
	}

}
