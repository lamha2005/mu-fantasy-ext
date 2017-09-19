package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.config.ChaosCastleConfig;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.ChaosCastlePowerRepository;
import com.creants.muext.dao.ChaosCastleRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastlePower;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosStageBase;
import com.creants.muext.entities.chaos.OpponentHeroClass;

/**
 * @author LamHM
 *
 */
@Service
public class ChaosCastleManager implements InitializingBean {
	private static final String RANK_D = "D";
	private static final String RANK_C = "C";
	private static final String RANK_B = "B";
	private static final String RANK_A = "A";
	private static final String RANK_S = "S";
	private Map<String, List<HeroClass>> teamMap;
	private String[] rankArr;

	@Autowired
	private ChaosCastleRepository chaosRepository;

	@Autowired
	private ChaosCastlePowerRepository chaosPowerRepository;

	@Autowired
	private BattleTeamRepository battleTeamRepository;

	@Autowired
	private HeroClassManager heroManager;

	@Autowired
	private NPCManager npcManager;

	private static final ItemConfig itemConfig = ItemConfig.getInstance();


	@Override
	public void afterPropertiesSet() throws Exception {
		teamMap = new HashMap<>();
		rankArr = new String[] { RANK_D, RANK_C, RANK_B, RANK_A, RANK_S };

		// Khi deploy server lần đầu tiên, hoặc chủ động load lại
		loadNPC();
		// 2h sáng load 1 lần cho tất cả user
		// loadUser();
	}


	private String getNextRank(String curRank) {
		int length = rankArr.length;
		for (int i = 0; i < length; i++) {
			if (curRank.equals(rankArr[i]) && i < (length - 1)) {
				return rankArr[i + 1];
			}
		}

		return RANK_D;
	}


	private void loadNPC() {
		long startTime = System.currentTimeMillis();
		List<ChaosCastlePower> powerList = new ArrayList<>();
		List<GameHero> npcList = npcManager.findHeroesByIsNPCIsTrue();
		for (GameHero gameHero : npcList) {
			List<HeroClass> heroes = npcManager.findHeroesByGameHeroId(gameHero.getId());
			Long[] battleTeam = new Long[] { -1L, -1L, -1L, -1L };
			int count = 0;
			int power = 0;
			for (HeroClass heroClass : heroes) {
				power += heroClass.getPower();
				battleTeam[count] = heroClass.getId();
				count++;
			}

			ChaosCastlePower battlePower = new ChaosCastlePower();
			battlePower.setGameHeroId(gameHero.getId());
			battlePower.setNPC(true);
			battlePower.setMaxPower(power);
			battlePower.setTeamPower(power);
			battlePower.setBattleTeam(battleTeam);
			powerList.add(battlePower);
		}

		chaosPowerRepository.save(powerList);
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
			chaosInfo.setRank(RANK_D);

			BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
			List<HeroClass> findHeroes = heroManager.findHeroes(battleTeam.getMainHeroes());
			if (!findHeroes.isEmpty()) {
				teamMap.put(gameHeroId, findHeroes);
			}

			// chaosInfo.setBattleTeam(battleTeam.getMainTeamArray());

		}

		return chaosInfo.getStage();
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


	public void saveChaosCastleInfo(ChaosCastleInfo chaosCastleInfo) {
		chaosRepository.save(chaosCastleInfo);
	}


