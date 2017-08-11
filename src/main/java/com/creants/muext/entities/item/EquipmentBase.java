package com.creants.muext.entities.item;

import com.creants.muext.entities.ItemBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonIgnoreProperties(value = { "availableHeroesString", "equipSlotString" })
public class EquipmentBase extends ItemBase {
	@JacksonXmlProperty(localName = "EquipSlot", isAttribute = true)
	private String equipSlotString;
	@JacksonXmlProperty(localName = "ItemRank", isAttribute = true)
	private int itemRank;
	@JacksonXmlProperty(localName = "SkillIndex", isAttribute = true)
	private int skillIndex;
	@JacksonXmlProperty(localName = "OptionIndex", isAttribute = true)
	private int optionIndex;
	@JacksonXmlProperty(localName = "TwoHanded", isAttribute = true)
	private Boolean twoHanded;

	@JacksonXmlProperty(localName = "LVRequire", isAttribute = true)
	private int levelRequire;
	@JacksonXmlProperty(localName = "ATK", isAttribute = true)
	private Integer atk;
	@JacksonXmlProperty(localName = "HP", isAttribute = true)
	private Integer hp;
	@JacksonXmlProperty(localName = "REC", isAttribute = true)
	private Integer rec;
	@JacksonXmlProperty(localName = "DEF", isAttribute = true)
	private Integer def;
	@JacksonXmlProperty(localName = "BlessCost", isAttribute = true)
	private Integer blessCost;
	@JacksonXmlProperty(localName = "ZenCost", isAttribute = true)
	private Integer zenCost;
	@JacksonXmlProperty(localName = "RecipeIndex", isAttribute = true)
	private Integer recipeIndex;
	@JacksonXmlProperty(localName = "Upgarde", isAttribute = true)
	private Boolean upgarde;
	@JacksonXmlProperty(localName = "Disassemble", isAttribute = true)
	private Boolean disassemble;
	@JacksonXmlProperty(localName = "Elemental", isAttribute = true)
	private String elemental;

	@JacksonXmlProperty(localName = "lvupATK", isAttribute = true)
	private int lvupATK;
	@JacksonXmlProperty(localName = "lvupHP", isAttribute = true)
	private int lvupHP;
	@JacksonXmlProperty(localName = "lvupDEF", isAttribute = true)
	private int lvupDEF;
	@JacksonXmlProperty(localName = "lvupREC", isAttribute = true)
	private int lvupREC;
	@JacksonXmlProperty(localName = "Evovle", isAttribute = true)
	private int evovle;

	@JacksonXmlProperty(localName = "AvailableClassGroup", isAttribute = true)
	private transient String availableHeroesString;
	private int[] availableClassGroups;
	private int[] equipSlot;


	public String getEquipSlotString() {
		return equipSlotString;
	}


	public void setEquipSlotString(String equipSlotString) {
		this.equipSlotString = equipSlotString;
	}


	public int[] getEquipSlot() {
		return equipSlot;
	}


	public void setEquipSlot(int[] equipSlot) {
		this.equipSlot = equipSlot;
	}


	public int getLevelRequire() {
		return levelRequire;
	}


	public void setLevelRequire(int levelRequire) {
		this.levelRequire = levelRequire;
	}


	public Integer getAtk() {
		return atk;
	}


	public void setAtk(Integer atk) {
		this.atk = atk;
	}


	public Integer getHp() {
		return hp;
	}


	public void setHp(Integer hp) {
		this.hp = hp;
	}


	public Integer getRec() {
		return rec;
	}


	public void setRec(Integer rec) {
		this.rec = rec;
	}


	public Integer getDef() {
		return def;
	}


	public void setDef(Integer def) {
		this.def = def;
	}


	public String getAvailableHeroesString() {
		return availableHeroesString;
	}


	public void setAvailableHeroesString(String availableHeroesString) {
		this.availableHeroesString = availableHeroesString;
	}


	public int[] getAvailableClassGroups() {
		return availableClassGroups;
	}


	public void setAvailableClassGroups(int[] availableClassGroups) {
		this.availableClassGroups = availableClassGroups;
	}


	public Integer getBlessCost() {
		return blessCost;
	}


	public void setBlessCost(Integer blessCost) {
		this.blessCost = blessCost;
	}


	public Integer getZenCost() {
		return zenCost;
	}


	public void setZenCost(Integer zenCost) {
		this.zenCost = zenCost;
	}


	public Boolean getUpgarde() {
		return upgarde;
	}


	public void setUpgarde(Boolean upgarde) {
		this.upgarde = upgarde;
	}


	public Boolean getDisassemble() {
		return disassemble;
	}


	public void setDisassemble(Boolean disassemble) {
		this.disassemble = disassemble;
	}


	public String getElemental() {
		return elemental;
	}


	public void setElemental(String elemental) {
		this.elemental = elemental;
	}


	public int getItemRank() {
		return itemRank;
	}


	public void setItemRank(int itemRank) {
		this.itemRank = itemRank;
	}


	public int getSkillIndex() {
		return skillIndex;
	}


	public void setSkillIndex(int skillIndex) {
		this.skillIndex = skillIndex;
	}


	public int getOptionIndex() {
		return optionIndex;
	}


	public void setOptionIndex(int optionIndex) {
		this.optionIndex = optionIndex;
	}


	public Boolean getTwoHanded() {
		return twoHanded;
	}


	public void setTwoHanded(Boolean twoHanded) {
		this.twoHanded = twoHanded;
	}


	public Integer getRecipeIndex() {
		return recipeIndex;
	}


	public void setRecipeIndex(Integer recipeIndex) {
		this.recipeIndex = recipeIndex;
	}

}
