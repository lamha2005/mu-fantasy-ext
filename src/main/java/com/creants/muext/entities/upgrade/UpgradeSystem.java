package com.creants.muext.entities.upgrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonIgnoreProperties({ "evolveHeroList", "levelUpHeroByHeroList", "levelUpHeroByItemList" })
public class UpgradeSystem {
	@JacksonXmlProperty(localName = "EvolveHeroes")
	private List<EvolveHero> evolveHeroList;
	@JacksonXmlProperty(localName = "LevelUpHeroByHeroes")
	private List<LevelUpHeroByHero> levelUpHeroByHeroList;
	@JacksonXmlProperty(localName = "LevelUpHeroByItems")
	private List<LevelUpHeroByItem> levelUpHeroByItemList;

	@JacksonXmlProperty(localName = "LevelUpEquipmentsLT3")
	private List<LevelUpEquipment> levelUpEquipmentLT3List;
	@JacksonXmlProperty(localName = "LevelUpEquipmentsGT3")
	private List<LevelUpEquipment> levelUpEquipmentGT3List;

	private Map<String, EvolveHero> evolveHeroes;
	private Map<Integer, LevelUpHeroByHero> levelUpHeroByHeroes;
	private Map<Integer, LevelUpHeroByItem> levelUpHeroByItems;
	private Map<Integer, LevelUpEquipment> levelUpEquipmentsLT3;
	private Map<Integer, LevelUpEquipment> levelUpEquipmentsGT3;


	public void convertBase() {
		evolveHeroes = new HashMap<>();
		for (EvolveHero evolveHero : evolveHeroList) {
			evolveHeroes.put(evolveHero.getClassGroup() + "/" + evolveHero.getRank(), evolveHero);
		}

		levelUpHeroByHeroes = new HashMap<>();
		for (LevelUpHeroByHero levelUpHero : levelUpHeroByHeroList) {
			levelUpHeroByHeroes.put(levelUpHero.getRank(), levelUpHero);
		}

		levelUpHeroByItems = new HashMap<>();
		for (LevelUpHeroByItem levelUpItem : levelUpHeroByItemList) {
			levelUpHeroByItems.put(levelUpItem.getItemIndex(), levelUpItem);
		}

		levelUpEquipmentsLT3 = new HashMap<>();
		for (LevelUpEquipment levelUpItem : levelUpEquipmentLT3List) {
			levelUpEquipmentsLT3.put(levelUpItem.getLevel(), levelUpItem);
		}

		levelUpEquipmentsGT3 = new HashMap<>();
		for (LevelUpEquipment levelUpItem : levelUpEquipmentGT3List) {
			levelUpEquipmentsGT3.put(levelUpItem.getLevel(), levelUpItem);
		}

	}


	public int getExpFromHero(int rank, boolean sameElement) {
		LevelUpHeroByHero levelUpHeroByHero = levelUpHeroByHeroes.get(rank);
		if (levelUpHeroByHero == null || levelUpHeroByHero.getSameElementals() <= 0) {
			return -1;
		}

		return sameElement ? levelUpHeroByHero.getSameElementals() : levelUpHeroByHero.getDifferentElementals();
	}


	public int getExpFromItem(int itemIndex, boolean sameElement) {
		LevelUpHeroByItem levelUpHeroByItem = levelUpHeroByItems.get(itemIndex);
		if (levelUpHeroByItem == null) {
			return -1;
		}

		return sameElement ? levelUpHeroByItem.getSameElementals() : levelUpHeroByItem.getDifferentElementals();
	}


	public List<EvolveHero> getEvolveHeroList() {
		return evolveHeroList;
	}


	public void setEvolveHeroList(List<EvolveHero> evolveHeroes) {
		this.evolveHeroList = evolveHeroes;
	}


	public List<LevelUpHeroByHero> getLevelUpHeroByHeroList() {
		return levelUpHeroByHeroList;
	}


	public void setLevelUpHeroByHeroList(List<LevelUpHeroByHero> levelUpHeroByHeroes) {
		this.levelUpHeroByHeroList = levelUpHeroByHeroes;
	}


	public List<LevelUpHeroByItem> getLevelUpHeroByItemList() {
		return levelUpHeroByItemList;
	}


	public void setLevelUpHeroByItemList(List<LevelUpHeroByItem> levelUpHeroByItems) {
		this.levelUpHeroByItemList = levelUpHeroByItems;
	}


	public Map<String, EvolveHero> getEvolveHeroes() {
		return evolveHeroes;
	}


	public void setEvolveHeroes(Map<String, EvolveHero> evolveHeroes) {
		this.evolveHeroes = evolveHeroes;
	}


	public Map<Integer, LevelUpHeroByHero> getLevelUpHeroByHeroes() {
		return levelUpHeroByHeroes;
	}


	public void setLevelUpHeroByHeroes(Map<Integer, LevelUpHeroByHero> levelUpHeroByHeroes) {
		this.levelUpHeroByHeroes = levelUpHeroByHeroes;
	}


	public Map<Integer, LevelUpHeroByItem> getLevelUpHeroByItems() {
		return levelUpHeroByItems;
	}


	public void setLevelUpHeroByItems(Map<Integer, LevelUpHeroByItem> levelUpHeroByItems) {
		this.levelUpHeroByItems = levelUpHeroByItems;
	}


	public Map<Integer, LevelUpEquipment> getLevelUpEquipmentsLT3() {
		return levelUpEquipmentsLT3;
	}


	public Map<Integer, LevelUpEquipment> getLevelUpEquipmentsGT3() {
		return levelUpEquipmentsGT3;
	}

}
