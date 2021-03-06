package com.creants.muext.controllers;

import java.util.List;

import org.springframework.data.domain.Page;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.MessageFactory;

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
	private static final int ITEM_PAGE = 11;

	private HeroItemManager heroItemManager;
	private static final ItemConfig itemConfig = ItemConfig.getInstance();


	public ItemRequestHandler() {
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		if (action == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.LACK_OF_INFOMATION),
					user);
			return;
		}
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
			case ITEM_PAGE:
				getItemPage(user, params);
				break;

			default:
				break;
		}
	}


	private void getItemPage(QAntUser user, IQAntObject params) {
		Integer page = params.getInt("page");
		if (page == null) {
			page = 1;
		}
		Page<HeroItem> itemPage = heroItemManager.getItems(user.getName(), page);
		params.putInt("max_page", itemPage.getTotalPages());

		List<HeroItem> items = itemPage.getContent();
		if (items != null && items.size() > 0) {
			IQAntArray arr = QAntArray.newInstance();
			for (HeroItem item : items) {
				item.setItemBase(itemConfig.getItem(item.getIndex()));
				arr.addQAntObject(QAntObject.newFromObject(item));
			}
			params.putQAntArray("items", arr);
		}

		send(ExtensionEvent.CMD_ITEM_REQ, params, user);
	}


	private void upgradeItem(QAntUser user, IQAntObject params) {
		Long itemId = params.getLong("id");
		if (itemId == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.LACK_OF_INFOMATION),
					user);
			return;
		}

		HeroItem item = heroItemManager.getEquipment(itemId, user.getName());
		if (item == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.NOT_EXIST_ITEM), user);
			return;
		}

		boolean isEquipment = ItemConfig.getInstance().isEquipment(item.getIndex());
		if (!isEquipment) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.CAN_NOT_UPGRADE_ITEM),
					user);
			return;
		}

	}


	private void takeOn(QAntUser user, IQAntObject params) {
		// TODO kiem tra hero nay co phai cua user nay ko
		Long heroId = params.getLong("heroId");
		Long itemId = params.getLong("itemId");
		Integer slot = params.getInt("slotIndex");
		if (heroId == null || itemId == null || slot == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.LACK_OF_INFOMATION),
					user);
			return;
		}

		HeroEquipment item = heroItemManager.getEquipment(itemId, user.getName());
		if (item == null || item.getHeroId() != null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.ITEM_IS_USING), user);
			return;
		}

		QAntObject response = QAntObject.newInstance();
		response.putInt("code", 1);
		response.putInt("act", TAKE_ON);
		response.putLong("heroId", heroId);
		HeroEquipment equipment = heroItemManager.getEquipmentBySlot(slot, heroId);
		if (equipment != null) {
			heroItemManager.takeOff(equipment);
			response.putLong("tk_off", equipment.getId());
		}
		response.putLong("slotIndex", slot);
		response.putLong("itemId", itemId);
		heroItemManager.takeOn(item, heroId, slot);

		send(ExtensionEvent.CMD_ITEM_REQ, response, user);
	}


	private void takeOff(QAntUser user, IQAntObject params) {
		Long heroId = params.getLong("heroId");
		Long itemId = params.getLong("itemId");
		if (heroId == null || itemId == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.LACK_OF_INFOMATION),
					user);
			return;
		}

		// kết hợp itemId với gameHeroId 1 lần
		HeroEquipment equipment = heroItemManager.getEquipment(itemId, user.getName(), heroId);
		if (equipment == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_ITEM_REQ, GameErrorCode.NOT_EXIST_ITEM), user);
			return;
		}

		QAntObject response = QAntObject.newInstance();
		response.putInt("code", 1);
		response.putInt("act", TAKE_OFF);
		response.putLong("heroId", heroId);
		response.putLong("itemId", itemId);
		response.putLong("slotIndex", equipment.getSlotIndex());

		heroItemManager.takeOff(equipment);

		send(ExtensionEvent.CMD_ITEM_REQ, response, user);
	}


	private void consumeItem(QAntUser user, IQAntObject params) {

	}


	private void sellItem(QAntUser user, IQAntObject params) {

	}
}
