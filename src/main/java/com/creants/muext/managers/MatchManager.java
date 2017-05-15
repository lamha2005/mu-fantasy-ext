package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.muext.config.MonsterConfig;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.Monster;

@Service
public class MatchManager implements InitializingBean {
	private MonsterConfig monsterConfig;

	private Map<String, IQAntObject> matchMap;


	@Override
	public void afterPropertiesSet() throws Exception {
		monsterConfig = MonsterConfig.getInstance();
		matchMap = new ConcurrentHashMap<>();
	}


	public List<Monster> getMonsterList(List<Integer[]> monsterIndex) {
		List<Monster> monsters = new ArrayList<>();

		int monsterId = 0;
		for (Integer[] indexes : monsterIndex) {
			for (int i = 0; i < indexes.length; i++) {
				Monster monster = monsterConfig.getMonster(indexes[i]);
				monster.setId(monsterId);
				monsters.add(monster);
				monsterId++;
			}
		}

		return monsters;
	}


	public IQAntObject getMatch(int matchId) {
		return matchMap.get(matchId);
	}


	public boolean finish(GameHero gameHero) {
		IQAntObject match = matchMap.get(gameHero.getId());
		if (match == null) {
			return false;
		}

		// TODO check and reward

		return true;
	}


	public void removeMatch(String gameHeroId) {
		boolean contains = matchMap.containsKey(gameHeroId);
		if (contains) {
			matchMap.remove(gameHeroId);
		}
	}
}
