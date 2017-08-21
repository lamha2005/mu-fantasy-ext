package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.dao.GiftEventRepository;
import com.creants.muext.entities.event.GiftEventBase;
import com.creants.muext.entities.event.GiftInfo;
import com.creants.muext.entities.event.HeroGift;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.om.ItemPackageInfo;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class RankRequestHandler extends BaseClientRequestHandler {
	private static final String CMD = "cmd_gift_events";
	private static final int GET_CATEGORY_EVENT = 1;
	private static final int CLAIM_GIFT_PACKAGE = 2;
	private GiftEventRepository giftRepository;
	private HeroItemManager itemManager;


	public RankRequestHandler() {
		giftRepository = Creants2XApplication.getBean(GiftEventRepository.class);
		itemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer revision = params.getInt("rvs");
		if (revision == null) {
			sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.LACK_OF_INFOMATION), user);
			return;
		}

		Integer action = params.getInt("act");
		if (action == null) {
			action = 1;
		}

		switch (action) {
			case GET_CATEGORY_EVENT:
				getCategoryEvent(user);
				break;
			case CLAIM_GIFT_PACKAGE:
				processClaimGift(user, params);
				break;

			default:
				getCategoryEvent(user);
				break;
		}

		if (revision != GiftEventConfig.getInstance().getRevison()) {

		}
	}


	private void processClaimGift(QAntUser user, IQAntObject params) {
		Integer giftIndex = params.getInt("giftIndex");
		HeroGift heroGift = giftRepository.findOne(user.getName());
		GiftInfo gift = heroGift.getGift(giftIndex);
		if (gift == null) {
			sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.LACK_OF_INFOMATION), user);
			return;
		}

		List<Integer> claimList = gift.getClaim();
		Integer packageIndex = params.getInt("packageIndex");

		if (claimList.size() <= 0 || !claimList.contains(packageIndex)) {
			sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.LACK_OF_INFOMATION), user);
			return;
		}

		GiftEventBase event = GiftEventConfig.getInstance().getEvent(giftIndex);
		ItemPackageInfo itemPackage = event.getPackage(giftIndex);

		List<HeroItem> addItems = itemManager.addItems(user.getName(), itemPackage.getItemListString());
		IQAntObject response = QAntObject.newInstance();
		response.putInt("act", CLAIM_GIFT_PACKAGE);
		IQAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
		}
		response.putQAntArray("items", itemArr);
		send(CMD, response, user);

		claimList.remove(packageIndex);
		giftRepository.save(heroGift);
	}


	private void getCategoryEvent(QAntUser user) {
		List<GiftEventBase> events = GiftEventConfig.getInstance().getEvents();

		IQAntObject response = QAntObject.newInstance();
		IQAntArray eventArr = QAntArray.newInstance();
		for (GiftEventBase giftEventBase : events) {
			eventArr.addQAntObject(QAntObject.newFromObject(giftEventBase));
		}

		response.putQAntArray(CMD, eventArr);
		response.putInt("act", GET_CATEGORY_EVENT);
		send(CMD, response, user);
	}

}