	public ChaosCastleInfo getChaosCastleInfo(String gameHeroId) {
		ChaosCastleInfo chaosInfo = chaosRepository.findOne(gameHeroId);
		if (chaosInfo == null) {
			chaosInfo = new ChaosCastleInfo();
			chaosInfo.setGameHeroId(gameHeroId);
			chaosInfo.setTicketNo(1);
			chaosInfo.setBeginTime(System.currentTimeMillis());
			chaosInfo.setRank(RANK_D);

			BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
			List<HeroClass> findHeroes = heroManager.findHeroesFullInfo(battleTeam.getMainHeroes());
			int maxPower = 0;
			if (!findHeroes.isEmpty()) {
				teamMap.put(gameHeroId, findHeroes);
				int teamPower = 0;
				List<OpponentHeroClass> heroes = new ArrayList<>(findHeroes.size());
				for (HeroClass heroClass : findHeroes) {
					teamPower += heroClass.getPower();
					heroes.add(new OpponentHeroClass(heroClass));
				}
				maxPower = teamPower;

				ChaosCastlePower chaosPower = new ChaosCastlePower();
				chaosPower.setGameHeroId(gameHeroId);
				chaosPower.setBattleTeam(battleTeam.getMainTeamArray());
				chaosPower.setMaxPower(maxPower);
				chaosPower.setTeamPower(teamPower);
				chaosPowerRepository.save(chaosPower);
			}

			// mở stage đầu tiên
			ChaosStageBase castleStage = ChaosCastleConfig.getInstance().getFirstStage();
			ChaosCastleStage stage = new ChaosCastleStage();
			stage.setId(castleStage.getIndex());

			// TODO khi nào ko có user tương ứng thì mới dùng boss
			ChaosCastlePower opponent = findOpponent(maxPower, castleStage);
			GameHero gameHero = npcManager.getGameHero(opponent.getGameHeroId());
			stage.setAccName(gameHero.getName());
			stage.setLevel(gameHero.getLevel());
			List<HeroClass> heroes = gameHero.getHeroes();
			List<OpponentHeroClass> chaosHeroes = new ArrayList<>(heroes.size());
			int teamPower = 0;
			for (HeroClass heroClass : heroes) {
				chaosHeroes.add(new OpponentHeroClass(heroClass));
				teamPower += heroClass.getPower();
			}
			stage.setHeroes(chaosHeroes);
			stage.setTeamPower(teamPower);

			// thông tin reward
			stage.setReward(itemConfig.splitItem(castleStage.getFullReward(chaosInfo.getRank())));

			chaosInfo.setStage(stage);

			chaosRepository.save(chaosInfo);
		} else {
			ChaosCastleStage stage = chaosInfo.getStage();
			ChaosStageBase stageBase = ChaosCastleConfig.getInstance().getStage(stage.getId());
			// thông tin reward
			stage.setReward(itemConfig.splitItem(stageBase.getFullReward(chaosInfo.getRank())));
		}

		return chaosInfo;
	}


	public ChaosCastleStage getNextStage(ChaosCastleInfo chaosInfo) {
		ChaosCastlePower chaosCastlePower = chaosInfo.getChaosCastlePower();
		ChaosCastleStage curStage = chaosInfo.getStage();
		ChaosStageBase stageBase = null;
		if (curStage.getId() < 20) {
			stageBase = ChaosCastleConfig.getInstance().getStage(curStage.getId() + 1);
		} else {
			stageBase = ChaosCastleConfig.getInstance().getStage(curStage.getId() + 1);
			chaosInfo.setRank(getNextRank(chaosInfo.getRank()));
		}
		ChaosCastleStage stage = new ChaosCastleStage();
		stage.setId(stageBase.getIndex());
		ChaosCastlePower opponent = findOpponent(chaosCastlePower.getMaxPower(), stageBase);
		GameHero gameHero = npcManager.getGameHero(opponent.getGameHeroId());
		stage.setAccName(gameHero.getName());
		stage.setLevel(gameHero.getLevel());
		List<HeroClass> heroes = gameHero.getHeroes();
		List<OpponentHeroClass> chaosHeroes = new ArrayList<>(heroes.size());
		int teamPower = 0;
		for (HeroClass heroClass : heroes) {
			chaosHeroes.add(new OpponentHeroClass(heroClass));
			teamPower += heroClass.getPower();
		}
		stage.setHeroes(chaosHeroes);
		stage.setTeamPower(teamPower);

		// thông tin reward
		stage.setReward(itemConfig.splitItem(stageBase.getFullReward(chaosInfo.getRank())));
		chaosRepository.save(chaosInfo);
		return stage;
	}


	private ChaosCastlePower findOpponent(int maxPower, ChaosStageBase castleStage) {
		int[] battlePowserArr = castleStage.getBattlePowserArr();
		int from = maxPower * battlePowserArr[0] / 100;
		int to = maxPower * battlePowserArr[1] / 100;
		List<ChaosCastlePower> findByTeamPowerBetween = chaosPowerRepository.findByTeamPowerBetween(from, to);
		if (findByTeamPowerBetween.size() <= 0)
			return null;

		return findByTeamPowerBetween.get(0);
	}
}
