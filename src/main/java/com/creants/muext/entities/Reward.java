package com.creants.muext.entities;

import java.util.List;

/**
 * @author LamHM
 *
 */
public class Reward {
	private int id;
	private long exp;
	private int soul;
	private int zen;

	private List<Item> items;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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


	public long getExp() {
		return exp;
	}


	public void setExp(long exp) {
		this.exp = exp;
	}


	public List<Item> getItems() {
		return items;
	}


	public void setItems(List<Item> items) {
		this.items = items;
	}

}
