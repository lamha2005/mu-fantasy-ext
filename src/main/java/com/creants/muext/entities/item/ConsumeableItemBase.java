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
	@JacksonXmlProperty(localName = "Overlap", isAttribute = true)
	private int overlap;
	@JacksonXmlProperty(localName = "EffectType", isAttribute = true)
	private Integer effectType;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	private String desc;


	public int getItemType() {
		return itemType;
	}


	public void setItemType(int itemType) {
		this.itemType = itemType;
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

}
