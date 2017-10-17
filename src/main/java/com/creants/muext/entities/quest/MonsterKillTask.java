package com.creants.muext.entities.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LamHM
 *
 */
public class MonsterKillTask extends Quest {
	// monster, no
	private Map<Integer, Integer> monsters;


	@Override
	public void convertBase() {
		splitReward();
		splitStageIndex();

		monsters = new HashMap<Integer, Integer>();
		monsters.put(getTask(), getCount());
	}


	public Map<Integer, Integer> getMonsters() {
		return monsters;
	}


	public Collection<Integer> doGetMonsterList() {
		return new ArrayList<>(monsters.keySet());
	}

}
