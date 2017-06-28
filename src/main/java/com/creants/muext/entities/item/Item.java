package com.creants.muext.entities.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import com.creants.muext.entities.ItemBase;

/**
 * @author LamHM
 *
 */
public class Item {
	@Id
	private long id;
	@Indexed
	private transient String gameHeroId;
	private int index;
	private int no;
	private int gridIndex;
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

}
