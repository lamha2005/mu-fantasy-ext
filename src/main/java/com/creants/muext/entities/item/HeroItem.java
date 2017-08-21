package com.creants.muext.entities.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.entities.ItemBase;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_items")
public class HeroItem implements SerializableQAntType {
	@Id
	public long id;
	@Indexed
	private String gameHeroId;

	public Long heroId;
	public String element;
	public int itemGroup;

	public int index;
	public int no;
	private Integer gridIndex;
	public boolean isOverlap;
	public Integer slotIndex;

	@Transient
	private transient ItemBase itemBase;


	public HeroItem() {

	}


	public HeroItem(long id, String gameHeroId, HeroItem heroItem) {
		this.id = id;
		this.gameHeroId = gameHeroId;
		element = heroItem.getElement();
		itemGroup = heroItem.getItemGroup();
		index = heroItem.getIndex();
		no = heroItem.getNo();
		isOverlap = heroItem.isOverlap();
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public Long getHeroId() {
		return heroId;
	}


	public void setHeroId(Long heroId) {
		this.heroId = heroId;
	}


	public String getElement() {
		return element;
	}


	public void setElement(String element) {
		this.element = element;
	}


	public int getItemGroup() {
		return itemGroup;
	}


	public void setItemGroup(int itemGroup) {
		this.itemGroup = itemGroup;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}


	public void incr(int value) {
		this.no += value;
	}


	public void decr(int value) {
		this.no -= value;
	}


	public int getGridIndex() {
		return gridIndex;
	}


	public void setGridIndex(int gridIndex) {
		this.gridIndex = gridIndex;
	}


	public ItemBase getItemBase() {
		return itemBase;
	}


	public void setItemBase(ItemBase itemBase) {
		this.itemBase = itemBase;
	}


	public boolean isOverlap() {
		return isOverlap;
	}


	public void setOverlap(boolean isOverlap) {
		this.isOverlap = isOverlap;
	}


	public Integer getSlotIndex() {
		return slotIndex;
	}


	public void setSlotIndex(Integer slotIndex) {
		this.slotIndex = slotIndex;
	}


	public void takeOn(Long heroId, int slotIndex) {
		this.heroId = heroId;
		this.slotIndex = slotIndex;
	}


	public void takeOff() {
		this.heroId = null;
		this.slotIndex = null;
	}


	@Override
	public boolean equals(Object obj) {
		HeroItem heroItem = (HeroItem) obj;
		return this.itemGroup == heroItem.getItemGroup() && this.index == heroItem.getIndex();
	}


	@Override
	public String toString() {
		return "{gameHeroId:" + gameHeroId + ", id:" + id + ", index: " + index + ", no:" + no + "}";
	}

}
