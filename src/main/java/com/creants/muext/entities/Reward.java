package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LamHM
 *
 */
public class Reward {
	private int index;
	private int exp;
	private int soul;
	private int zen;

	private List<ItemBase> items;


	public Reward() {
		items = new ArrayList<>();
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getSoul() {
		return soul;
	}


	public void setSoul(int soul) {
		this.soul = soul;
	}


	public int getZen() {
		return zen;
	}


	public void setZen(int zen) {
		this.zen = zen;
	}


	public int getExp() {
		return exp;
	}


	public void setExp(int exp) {
		this.exp = exp;
	}


	public List<ItemBase> getItems() {
		return items;
	}


	public void setItems(List<ItemBase> items) {
		this.items = items;
	}

}
