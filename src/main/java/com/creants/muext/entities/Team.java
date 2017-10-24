package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class Team implements SerializableQAntType {
	private int index;
	public String name;
	public Long[] heroes;
	public int leaderIndex;


	public Team() {
		heroes = new Long[] { -1L, -1L, -1L, -1L, -1L };
		index = -1;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long[] getHeroes() {
		return heroes;
	}


	public void setHeroes(Long[] heroes) {
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


	public List<Long> getIdList() {
		List<Long> heroIds = new ArrayList<>(heroes.length);
		for (int i = 0; i < heroes.length; i++) {
			heroIds.add(heroes[i]);
		}
		return heroIds;
	}


	public List<Long> getHeroIds() {
		List<Long> heroIds = new ArrayList<>();
		for (int i = 0; i < heroes.length; i++) {
			Long id = heroes[i];
			if (id > 0) {
				heroIds.add(id);
			}
		}
		return heroIds;
	}


	public List<Long> getFormation() {
		List<Long> heroIds = new ArrayList<>();
		for (int i = 0; i < heroes.length; i++) {
			heroIds.add(heroes[i]);
		}
		return heroIds;
	}


	public int getLeaderIndex() {
		return leaderIndex;
	}


	public void setLeaderIndex(int leaderIndex) {
		this.leaderIndex = leaderIndex;
	}

}
