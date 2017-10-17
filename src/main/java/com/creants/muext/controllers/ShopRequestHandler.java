package com.creants.muext.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.config.ShopConfig;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.entities.shop.ShopPackage;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.GameHeroManager;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class ShopRequestHandler extends BaseClientRequestHandler {
	private static final ShopConfig shopConfig = ShopConfig.getInstance();
	private GameHeroManager gameHeroManager;
	private HeroItemManager heroItemManager;
	private static final int GET_PACKAGE_LIST = 1;
	private static final int BUY_ITEM = 2;


	public ShopRequestHandler() {
		gameHeroManager = Creants2XApplication.getBean(GameHeroManager.class);
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer act = params.getInt("act");
		if (act == null) {
			act = GET_PACKAGE_LIST;
		}

		switch (act) {
			case GET_PACKAGE_LIST:
				getPackageList(user, params);
				break;
			case BUY_ITEM:
				buyItem(user, params);
				break;

			default:
				break;
		}

	}


	private void getPackageList(QAntUser user, IQAntObject params) {
		// String categoryId = params.getUtfString("cat_id");
		IQAntArray arr = QAntArray.newInstance();
		List<ShopPackage> packages = shopConfig.getPackages();
		for (ShopPackage shopPackage : packages) {
			arr.addQAntObject(QAntObject.newFromObject(shopPackage));
		}

		params.putQAntArray("package_list", arr);
		send(ExtensionEvent.CMD_SHOP, params, user);
	}


	private void buyItem(QAntUser user, IQAntObject params) {
		Integer packageId = params.getInt("package_index");
		if (packageId == null) {
			// TODO send error
			QAntTracer.warn(this.getClass(), "ShopRequestHandler package_id is null");
			responseError(user, GameErrorCode.LACK_OF_INFOMATION, GET_PACKAGE_LIST);
			return;
		}

		Map<String, Object> assetMap = new HashMap<>();
		String gameHeroId = user.getName();
		GameHero hero = gameHeroManager.getHero(gameHeroId);
		ShopPackage itemPackage = shopConfig.getPackage(packageId);
		int priceItemIndex = itemPackage.getPriceItemIndex();
		int priceItemNo = itemPackage.getPriceItemNo();
		switch (priceItemIndex) {
			case ItemConfig.ZEN_INDEX:
				if (hero.getZen() < priceItemNo) {
					responseError(user, GameErrorCode.NOT_ENOUGH_ZEN, BUY_ITEM);
					return;
				}

				hero.incrZen(-priceItemNo);
				assetMap.put("zen", hero.getZen());
				break;
			case ItemConfig.BLESS_INDEX:
				if (hero.getBless() < priceItemNo) {
					responseError(user, GameErrorCode.NOT_ENOUGH_BLESS, BUY_ITEM);
					return;
				}
				hero.incrBless(-priceItemNo);
				assetMap.put("bless", hero.getBless());
				break;

			default:
				break;
		}

		List<HeroItem> addItems = heroItemManager.addItems(gameHeroId, itemPackage.getItemString());

		QAntObject updateObj = QAntObject.newInstance();
		QAntArray itemArr = QAntArray.newInstance();
		for (HeroItem heroItem : addItems) {
			if (heroItem.getItemType() != 9) {
				itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
			}
		}

		if (itemArr.size() > 0) {
			updateObj.putQAntArray("items", itemArr);
			params.putQAntObject("update", updateObj);
			send(ExtensionEvent.CMD_SHOP, params, user);
		}

		send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, MessageFactory.buildAssetsChange(assetMap), user);
		gameHeroManager.update(hero);

	}


	private void responseError(QAntUser user, GameErrorCode error, int act) {
		IQAntObject createErrorMsg = MessageFactory.createErrorMsg(ExtensionEvent.CMD_SHOP, error);
		createErrorMsg.putInt("act", act);
		sendError(createErrorMsg, user);
	}
}
