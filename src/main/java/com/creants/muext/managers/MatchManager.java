package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.muext.config.MonsterConfig;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.world.Stage;

/**
 * @author LamHM
 *
 */
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
				Monster monster = monsterConfig.createMonster(indexes[i]);
				monster.setId(monsterId);
				monsters.add(monster);
				monsterId++;
			}
		}

		return monsters;
	}


	public IQAntArray getRounds(Stage stage, List<Monster> monsterList) {
		IQAntArray rounds = QAntArray.newInstance();
		List<Integer[]> roundList = stage.getRoundList();
		int count = 0;
		for (Integer[] integers : roundList) {
			Integer[] round = new Integer[integers.length];
			for (int i = 0; i < integers.length; i++) {
				round[i] = count;
				count++;
			}
			rounds.addIntArray(Arrays.asList(round));
		}

		return rounds;
	}


	public IQAntObject getMatch(String gameHeroId) {
		return matchMap.get(gameHeroId);
	}


	public void newMatch(String gameHeroId, IQAntObject matchInfo) {
		matchMap.put(gameHeroId, matchInfo);
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
