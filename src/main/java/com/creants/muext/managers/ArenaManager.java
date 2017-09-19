package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.List;

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


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO chỉ load lần đầu
		loadNPC();
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
		int rank = 1;
		for (ArenaPower arenaPower : powerList) {
			arenaPower.setRank(rank++);
		}

		arenaPowerRepository.save(powerList);
		QAntTracer.info(this.getClass(),
				"loadNPC in ArenaManager: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
	}

}
