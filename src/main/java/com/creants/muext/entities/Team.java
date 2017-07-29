package com.creants.muext.entities;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class Team implements SerializableQAntType {
	public String name;
	public long[] heroes;


	public Team() {
		heroes = new long[] { -1L, -1L, -1L, -1L };

	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long[] getHeroes() {
		return heroes;
	}


	public void setHeroes(long[] heroes) {
		this.heroes = heroes;
	}


	public void addHero(long heroId) {
		for (int i = 0; i < heroes.length; i++) {
			if (heroes[i] < 0) {
				heroes[i] = heroId;
				break;
			}
		}
	}

}
