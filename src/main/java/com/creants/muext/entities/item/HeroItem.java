package com.creants.muext.entities.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero-items")
public class HeroItem implements SerializableQAntType {
	@Id
	public long id;
	@Indexed
	private transient String gameHeroId;

	public int index;

	public int rank;

	private String element;

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


	@Override
	public boolean equals(Object obj) {
		HeroItem heroItem = (HeroItem) obj;
		return this.itemGroup == heroItem.getItemGroup() && this.index == heroItem.getIndex();
	}


	@Override
	public String toString() {
		return "{gameHeroId:" + gameHeroId + ", id:" + id + ", index: " + index + "}";
	}

}
