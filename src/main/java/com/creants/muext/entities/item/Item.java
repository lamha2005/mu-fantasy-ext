package com.creants.muext.entities.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.muext.entities.ItemBase;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_items")
public class Item {
	@Id
	private long id;
	@Indexed
	private transient String gameHeroId;
	private int itemGroup;

	private int index;
	private int no;
	private Integer gridIndex;
	private Boolean isHandsOn;
	private boolean isOverlap;

	@Transient
	private transient ItemBase itemBase;


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


	public Boolean getIsHandsOn() {
		return isHandsOn;
	}


	public void setIsHandsOn(Boolean isHandsOn) {
		this.isHandsOn = isHandsOn;
	}


	public boolean isOverlap() {
		return isOverlap;
	}


	public void setOverlap(boolean isOverlap) {
		this.isOverlap = isOverlap;
	}

}
