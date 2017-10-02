package com.creants.muext.entities.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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

		monsters = new HashMap<Integer, Integer>();
		String taskString = getTaskString();
		if (taskString == null)
			return;

		String[] monsterArr = StringUtils.split(taskString, "#");
		for (int i = 0; i < monsterArr.length; i++) {
			for (String monsterStr : monsterArr) {
				String[] monsterInfo = StringUtils.split(monsterStr, "/");
				monsters.put(Integer.parseInt(monsterInfo[0]), Integer.parseInt(monsterInfo[1]));
			}
		}
	}


	public Map<Integer, Integer> getMonsters() {
		return monsters;
	}


	public Collection<Integer> doGetMonsterList() {
		return new ArrayList<>(monsters.keySet());
	}

}
