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
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosStageBase;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.managers.ChaosCastleManager;
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

	private ChaosCastleManager chaosCastleManager;
	private HeroItemManager heroItemManager;


	public ChaosCastleRequestHandler() {
		chaosCastleManager = Creants2XApplication.getBean(ChaosCastleManager.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
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

		params.removeElement("use_items");
		ChaosCastleStage stage = chaosCastleManager.getChaosCastleStage(user.getName());
		processReward(params, user, stage);
		processNextStage(user, stage);
		send(ExtensionEvent.CMD_CHAOS_CASTLE, params, user);
	}


	private void processNextStage(QAntUser user, ChaosCastleStage stage) {
		QAntObject response = QAntObject.newInstance();
		response.putInt("type", 2);
		IQAntObject data = QAntObject.newInstance();
		data.putQAntObject("next_stage", QAntObject.newFromObject(chaosCastleManager.getNextStage(stage)));
		data.putUtfString("rank", stage.getRank());
		response.putQAntObject("data", data);
		send(ExtensionEvent.CMD_NTF_UNLOCK, response, user);
	}


	private void processReward(IQAntObject params, QAntUser user, ChaosCastleStage unlockStage) {
		QAntObject reward = QAntObject.newInstance();

		ChaosStageBase stage = ChaosCastleConfig.getInstance().getStage(unlockStage.getStageIndex());
		String rewardItems = stage.getFullReward(unlockStage.getRank());
		ItemConfig instance = ItemConfig.getInstance();
		reward.putQAntArray("items", instance.buildShortItemInfo(rewardItems));
		params.putQAntObject("reward", reward);

		List<HeroItem> addItems = heroItemManager.addItems(user.getName(), rewardItems);
		QAntObject updateObj = QAntObject.newInstance();
		QAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}
		updateObj.putQAntArray("items", itemArr);
		params.putQAntObject("update", updateObj);
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
