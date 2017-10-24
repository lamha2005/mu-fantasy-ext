package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.dao.ArenaPowerRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.arena.ArenaPower;

/**
 * @author LamHM
 *
 */
@Service
public class ArenaManager implements InitializingBean {

	@Autowired
	private ArenaPowerRepository arenaPowerRepository;
	@Autowired
	private NPCManager npcManager;

	private Map<String, String> battleMap;


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO chỉ load lần đầu
		loadNPC();
		battleMap = new ConcurrentHashMap<>();
	}


	private void loadNPC() {
		long startTime = System.currentTimeMillis();
		List<ArenaPower> powerList = new ArrayList<>();
		List<GameHero> npcList = npcManager.findHeroesByIsNPCIsTrue();
		npcList.forEach(gameHero -> {
			List<HeroClass> heroes = npcManager.findHeroesByGameHeroId(gameHero.getId());
			Long[] battleTeam = new Long[] { -1L, -1L, -1L, -1L };
			int count = 0;
			int power = 0;
			for (HeroClass heroClass : heroes) {
				power += heroClass.getPower();
				battleTeam[count++] = heroClass.getId();
			}

			ArenaPower battlePower = new ArenaPower();
			battlePower.setGameHeroId(gameHero.getId());
			battlePower.setNPC(true);
			battlePower.setTeamPower(power);
			battlePower.setBattleTeam(battleTeam);
			powerList.add(battlePower);
		});

		powerList.sort((ArenaPower o1, ArenaPower o2) -> o2.getTeamPower() - o1.getTeamPower());
		for (ArenaPower arenaPower : powerList) {
			arenaPower.setRank("A");
		}

		arenaPowerRepository.save(powerList);
		QAntTracer.info(this.getClass(),
				"loadNPC in ArenaManager: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
	}


	private boolean checkInBattle(String gameHeroId) {
		return battleMap.containsKey(gameHeroId);
	}


	private String getOpponent(String gameHeroId) {
		return battleMap.get(gameHeroId);
	}

}
