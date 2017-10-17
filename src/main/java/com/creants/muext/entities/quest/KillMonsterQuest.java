package com.creants.muext.entities.quest;

import java.util.Map;

/**
 * @author LamHM
 *
 */
public class KillMonsterQuest extends HeroQuest {
	private Map<Integer, Integer> monsters;


	public Map<Integer, Integer> getMonsters() {
		return monsters;
	}


	public void setMonsters(Map<Integer, Integer> monsters) {
		this.monsters = monsters;
	}


	public boolean killMonster(Map<Integer, Integer> monsterMap) {
		int count = getCount();
		int clearCount = monsters.size();
		for (Integer monsterIndex : monsterMap.keySet()) {
			Integer monsterNo = monsters.get(monsterIndex);
			if (monsterNo == null)
				continue;

			int remain = monsterNo - monsterMap.get(monsterIndex);
			if (remain <= 0) {
				remain = 0;
				clearCount--;
			}

			count = remain;
			monsters.put(monsterIndex, remain);
		}

		return count <= 0;
	}

}
