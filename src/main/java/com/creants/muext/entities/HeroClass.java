package com.creants.muext.entities;

import com.creants.muext.entities.states.AdditionStats;

/**
 * @author LamHM
 *
 */
public abstract class HeroClass extends Character {
	protected int id;
	protected int exp;

	private AdditionStats additionStats;


	public HeroClass() {
		init();
	}


	public abstract void init();


	public int getId() {
		return id;
	}


	public int getExp() {
		return exp;
	}


	public void setExp(int exp) {
		this.exp = exp;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public AdditionStats getAdditionStats() {
		return additionStats;
	}


	public void setAdditionStats(AdditionStats additionStats) {
		this.additionStats = additionStats;
	}


	public int getExpPoolSize() {
		return -1;
	}

}
