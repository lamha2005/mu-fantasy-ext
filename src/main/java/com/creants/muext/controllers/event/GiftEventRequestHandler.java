package com.creants.muext.controllers.event;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.controllers.ExtensionEvent;
import com.creants.muext.dao.GiftEventRepository;
import com.creants.muext.entities.event.GiftEventBase;
import com.creants.muext.entities.event.GiftInfo;
import com.creants.muext.entities.event.HeroGift;
import com.creants.muext.entities.ext.ShortItemExt;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.om.ItemPackageInfo;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class GiftEventRequestHandler extends BaseClientRequestHandler {
	private static final int GET_CATEGORY_EVENT = 1;
	private static final int CLAIM_GIFT_PACKAGE = 2;
	private GiftEventRepository giftRepository;
	private HeroItemManager itemManager;


	public GiftEventRequestHandler() {
		giftRepository = Creants2XApplication.getBean(GiftEventRepository.class);
		itemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer revision = params.getInt("rvs");
		if (revision == null) {
			revision = -1;
		}

		Integer action = params.getInt("act");
		if (action == null) {
			action = GET_CATEGORY_EVENT;
		}

		switch (action) {
			case GET_CATEGORY_EVENT:
				getCategoryEvent(user, revision);
				break;
			case CLAIM_GIFT_PACKAGE:
				processClaimGift(user, params);
				break;

			default:
				getCategoryEvent(user, revision);
				break;
		}

	}


	private void processClaimGift(QAntUser user, IQAntObject params) {
		params.removeElement("rvs");
		Integer giftIndex = params.getInt("giftIndex");
		HeroGift heroGift = giftRepository.findOne(user.getName());
		GiftInfo gift = heroGift.getGift(giftIndex);
		if (gift == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_GIFT_EVENTS, GameErrorCode.LACK_OF_INFOMATION),
					user);
			return;
		}

		List<Integer> claimList = gift.getClaim();
		Integer packageIndex = params.getInt("packageIndex");

		if (claimList.size() <= 0 || !claimList.contains(packageIndex)) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_GIFT_EVENTS, GameErrorCode.LACK_OF_INFOMATION),
					user);
			return;
		}
		gift.setLastUpdateTime(System.currentTimeMillis());

		GiftEventBase event = GiftEventConfig.getInstance().getEvent(giftIndex);
		ItemPackageInfo itemPackage = event.getPackage(packageIndex);

		String itemListString = itemPackage.getItemListString();
		List<HeroItem> addItems = itemManager.addItems(user.getName(), itemListString);
		params.putInt("act", CLAIM_GIFT_PACKAGE);
		params.putInt("code", 1);
		params.putLong("lastUpdateTime", gift.getLastUpdateTime());
		IQAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}
		params.putQAntArray("items", itemArr);

		List<ShortItemExt> itemsInfo = ItemConfig.getInstance().splitItem(itemListString);
		IQAntArray itemsInfoArr = QAntArray.newInstance();
		for (ShortItemExt sortItemExt : itemsInfo) {
			itemsInfoArr.addQAntObject(QAntObject.newFromObject(sortItemExt));
		}
		params.putQAntArray("items_info", itemsInfoArr);

		send(ExtensionEvent.CMD_GIFT_EVENTS, params, user);

		gift.addClaimed(packageIndex);
		giftRepository.save(heroGift);
	}


	private void getCategoryEvent(QAntUser user, int revision) {
		if (revision == GiftEventConfig.getInstance().getRevison()) {
			return;
		}
		List<GiftEventBase> events = GiftEventConfig.getInstance().getEvents();

		IQAntObject response = QAntObject.newInstance();
		IQAntArray eventArr = QAntArray.newInstance();
		for (GiftEventBase giftEventBase : events) {
			eventArr.addQAntObject(QAntObject.newFromObject(giftEventBase));
		}

		response.putQAntArray(ExtensionEvent.CMD_GIFT_EVENTS, eventArr);
		response.putInt("act", GET_CATEGORY_EVENT);
		response.putInt("rvs", GiftEventConfig.getInstance().getRevison());
		send(ExtensionEvent.CMD_GIFT_EVENTS, response, user);
	}

}
