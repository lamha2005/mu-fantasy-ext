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
@Document(collection = "hero-items")
public abstract class HeroItem implements SerializableQAntType {
	@Id
	public long id;
	@Indexed
	private transient String gameHeroId;

	public int index;

	@Transient
	public int rank;

	@Transient
	public String element;

	private transient int itemGroup;

	private Integer gridIndex;

	public int no;


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


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
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


	public Integer getGridIndex() {
		return gridIndex;
	}


	public void setGridIndex(Integer gridIndex) {
		this.gridIndex = gridIndex;
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}


	public abstract void setItemBase(ItemBase itemBase);


	public abstract int getItemType();


	@Override
	public String toString() {
		return "{gameHeroId:" + gameHeroId + ", id:" + id + ", index: " + index + "}";
	}

}
