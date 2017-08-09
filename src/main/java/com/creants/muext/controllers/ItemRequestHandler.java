package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.managers.HeroItemManager;

/**
 * @author LamHM
 *
 */
public class ItemRequestHandler extends BaseClientRequestHandler {
	private static final int TAKE_ON = 1;
	private static final int TAKE_OFF = 2;
	private static final int CONSUME_ITEM = 3;
	private static final int SELL_ITEM = 4;
	private static final int UPGRADE_ITEM = 5;

	private HeroItemManager heroItemManager;


	public ItemRequestHandler() {
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		switch (action) {
			case TAKE_ON:
				takeOn(user, params);
				break;
			case TAKE_OFF:
				takeOff(user, params);
				break;
			case CONSUME_ITEM:
				consumeItem(user, params);
				break;
			case SELL_ITEM:
				sellItem(user, params);
				break;
			case UPGRADE_ITEM:
				upgradeItem(user, params);
				break;

			default:
				getItem(user, params);
				break;
		}
	}


	private void upgradeItem(QAntUser user, IQAntObject params) {

	}


	private void takeOn(QAntUser user, IQAntObject params) {
		Long heroId = params.getLong("hero_id");
		Integer itemIndex = params.getInt("item_index");
		Integer slot = params.getInt("slot_index");
		// TODO validate

	}


	private void takeOff(QAntUser user, IQAntObject params) {

	}


	private void consumeItem(QAntUser user, IQAntObject params) {

	}


	private void sellItem(QAntUser user, IQAntObject params) {

	}


	private void getItem(QAntUser user, IQAntObject params) {
		List<HeroItem> items = heroItemManager.getItems(user.getName());
		if (items == null) {
			send("cmd_get_items", null, user);
			return;
		}

		QAntArray qantArr = QAntArray.newInstance();
		for (HeroItem heroItem : items) {
			qantArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}

		params = QAntObject.newInstance();
		params.putQAntArray("items", qantArr);

		send("cmd_get_items", params, user);
	}
}
