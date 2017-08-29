package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.ChaosCastleRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosHeroClass;

/**
 * @author LamHM
 *
 */
@Service
public class ChaosCastleManager implements InitializingBean {
	private static final String RANK_A = "A";
	private static final String RANK_B = "B";
	private static final String RANK_C = "C";
	private static final String RANK_D = "D";
	private static final String RANK_S = "S";
	private Map<String, List<HeroClass>> teamMap;

	@Autowired
	private ChaosCastleRepository chaosRepository;

	@Autowired
	private BattleTeamRepository battleTeamRepository;

	@Autowired
	private HeroClassManager heroManager;

	@Autowired
	private NPCManager npcManager;


	@Override
	public void afterPropertiesSet() throws Exception {
		teamMap = new HashMap<>();

		// Khi deploy server lần đầu tiên, hoặc chủ động load lại
		loadNPC();
		// 2h sáng load 1 lần cho tất cả user
		// loadUser();
	}


	private void loadNPC() {
		long startTime = System.currentTimeMillis();
		List<ChaosCastleInfo> chaosPowerList = new ArrayList<>();
		// List<GameHero> npcList =
		// gameHeroRepository.findHeroesByIsNPCIsTrue();
		List<GameHero> npcList = npcManager.findHeroesByIsNPCIsTrue();
		for (GameHero gameHero : npcList) {
			int power = 0;
			// List<HeroClass> heroes =
			// heroManager.findHeroesByGameHeroId(gameHero.getId());
			List<HeroClass> heroes = npcManager.findHeroesByGameHeroId(gameHero.getId());
			List<ChaosHeroClass> chaosHeroes = new ArrayList<>();
			Long[] battleTeam = new Long[] { -1L, -1L, -1L, -1L };
			int count = 0;
			for (HeroClass heroClass : heroes) {
				power += heroClass.getPower();
				chaosHeroes.add(new ChaosHeroClass(heroClass));
				battleTeam[count] = heroClass.getId();
				count++;
			}

			if (power > 0) {
				ChaosCastleInfo chaosInfo = new ChaosCastleInfo();
				chaosInfo.setGameHeroId(gameHero.getId());
				chaosInfo.setNPC(true);
				chaosInfo.setMaxPower(power);
				chaosInfo.setTeamPower(power);
				chaosInfo.setBattleTeam(battleTeam);
				chaosPowerList.add(chaosInfo);
			}
		}

		chaosRepository.save(chaosPowerList);
		QAntTracer.info(this.getClass(),
				"loadNPC in Chaos Castle: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
	}


	public ChaosCastleStage join(String gameHeroId) {
		ChaosCastleInfo chaosInfo = chaosRepository.findOne(gameHeroId);
		if (chaosInfo == null) {
			chaosInfo = new ChaosCastleInfo();
			chaosInfo.setGameHeroId(gameHeroId);
			chaosInfo.setTicketNo(1);
			chaosInfo.setBeginTime(System.currentTimeMillis());
			chaosInfo.setRank(RANK_A);

			BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
			chaosInfo.setBattleTeam(battleTeam.getMainTeamArray());
			List<HeroClass> findHeroes = heroManager.findHeroes(battleTeam.getMainHeroes());
			if (!findHeroes.isEmpty()) {
				teamMap.put(gameHeroId, findHeroes);
			}

		}

		return chaosInfo.getUnlockStage();
	}


	public int calculateTeamPower(List<HeroClass> heroes) {
		int result = 0;
		if (heroes.size() > 0) {
			for (HeroClass heroClass : heroes) {
				result += heroClass.getPower();
			}
		}

		return result;
	}


	public ChaosCastleInfo getChaosCastleInfo(String gameHeroId) {
		ChaosCastleInfo chaosInfo = chaosRepository.findOne(gameHeroId);
		if (chaosInfo == null) {
			chaosInfo = new ChaosCastleInfo();
			chaosInfo.setGameHeroId(gameHeroId);
			chaosInfo.setTicketNo(1);
			chaosInfo.setBeginTime(System.currentTimeMillis());
			chaosInfo.setRank(RANK_A);

			BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
			chaosInfo.setBattleTeam(battleTeam.getMainTeamArray());
			List<HeroClass> findHeroes = heroManager.findHeroesFullInfo(battleTeam.getMainHeroes());
			if (!findHeroes.isEmpty()) {
				teamMap.put(gameHeroId, findHeroes);
				int teamPower = 0;

				List<ChaosHeroClass> heroes = new ArrayList<>(findHeroes.size());
				for (HeroClass heroClass : findHeroes) {
					teamPower += heroClass.getPower();
					heroes.add(new ChaosHeroClass(heroClass));
				}
				chaosInfo.setMaxPower(teamPower);
				chaosInfo.setTeamPower(teamPower);
			}

			// mở stage đầu tiên
			ChaosCastleStage stage = new ChaosCastleStage();
			// TODO khi nào ko có user tương ứng thì mới dùng boss
			GameHero gameHero = npcManager.getGameHero("mus1#1#npc");
			stage.setAccName(gameHero.getName());
			List<HeroClass> heroes = gameHero.getHeroes();
			List<ChaosHeroClass> chaosHeroes = new ArrayList<>(heroes.size());
			for (HeroClass heroClass : heroes) {
				chaosHeroes.add(new ChaosHeroClass(heroClass));
			}
			stage.setHeroes(chaosHeroes);

			chaosRepository.save(chaosInfo);
		}
		return chaosInfo;
	}

}
