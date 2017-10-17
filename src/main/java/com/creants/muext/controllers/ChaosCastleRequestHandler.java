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
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosStageBase;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.managers.ChaosCastleManager;
import com.creants.muext.managers.GameHeroManager;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.util.UserHelper;

/**
 * @author LamHM
 *
 */
public class ChaosCastleRequestHandler extends BaseClientRequestHandler {
	private static final int JOIN = 1;
	private static final int FINISH = 2;
	private static final int UPDATE_BATTLE_TEAM = 3;
	private static final int UNLOCK_STAGE = 4;

	private ChaosCastleManager chaosCastleManager;
	private HeroItemManager heroItemManager;
	private GameHeroManager gameHeroManager;


	public ChaosCastleRequestHandler() {
		chaosCastleManager = Creants2XApplication.getBean(ChaosCastleManager.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
		gameHeroManager = Creants2XApplication.getBean(GameHeroManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		if (action == null) {
			action = JOIN;
		}

		switch (action) {
			case JOIN:
				processJoinCastle(user, params);
				break;
			case FINISH:
				processFinish(user, params);
				break;
			case UPDATE_BATTLE_TEAM:
				processUpdateBattleTeam(user, params);
				break;
		}

	}


	private void processUpdateBattleTeam(QAntUser user, IQAntObject params) {
		// ChaosCastleInfo chaosCastleInfo =
		// chaosCastleManager.getChaosCastleInfo(user.getName());

		// TODO check có sở hữu các hero này ko
		// chaosCastleInfo.setBattleTeam(params.getLongArray("heroes"));
		// chaosCastleManager.saveChaosCastleInfo(chaosCastleInfo);
	}


	private void processFinish(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();

		List<HeroConsumeableItem> useItems = null;
		IQAntArray itemArr = params.getCASArray("use_items");
		if (itemArr != null && itemArr.size() > 0) {
			int size = itemArr.size();
			Map<Long, Integer> itemIds = new HashMap<>();
			for (int i = 0; i < size; i++) {
				IQAntObject itemObj = itemArr.getQAntObject(i);
				itemIds.put(itemObj.getLong("id"), itemObj.getInt("no"));
			}

			useItems = heroItemManager.useItems(gameHeroId, itemIds);
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

		ChaosCastleStage stage = chaosCastleManager.getChaosCastleStage(gameHeroId);
		processReward(params, user, stage);
		send(ExtensionEvent.CMD_CHAOS_CASTLE, params, user);

		processNextStage(user, stage);
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
		send(ExtensionEvent.CMD_CHAOS_CASTLE, params, user);
	}

}