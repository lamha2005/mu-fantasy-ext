package com.creants.muext.entities.item;

import com.creants.muext.entities.ItemBase;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class ConsumeableItemBase extends ItemBase implements IConsumeableItem {
	@JacksonXmlProperty(localName = "ItemType", isAttribute = true)
	private int itemType;
	@JacksonXmlProperty(localName = "ItemRank", isAttribute = true)
	private int itemRank;
	@JacksonXmlProperty(localName = "SkillIndex", isAttribute = true)
	private int skillIndex;
	@JacksonXmlProperty(localName = "Overlap", isAttribute = true)
	private int overlap;
	@JacksonXmlProperty(localName = "EffectType", isAttribute = true)
	private Integer effectType;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	private String desc;
	@JacksonXmlProperty(localName = "Value", isAttribute = true)
	private float value;


	public int getItemType() {
		return itemType;
	}


	public void setItemType(int itemType) {
		this.itemType = itemType;
	}


	public int getItemRank() {
		return itemRank;
	}


	public void setItemRank(int itemRank) {
		this.itemRank = itemRank;
	}


	public int getOverlap() {
		return overlap;
	}


	public void setOverlap(int overlap) {
		this.overlap = overlap;
	}


	public Integer getEffectType() {
		return effectType;
	}


	public void setEffectType(Integer effectType) {
		this.effectType = effectType;
	}


	public int getSkillIndex() {
		return skillIndex;
	}


	public void setSkillIndex(int skillIndex) {
		this.skillIndex = skillIndex;
	}


	public float getValue() {
		return value;
	}


	public void setValue(float value) {
		this.value = value;
	}

}
