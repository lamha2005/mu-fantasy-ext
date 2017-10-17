package com.creants.muext.managers;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.config.ChaosCastleConfig;
import com.creants.muext.config.HeroClassConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.ChaosCastlePowerRepository;
import com.creants.muext.dao.ChaosCastleRepository;
import com.creants.muext.dao.ChaosCastleStageRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastlePower;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosStageBase;
import com.creants.muext.entities.ext.ShortItemExt;
import com.creants.muext.services.AutoIncrementService;

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
	private static final int MAX_STAGE = 20;
	private String[] rankArr;

	@Autowired
	private ChaosCastleRepository chaosRepository;

	@Autowired
	private ChaosCastleStageRepository chaosStageRepository;

	@Autowired
	private ChaosCastlePowerRepository chaosPowerRepository;

	@Autowired
	private BattleTeamRepository battleTeamRepository;

	@Autowired
	private HeroClassManager heroManager;

	@Autowired
	private AutoIncrementService autoIncrService;


	@Override
	public void afterPropertiesSet() throws Exception {
		rankArr = new String[] { RANK_D, RANK_C, RANK_B, RANK_A, RANK_S };

		// Khi deploy server lần đầu tiên, hoặc chủ động load lại
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


	private int calculateTeamPower(List<HeroClass> heroes) {
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


	public ChaosCastleInfo getChaosCastleInfo(String gameHeroId, String gameHeroName) {
		ChaosCastleInfo chaosInfo = chaosRepository.findOne(gameHeroId);
		if (chaosInfo == null) {
			chaosInfo = new ChaosCastleInfo();
			chaosInfo.setGameHeroId(gameHeroId);
			chaosInfo.setTicketNo(1);
			chaosInfo.setBeginTime(System.currentTimeMillis());
			chaosInfo.setRank(RANK_D);

			// create battle power
			BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
			List<HeroClass> heroList = heroManager.findHeroesFullInfo(battleTeam.getMainHeroes());
			ChaosCastlePower chaosPower = new ChaosCastlePower();
			chaosPower.setGameHeroId(gameHeroId);
			chaosPower.setBattleTeam(battleTeam.getMainTeamArray());
			chaosPower.setGameHeroName(gameHeroName);

			List<HeroClass> topDamHero = heroManager.getTopDamHero(gameHeroId);
			chaosPower.setTeamPower(calculateTeamPower(heroList));
			chaosPower.setMaxPower(calculateTeamPower(topDamHero));
			chaosPower.setHeroes(heroList);
			chaosPowerRepository.save(chaosPower);
			chaosRepository.save(chaosInfo);

		}

		return chaosInfo;
	}


	public ChaosCastleStage findChaosCastleStage(String gameHeroId) {
		ChaosCastleStage chaosCastleStage = chaosStageRepository.findOne(gameHeroId);
		if (chaosCastleStage == null) {
			ChaosStageBase castleStage = ChaosCastleConfig.getInstance().getFirstStage();
			ChaosCastleStage stage = new ChaosCastleStage();
			stage.setStageIndex(castleStage.getIndex());

			ChaosCastlePower chaosPower = chaosPowerRepository.findOne(gameHeroId);
			ChaosCastlePower opponent = findOpponent(chaosPower.getMaxPower(), castleStage,
					chaosPower.getMinLevelInTeam());
			if (opponent == null) {
				QAntTracer.warn(this.getClass(), "Can not found opponent for maxPower = " + chaosPower.getMaxPower());
				return null;
			}

			chaosCastleStage = new ChaosCastleStage();
			chaosCastleStage.setGameHeroId(gameHeroId);
			chaosCastleStage.setOpponentId(opponent.getGameHeroId());
			chaosCastleStage.setRank(RANK_D);
			chaosCastleStage.setStageIndex(stage.getStageIndex());
			chaosCastleStage.setClamed(false);
			chaosCastleStage.setOpponent(opponent);
			chaosStageRepository.save(chaosCastleStage);
		} else {
			ChaosCastlePower opponent = chaosPowerRepository.findOne(chaosCastleStage.getOpponentId());
			chaosCastleStage.setOpponent(opponent);
		}

		return chaosCastleStage;
	}


	public ChaosCastleStage getChaosCastleStage(String gameHeroId) {
		return chaosStageRepository.findOne(gameHeroId);
	}


	public List<ShortItemExt> getReward(int stageIndex, int rank) {
		return null;
	}


	public ChaosCastleStage getNextStage(ChaosCastleStage stage) {
		int stageIndex = stage.getStageIndex() + 1;
		if (stageIndex >= MAX_STAGE) {
			stage.setRank(getNextRank(stage.getRank()));
			stageIndex = 0;
		}

		stage.setClamed(false);
		stage.setWin(false);
		stage.setStageIndex(stageIndex);

		ChaosStageBase nextStageBase = ChaosCastleConfig.getInstance().getStage(stageIndex);

		ChaosCastlePower chaosCastlePower = chaosPowerRepository.findOne(stage.getGameHeroId());
		ChaosCastlePower opponent = findOpponent(chaosCastlePower.getMaxPower(), nextStageBase, 1);

		if (opponent == null) {
			QAntTracer.warn(this.getClass(), "Can not found opponent for maxPower = " + chaosCastlePower.getMaxPower());
			return null;
		}

		stage.setOpponentId(opponent.getGameHeroId());
		stage.setOpponent(opponent);
		chaosStageRepository.save(stage);

		return stage;
	}


	private ChaosCastlePower findOpponent(int maxPower, ChaosStageBase castleStage, int maxLevel) {
		int[] battlePowerArr = castleStage.getBattlePowserArr();
		int fromRate = battlePowerArr[0];
		int from = maxPower * fromRate / 100;
		int to = maxPower * battlePowerArr[1] / 100;
		List<ChaosCastlePower> findByTeamPowerBetween = chaosPowerRepository.findByTeamPowerBetween(from, to);
		if (findByTeamPowerBetween.size() > 0)
			return findByTeamPowerBetween.get(0);

		List<HeroClass> npcHeroList = HeroClassConfig.getInstance().genNPC(maxPower, fromRate, maxLevel);
		ChaosCastlePower result = new ChaosCastlePower();
		result.setGameHeroId("mus1#" + autoIncrService.genNPCId() + "#npc");
		result.setGameHeroName(result.getGameHeroId());

		for (HeroClass heroClass : npcHeroList) {
			result.addHero(heroClass.getId());
		}

		result.setTeamLeader(new Random().nextInt(npcHeroList.size()));
		int teamPower = calculateTeamPower(npcHeroList);
		result.setMaxPower(teamPower);
		result.setTeamPower(teamPower);
		result.setHeroes(npcHeroList);
		chaosPowerRepository.save(result);
		return result;
	}
}
