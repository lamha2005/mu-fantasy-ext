package com.creants.muext.entities.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.entities.ItemBase;
import com.creants.muext.entities.item.ConsumeableItemBase;
import com.creants.muext.entities.item.EquipmentBase;
import com.creants.muext.entities.item.HeroItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = { "rewards", "bonus", "rewardItems", "firstTimeRewardString", "randomBonusString" })
public class Stage implements SerializableQAntType {
	@JacksonXmlProperty(localName = "StageIndex", isAttribute = true)
	public int index;

	@JacksonXmlProperty(localName = "ChapterIndex", isAttribute = true)
	public int chapterIndex;

	@JacksonXmlProperty(localName = "ChapterName", isAttribute = true)
	public transient String chapterName;

	@JacksonXmlProperty(localName = "StageName", isAttribute = true)
	public transient String name;

	@JacksonXmlProperty(localName = "ChapterBG", isAttribute = true)
	public transient String chapterBG;

	@JacksonXmlProperty(localName = "BattleBG", isAttribute = true)
	public transient String battleBG;

	@JacksonXmlProperty(localName = "Formation", isAttribute = true)
	public transient String formation;

	@JacksonXmlProperty(localName = "StaminaCost", isAttribute = true)
	public transient Integer staminaCost;

	@JacksonXmlProperty(localName = "Expansion", isAttribute = true)
	public transient boolean expansion;

	@JacksonXmlProperty(localName = "MonsterIndex", isAttribute = true)
	public transient String monsterIndex;

	@JacksonXmlProperty(localName = "FirstTimeReward", isAttribute = true)
	public transient String firstTimeRewardString;

	@JacksonXmlProperty(localName = "RandomBonus", isAttribute = true)
	public transient String randomBonusString;

	@JacksonXmlProperty(localName = "ZenReward", isAttribute = true)
	public transient Integer zenReward;

	@JacksonXmlProperty(localName = "SweepTimes", isAttribute = true)
	public int sweepTimes;

	@JacksonXmlProperty(localName = "ExpReward", isAttribute = true)
	public transient int expReward;

	@JacksonXmlProperty(localName = "ExpAccReward", isAttribute = true)
	public transient int expAccReward;

	private transient Map<Integer, Integer> monsterCountMap;
	private transient List<Integer[]> roundList;
	private transient List<String> firstTimeReward;
	private transient List<String> randomBonus;

	private List<HeroItem> rewards;
	private List<HeroItem> bonus;


	public void init() {
		monsterCountMap = new HashMap<>();
		roundList = new ArrayList<>();
		readMonsterIndex();
		rewards = splitItem(firstTimeRewardString);
		bonus = splitItem(randomBonusString);

		firstTimeReward = splitRewardString(firstTimeRewardString);
		randomBonus = splitRewardString(randomBonusString);
	}


	private List<String> splitRewardString(String rewardString) {
		List<String> rewardList = new ArrayList<>();
		if (StringUtils.isNotBlank(rewardString)) {
			String[] split = StringUtils.split(rewardString, "#");
			for (int i = 0; i < split.length; i++) {
				rewardList.add(split[i]);
			}
		}

		return rewardList;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public boolean isExpansion() {
		return expansion;
	}


	public void setExpansion(boolean expansion) {
		this.expansion = expansion;
	}


	public int getSweepTimes() {
		return sweepTimes;
	}


	public void setSweepTimes(int sweepTimes) {
		this.sweepTimes = sweepTimes;
	}


	public int getChapterIndex() {
		return chapterIndex;
	}


	public void setChapterIndex(int chapterIndex) {
		this.chapterIndex = chapterIndex;
	}


	public String getChapterName() {
		return chapterName;
	}


	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getChapterBG() {
		return chapterBG;
	}


	public void setChapterBG(String chapterBG) {
		this.chapterBG = chapterBG;
	}


	public String getBattleBG() {
		return battleBG;
	}


	public void setBattleBG(String battleBG) {
		this.battleBG = battleBG;
	}


	public String getFormation() {
		return formation;
	}


	public void setFormation(String formation) {
		this.formation = formation;
	}


	public int getStaminaCost() {
		return staminaCost;
	}


	public void setStaminaCost(int staminaCost) {
		this.staminaCost = staminaCost;
	}


	public String getMonsterIndex() {
		return monsterIndex;
	}


	public void setMonsterIndex(String monsterIndex) {
		this.monsterIndex = monsterIndex;
	}


	public String getFirstTimeRewardString() {
		return firstTimeRewardString;
	}


	public int getZenReward() {
		return zenReward;
	}


	public void setZenReward(int zenReward) {
		this.zenReward = zenReward;
	}


	public String getRandomBonusString() {
		return randomBonusString;
	}


	public int getExpReward() {
		return expReward;
	}


	public int getExpAccReward() {
		return expAccReward;
	}


	private void readMonsterIndex() {
		String[] rounds = StringUtils.split(monsterIndex, "#");

		List<Integer[]> roundList = new ArrayList<>(rounds.length);
		for (int i = 0; i < rounds.length; i++) {
			String[] monsters = StringUtils.split(rounds[i], ",");
			Integer[] monsterIndexArr = new Integer[monsters.length];
			for (int j = 0; j < monsterIndexArr.length; j++) {
				int monsterIndex = Integer.parseInt(monsters[j]);
				monsterIndexArr[j] = Integer.parseInt(monsters[j]);

				Integer count = monsterCountMap.get(monsterIndex);
				if (count == null) {
					count = 1;
				} else {
					count++;
				}
				monsterCountMap.put(monsterIndex, count);
			}

			roundList.add(monsterIndexArr);
		}

		this.roundList = roundList;
	}


	public List<Integer[]> getRoundList() {
		return roundList;
	}


	public Set<Integer> getMonsters() {
		return monsterCountMap.keySet();
	}


	public Integer countMonster(int monsterIndex) {
		return monsterCountMap.get(monsterIndex);
	}


	public List<HeroItem> getRewards() {
		return rewards;
	}


	public List<HeroItem> getBonus() {
		return bonus;
	}


	public List<String> getFirstTimeReward() {
		return firstTimeReward;
	}


	public List<String> getRandomBonus() {
		return randomBonus;
	}


	private List<HeroItem> splitItem(String itemArrString) {
		List<int[]> itemsReward = new ArrayList<>();
		if (StringUtils.isNotBlank(itemArrString)) {
			String[] items = StringUtils.split(itemArrString, "#");
			for (int i = 0; i < items.length; i++) {
				String[] split = StringUtils.split(items[i], "/");
				itemsReward.add(new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]) });
			}
		}
		return convertToItem(itemsReward);
	}


	private List<HeroItem> convertToItem(List<int[]> items) {
		List<HeroItem> itemList = new ArrayList<>();
		if (items.size() > 0) {
			for (int[] ir : items) {
				ItemBase itemBase = ItemConfig.getInstance().getItem(ir[0]);
				HeroItem item = new HeroItem();
				item.setNo(ir[1]);
				item.setIndex(itemBase.getIndex());
				item.setItemGroup(itemBase.getGroupId());
				item.setItemBase(itemBase);
				if (itemBase instanceof ConsumeableItemBase) {
					ConsumeableItemBase consumeableItem = (ConsumeableItemBase) itemBase;
					item.setOverlap(true);
					item.setElement(consumeableItem.getElemental());
				} else if (itemBase instanceof EquipmentBase) {
					EquipmentBase equipmentBase = (EquipmentBase) itemBase;
					item.setElement(equipmentBase.getElemental());
				}
				itemList.add(item);
			}
		}
		return itemList;
	}

}
