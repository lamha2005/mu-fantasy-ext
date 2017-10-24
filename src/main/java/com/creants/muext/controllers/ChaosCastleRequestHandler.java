package com.creants.muext.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.ChaosCastleConfig;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastlePower;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosStageBase;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.managers.ChaosCastleManager;
import com.creants.muext.managers.GameHeroManager;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.util.UserHelper;

/**
 * @author LamHM
 *
 */
public class ChaosCastleRequestHandler extends BaseClientRequestHandler {
	private static final int JOIN_CHAOS_CASTLE = 1;
	private static final int FIGHT = 2;
	private static final int FINISH = 3;
	private static final int CLAIM = 4;
	private static final int UPDATE_BATTLE_TEAM = 5;
	private static final int UNLOCK_STAGE = 6;

	private ChaosCastleManager chaosCastleManager;
	private HeroItemManager heroItemManager;
	private GameHeroManager gameHeroManager;
	private HeroClassManager heroClassManager;
	private BattleTeamRepository battleTeamRepository;


	public ChaosCastleRequestHandler() {
		chaosCastleManager = Creants2XApplication.getBean(ChaosCastleManager.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
		gameHeroManager = Creants2XApplication.getBean(GameHeroManager.class);
		battleTeamRepository = Creants2XApplication.getBean(BattleTeamRepository.class);
		heroClassManager = Creants2XApplication.getBean(HeroClassManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		if (action == null) {
			action = JOIN_CHAOS_CASTLE;
		}

		switch (action) {
			case JOIN_CHAOS_CASTLE:
				processJoinCastle(user, params);
				break;
			case FIGHT:
				fight(user, params);
				break;
			case FINISH:
				processFinish(user, params);
				break;
			case CLAIM:
				claim(user, params);
				break;

			case UPDATE_BATTLE_TEAM:
				processUpdateBattleTeam(user, params);
				break;
		}

	}


	private void claim(QAntUser user, IQAntObject params) {
		ChaosCastleStage stage = chaosCastleManager.getChaosCastleStage(user.getName());
		processNextStage(user, stage);
		processReward(params, user, stage);
	}


	private void fight(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();
		BattleTeam battleTeam = battleTeamRepository.findOne(gameHeroId);
		List<Long> heroIds = battleTeam.getMainTeamIds();
		IQAntArray yourCrit = QAntArray.newInstance();

		IQAntObject me = QAntObject.newInstance();
		IQAntArray heroArr = QAntArray.newInstance();
		List<HeroClass> heroes = heroClassManager.findHeroesFullInfo(heroIds);
		for (HeroClass hero : heroes) {
			heroArr.addQAntObject(QAntObject.newFromObject(hero));

			IQAntObject obj = QAntObject.newInstance();
			obj.putLong("id", hero.getId());
			obj.putByteArray("crit", hero.genCrit(1));
			yourCrit.addQAntObject(obj);
		}

		me.putQAntArray("heroes", heroArr);
		me.putLongArray("formation", battleTeam.getMainFormation());
		me.putInt("leaderIndex", battleTeam.getMainTeam().getLeaderIndex());
		me.putQAntArray("crit", yourCrit);

		params.putQAntObject("your_team", me);

		ChaosCastleStage stage = chaosCastleManager.findChaosCastleStage(gameHeroId);
		IQAntObject opponent = QAntObject.newInstance();
		IQAntArray opponentCrit = QAntArray.newInstance();
		ChaosCastlePower opponentObj = stage.getOpponent();
		List<HeroClass> opponentHeroes = opponentObj.getHeroes();
		IQAntArray opponentHeroArr = QAntArray.newInstance();
		for (HeroClass hero : opponentHeroes) {
			opponentHeroArr.addQAntObject(QAntObject.newFromObject(hero));

			IQAntObject obj = QAntObject.newInstance();
			obj.putLong("id", hero.getId());
			obj.putByteArray("crit", hero.genCrit(1));
			opponentCrit.addQAntObject(obj);
		}

		opponentObj.getBattleTeam();
		opponent.putQAntArray("heroes", opponentHeroArr);
		opponent.putLongArray("formation", opponentObj.getFormation());
		opponent.putInt("leaderIndex", opponentObj.getTeamLeader());
		opponent.putQAntArray("crit", opponentCrit);
		params.putQAntObject("opponent", opponent);

		send(ExtensionEvent.CMD_CHAOS_CASTLE, params, user);
	}


	private void processUpdateBattleTeam(QAntUser user, IQAntObject params) {
		// ChaosCastleInfo chaosCastleInfo =
		// chaosCastleManager.getChaosCastleInfo(user.getName());

		// TODO check có sở hữu các hero này ko
		// chaosCastleInfo.setBattleTeam(params.getLongArray("heroes"));
		// chaosCastleManager.saveChaosCastleInfo(chaosCastleInfo);
	}


	private void processFinish(QAntUser user, IQAntObject params) {
		List<HeroConsumeableItem> useItems = null;
		IQAntArray itemArr = params.getCASArray("use_items");
		if (itemArr != null && itemArr.size() > 0) {
			int size = itemArr.size();
			Map<Long, Integer> itemIds = new HashMap<>();
			for (int i = 0; i < size; i++) {
				IQAntObject itemObj = itemArr.getQAntObject(i);
				itemIds.put(itemObj.getLong("id"), itemObj.getInt("no"));
			}

			useItems = heroItemManager.useItems(user.getName(), itemIds);
		}

		Boolean isWin = params.getBool("win");
		if (!isWin) {
			QAntObject response = QAntObject.newInstance();
			if (useItems != null) {
				QAntArray itemUdpArr = QAntArray.newInstance();
				for (HeroItem heroItem : useItems) {
					itemUdpArr.addQAntObject(QAntObject.newFromObject(heroItem));
				}
				QAntObject updateObj = QAntObject.newInstance();
				updateObj.putQAntArray("items", itemUdpArr);
				response.putQAntObject("update", updateObj);
			}

			send(ExtensionEvent.CMD_CHAOS_CASTLE, response, user);
			return;
		}

		params.putInt("code", 1);
		send(ExtensionEvent.CMD_CHAOS_CASTLE, params, user);
	}


	private void processNextStage(QAntUser user, ChaosCastleStage stage) {
		IQAntObject response = QAntObject.newInstance();
		response.putQAntObject("stage", QAntObject.newFromObject(chaosCastleManager.getNextStage(stage)));
		response.putInt("act", UNLOCK_STAGE);
		send(ExtensionEvent.CMD_CHAOS_CASTLE, response, user);
	}


	private void processReward(IQAntObject params, QAntUser user, ChaosCastleStage unlockStage) {
		QAntObject reward = QAntObject.newInstance();

		ChaosStageBase stage = ChaosCastleConfig.getInstance().getStage(unlockStage.getStageIndex());
		String rewardItems = stage.getFullReward(unlockStage.getRank());
		ItemConfig instance = ItemConfig.getInstance();
		reward.putQAntArray("items", instance.buildShortItemInfo(rewardItems));
		params.putQAntObject("reward", reward);

		String chestRewardString = stage.getChestRewardString();
		List<HeroItem> addItems = heroItemManager.addItems(user.getName(), chestRewardString);
		QAntObject updateObj = QAntObject.newInstance();
		QAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}
		updateObj.putQAntArray("items", itemArr);
		params.putQAntObject("update", updateObj);

		notifyAssetChange(user, stage, unlockStage.getRank());
	}


	private void notifyAssetChange(QAntUser user, ChaosStageBase stage, String rank) {
		GameHero gameHero = gameHeroManager.getHero(user.getName());
		IQAntObject assets = QAntObject.newInstance();

		gameHero.incrZen(stage.getTotalRewardZen(rank));
		assets.putLong("zen", gameHero.getZen());
		assets.putLong("chaos_point", stage.getChaosPointReward());

		send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, assets, user);
		gameHeroManager.update(gameHero);
	}


	private void processJoinCastle(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();
		ChaosCastleInfo chaosCastleInfo = chaosCastleManager.getChaosCastleInfo(gameHeroId,
				UserHelper.getGameHeroName(user));

		params.putQAntObject("chaos_info", QAntObject.newFromObject(chaosCastleInfo));
		ChaosCastleStage stage = chaosCastleManager.findChaosCastleStage(gameHeroId);
		params.putQAntObject("stage", QAntObject.newFromObject(stage));
		ChaosStageBase chaosStage = ChaosCastleConfig.getInstance().getStage(stage.getStageIndex());
		String fullReward = chaosStage.getFullReward(stage.getRank());
		QAntArray reward = ItemConfig.getInstance().buildShortItemInfo(fullReward);
		params.putQAntArray("reward", reward);
		send(ExtensionEvent.CMD_CHAOS_CASTLE, params, user);
	}

}
