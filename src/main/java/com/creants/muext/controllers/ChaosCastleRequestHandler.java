package com.creants.muext.controllers;

import java.util.ArrayList;
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
import com.creants.muext.config.ItemConfig;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.chaos.ChaosCastleInfo;
import com.creants.muext.entities.chaos.ChaosCastleStage;
import com.creants.muext.entities.chaos.ChaosRewardPackage;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.managers.ChaosCastleManager;
import com.creants.muext.managers.HeroItemManager;

/**
 * @author LamHM
 *
 */
public class ChaosCastleRequestHandler extends BaseClientRequestHandler {
	private static final String CMD = "cmd_chaos_castle";
	private static final int JOIN = 1;
	private static final int FINISH = 2;
	private static final int UPDATE_TEAM = 3;

	private ChaosCastleManager chaoCastleManager;
	private HeroItemManager heroItemManager;


	public ChaosCastleRequestHandler() {
		chaoCastleManager = Creants2XApplication.getBean(ChaosCastleManager.class);
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

			default:
				processJoinCastle(user, params);
				break;
		}

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

			send(CMD, response, user);
			return;
		}

		ChaosCastleInfo chaosCastleInfo = chaoCastleManager.getChaosCastleInfo(gameHeroId);
		ChaosCastleStage unlockStage = chaosCastleInfo.getUnlockStage();
		processReward(user, unlockStage.getRewardPackage());
		send(CMD, params, user);
	}


	private QAntObject processReward(QAntUser user, ChaosRewardPackage rewardPackage) {
		QAntObject response = QAntObject.newInstance();
		QAntObject reward = QAntObject.newInstance();
		reward.putInt("star_no", 3);
		reward.putInt("zen", rewardPackage.getZen());
		reward.putInt("chaos_point", rewardPackage.getChaosPoint());

		// reward.putQAntArray("items",
		// ItemConfig.getInstance().splitItem(rewardPackage.getRewardString()));
		response.putQAntObject("reward", reward);

		List<HeroItem> addItems = heroItemManager.addItems(user.getName(), rewardPackage.getRewardString());
		QAntObject updateObj = QAntObject.newInstance();
		QAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}
		updateObj.putQAntArray("items", itemArr);
		response.putQAntObject("update", updateObj);
		return reward;
	}


	private void processJoinCastle(QAntUser user, IQAntObject params) {
		ChaosCastleInfo chaosCastleInfo = chaoCastleManager.getChaosCastleInfo(user.getName());
		params.putQAntObject("chaos_info", QAntObject.newFromObject(chaosCastleInfo));
		send(CMD, params, user);
	}

}
