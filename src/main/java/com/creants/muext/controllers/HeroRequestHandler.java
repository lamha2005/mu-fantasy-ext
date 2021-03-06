package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.GameConfig;
import com.creants.muext.config.HeroClassConfig;
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroBase;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Team;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.entities.upgrade.EvolveHero;
import com.creants.muext.entities.upgrade.UpgradeSystem;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.GameHeroManager;
import com.creants.muext.managers.HeroClassManager;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.MessageFactory;

import de.jkeylockmanager.manager.KeyLockManager;
import de.jkeylockmanager.manager.KeyLockManagers;
import de.jkeylockmanager.manager.LockCallback;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class HeroRequestHandler extends BaseClientRequestHandler {
	private static final int UPGRADE_FEE = 100;
	private static final int UPGRADE_LEVEL = 1;
	private static final int UPGRADE_RANK = 2;
	private static final int HERO_DETAIL = 3;
	private static final int NEXT_RANK = 4;
	private static final int HERO_LIST = 10;
	private static final int HERO_PAGE = 11;

	private HeroItemManager heroItemManager;
	private HeroClassManager heroClassManager;
	private GameHeroManager gameHeroManager;
	private BattleTeamRepository battleTeamRep;
	private KeyLockManager keyLockManager;
	private static final HeroClassConfig heroConfig = HeroClassConfig.getInstance();


	public HeroRequestHandler() {
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
		heroClassManager = Creants2XApplication.getBean(HeroClassManager.class);
		gameHeroManager = Creants2XApplication.getBean(GameHeroManager.class);
		battleTeamRep = Creants2XApplication.getBean(BattleTeamRepository.class);
		keyLockManager = KeyLockManagers.newLock();
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		switch (action) {
			case UPGRADE_LEVEL:
				upgradeLevel(user.getName(), user, params);
				break;
			case UPGRADE_RANK:
				upgradeRank(user, params);
				break;
			case HERO_DETAIL:
				getHeroDetail(user, params);
				break;
			case HERO_LIST:
				getHeroList(user, params);
				break;
			case HERO_PAGE:
				getHeroPage(user, params);
				break;
			case NEXT_RANK:
				getNextRank(user, params);
				break;

			default:
				getHeroList(user, params);
				break;
		}
	}


	private void getNextRank(QAntUser user, IQAntObject params) {
		Long heroId = params.getLong("heroId");
		if (heroId == null) {
			responseError(user, GameErrorCode.CAN_NOT_FIND_HERO, NEXT_RANK);
			return;
		}

		HeroClass hero = heroClassManager.findHeroById(heroId);
		if (hero == null || !hero.getGameHeroId().equals(user.getName())) {
			responseError(user, GameErrorCode.CAN_NOT_UPGRADE_ITEM, NEXT_RANK);
			warn("upgradeRank hero is not owned: " + user.getName() + "/heroId:" + heroId);
			return;
		}

		HeroBase heroBase = hero.getHeroBase();
		if (heroBase.getEvolveTo() == null) {
			responseError(user, GameErrorCode.CAN_NOT_UPGRADE_ITEM, NEXT_RANK);
			warn("upgradeRank hero can not upgrade: " + user.getName() + "/heroId:" + heroId + "/heroIndex:"
					+ heroBase.getIndex());
			return;
		}

		Integer evolveTo = heroBase.getEvolveTo();
		HeroBase newHeroBase = heroConfig.getHeroBase(evolveTo);
		hero.setIndex(newHeroBase.getIndex());
		hero.setRank(hero.getRank() + 1);
		hero.setSkillList(heroBase.getSkillList());
		hero.setHeroBase(newHeroBase);
		params.putQAntObject("new_hero", QAntObject.newFromObject(hero));
		send(ExtensionEvent.CMD_HERO, params, user);
	}


	private void getHeroPage(QAntUser user, IQAntObject params) {
		Integer page = params.getInt("page");
		if (page == null) {
			page = 1;
		}
		Page<HeroClass> heroPage = heroClassManager.findHeroesByGameHeroId(user.getName(), page);
		params.putInt("max_page", heroPage.getTotalPages());

		List<HeroClass> heroes = heroPage.getContent();
		if (heroes != null && heroes.size() > 0) {
			IQAntArray arr = QAntArray.newInstance();
			for (HeroClass heroClass : heroes) {
				heroClass.setHeroBase(heroConfig.getHeroBase(heroClass.getIndex()));
				arr.addQAntObject(QAntObject.newFromObject(heroClass));
			}
			params.putQAntArray("heroes", arr);
		}

		send(ExtensionEvent.CMD_HERO, params, user);
	}


	private void getHeroList(QAntUser user, IQAntObject params) {
		params = QAntObject.newInstance();
		String gameHeroId = user.getName();
		List<HeroClass> heroes = heroClassManager.findHeroesByGameHeroId(gameHeroId);

		IQAntArray arr = QAntArray.newInstance();
		for (HeroClass heroClass : heroes) {
			arr.addQAntObject(QAntObject.newFromObject(heroClass));
		}
		params.putQAntArray("heroes", arr);

		BattleTeam battleTeam = battleTeamRep.findOne(gameHeroId);
		List<Team> teamList = battleTeam.getTeamList();
		IQAntArray teamArr = QAntArray.newInstance();
		for (Team team : teamList) {
			QAntObject teamObj = QAntObject.newInstance();
			int index = team.getIndex();
			teamObj.putInt("index", index > 0 ? index : -1);
			teamObj.putUtfString("name", team.getName());
			teamObj.putLongArray("heroes", new ArrayList<Long>(Arrays.asList(team.getHeroes())));
			teamObj.putInt("leader_index", team.getLeaderIndex());
			teamArr.addQAntObject(teamObj);
		}
		params.putQAntArray("teams", teamArr);
		params.putInt("act", HERO_LIST);

		send(ExtensionEvent.CMD_HERO, params, user);
	}


	private void getHeroDetail(QAntUser user, IQAntObject params) {
		Long heroId = params.getLong("heroId");
		if (heroId == null) {
			responseError(user, GameErrorCode.LACK_OF_INFOMATION, HERO_DETAIL);
			return;
		}

		HeroClass hero = heroClassManager.findHeroById(heroId);
		if (hero == null) {
			responseError(user, GameErrorCode.CAN_NOT_FIND_HERO, HERO_DETAIL);
			return;
		}

		Integer level = params.getInt("lvl");
		if (level == null) {
			level = hero.getLevel();
		}

		hero = new HeroClass(hero.getHeroBase(), level);
		IQAntObject nextLevelInfo = QAntObject.newInstance();
		nextLevelInfo.putInt("atk", hero.getAtk());
		nextLevelInfo.putInt("hp", hero.getHp());
		nextLevelInfo.putInt("def", hero.getDef());
		nextLevelInfo.putInt("rec", hero.getRec());
		nextLevelInfo.putInt("max_exp", hero.getMaxExp());
		nextLevelInfo.putLong("id", heroId);
		params.putQAntObject("next_level", nextLevelInfo);

		send(ExtensionEvent.CMD_HERO, params, user);
	}


	private void upgradeRank(QAntUser user, IQAntObject params) {
		keyLockManager.executeLocked(user.getName(), new LockCallback() {

			@Override
			public void doInLock() {
				Long heroId = params.getLong("heroId");
				if (heroId == null) {
					responseError(user, GameErrorCode.CAN_NOT_FIND_HERO, UPGRADE_RANK);
					return;
				}

				HeroClass hero = heroClassManager.findHeroById(heroId);
				String gameHeroId = user.getName();
				if (hero == null || !hero.getGameHeroId().equals(gameHeroId)) {
					responseError(user, GameErrorCode.CAN_NOT_UPGRADE_HERO, UPGRADE_RANK);
					warn("upgradeRank hero is not owned: " + gameHeroId + "/heroId:" + heroId);
					return;
				}

				HeroBase heroBase = hero.getHeroBase();
				if (heroBase.getEvolveTo() == null) {
					responseError(user, GameErrorCode.CAN_NOT_UPGRADE_HERO, UPGRADE_RANK);
					warn("upgradeRank hero can not upgrade: " + gameHeroId + "/heroId:" + heroId);
					return;
				}

				GameHero gameHero = gameHeroManager.getHero(gameHeroId);

				// TODO validate nguyên liệu
				String evolveId = UpgradeSystem.genEvolveKey(hero.getClassGroup(), hero.nextRank());
				EvolveHero evolveHero = GameConfig.getInstance().getEvolveHero(evolveId);
				if (evolveHero == null) {
					responseError(user, GameErrorCode.CAN_NOT_UPGRADE_HERO, UPGRADE_RANK);
					warn("upgradeRank hero can not upgrade: " + gameHeroId + "/evolveId:" + evolveId);
					return;
				}

				if (evolveHero.getZenCost() > gameHero.getZen()) {
					responseError(user, GameErrorCode.NOT_ENOUGH_ZEN, UPGRADE_RANK);
					warn("upgradeRank hero can not upgrade: " + gameHeroId + "/heroId:" + heroId);
					return;
				}

				IQAntArray itemArr = params.getCASArray("items");
				if (itemArr == null || itemArr.size() <= 0) {
					responseError(user, GameErrorCode.LACK_OF_INFOMATION, UPGRADE_RANK);
					warn("upgradeRank hero can not upgrade. Items empty: " + gameHeroId + "/heroId:" + heroId);
					return;
				}

				int size = itemArr.size();
				Map<Long, Integer> itemIds = new HashMap<>();
				for (int i = 0; i < size; i++) {
					IQAntObject itemObj = itemArr.getQAntObject(i);
					itemIds.put(itemObj.getLong("id"), itemObj.getInt("no"));
				}

				List<HeroConsumeableItem> useItems = heroItemManager.useItems(gameHeroId, itemIds);
				if (useItems != null) {
					QAntArray itemUdpArr = QAntArray.newInstance();
					for (HeroItem heroItem : useItems) {
						itemUdpArr.addQAntObject(QAntObject.newFromObject(heroItem));
					}
					QAntObject updateObj = QAntObject.newInstance();
					updateObj.putQAntArray("items", itemUdpArr);
					params.putQAntObject("update", updateObj);
				}

				heroClassManager.upgradeHero(hero);

				gameHero = gameHeroManager.incrZen(gameHeroId, -evolveHero.getZenCost());
				Map<String, Object> assetMap = new HashMap<>();
				assetMap.put("zen", gameHero.getZen());
				send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, MessageFactory.buildAssetsChange(assetMap), user);

				params.putInt("code", 1);
				params.putQAntObject("new_hero", QAntObject.newFromObject(hero));
				send(ExtensionEvent.CMD_HERO, params, user);
			}
		});
	}


	private void responseError(QAntUser user, GameErrorCode error, int act) {
		IQAntObject createErrorMsg = MessageFactory.createErrorMsg(ExtensionEvent.CMD_HERO, error);
		createErrorMsg.putInt("act", act);
		sendError(createErrorMsg, user);
	}


	private void warn(String message) {
		QAntTracer.warn(this.getClass(), message);
	}


	private void upgradeLevel(String gameHeroId, QAntUser user, IQAntObject params) {
		keyLockManager.executeLocked(gameHeroId, new LockCallback() {

			@Override
			public void doInLock() {
				QAntTracer.debug(HeroRequestHandler.class, "[DEBUG] upgradeLevel: " + gameHeroId);
				int fee = 0;
				int exp = 0;
				Long heroId = params.getLong("heroId");
				if (heroId == null) {
					responseError(user, GameErrorCode.CAN_NOT_FIND_HERO, UPGRADE_LEVEL);
					return;
				}

				// hero cần upgrade
				HeroClass hero = heroClassManager.findHeroById(heroId);
				if (hero == null) {
					responseError(user, GameErrorCode.CAN_NOT_FIND_HERO, UPGRADE_LEVEL);
					return;
				}

				// cắn hero
				// TODO kiểm tra mấy con hero này có nằm trong battle team ko,
				// nếu có thì cập nhật lại
				Collection<Long> consumHeroIds = params.getLongArray("heroIds");
				List<HeroClass> consumHeroes = new ArrayList<>();
				if (consumHeroIds != null && consumHeroIds.size() > 0) {
					consumHeroes = heroClassManager.findHeroes(consumHeroIds);
					if (consumHeroes.size() != consumHeroIds.size()) {
						responseError(user, GameErrorCode.CAN_NOT_FIND_HERO, UPGRADE_LEVEL);
						return;
					}

					fee = hero.getLevel() * UPGRADE_FEE;
					List<HeroEquipment> heroEquipments = new ArrayList<>();
					for (HeroClass heroClass : consumHeroes) {
						int incrExp = GameConfig.getInstance().getExpFromHero(heroClass.getRank(),
								hero.getElement().equals(heroClass.getElement()));

						if (incrExp < 0) {
							responseError(user, GameErrorCode.MATERIAL_CAN_NOT_USE, UPGRADE_LEVEL);
							warn("Can not use hero rank: " + heroClass.getRank() + "/gameHeroId:" + gameHeroId);
							return;
						}

						exp += incrExp;

						List<HeroEquipment> equipment = heroItemManager.getTakeOnEquipments(heroClass.getId());
						if (equipment != null && equipment.size() > 0) {
							heroEquipments.addAll(equipment);
						}
					}

					if (heroEquipments.size() > 0) {
						for (HeroEquipment heroItem : heroEquipments) {
							heroItem.takeOff();
						}

						heroItemManager.updateEquipments(heroEquipments);
					}
				}

				List<HeroConsumeableItem> consumeableItems = new ArrayList<>();
				IQAntArray consumeItemArr = params.getCASArray("items");
				if (consumeItemArr != null && consumeItemArr.size() > 0) {
					fee += hero.getLevel() * UPGRADE_FEE;
					Map<Long, Integer> updateMap = new HashMap<>();
					for (int i = 0; i < consumeItemArr.size(); i++) {
						IQAntObject item = consumeItemArr.getQAntObject(i);
						Long itemId = item.getLong("id");
						Integer itemNo = item.getInt("no");
						updateMap.put(itemId, itemNo);
					}

					consumeableItems = heroItemManager.getConsumeableItems(gameHeroId, updateMap.keySet());
					if (consumeableItems.size() != consumeItemArr.size()) {
						// TODO send err
						return;
					}

					for (HeroConsumeableItem heroItem : consumeableItems) {
						Integer no = updateMap.get(heroItem.getId());
						heroItem.decr(no);

						int incrExp = GameConfig.getInstance().getExpFromItem(heroItem.getIndex(),
								hero.getElement().equals(heroItem.getElement()));
						if (incrExp < 0) {
							responseError(user, GameErrorCode.MATERIAL_CAN_NOT_USE, UPGRADE_LEVEL);
							return;
						}

						exp += (incrExp * no);
					}

					// TODO xóa các item = 0
				}

				if (fee == 0) {
					responseError(user, GameErrorCode.FEE_NULL, UPGRADE_LEVEL);
					return;
				}

				GameHero gameHero = gameHeroManager.getHero(gameHeroId);
				if (gameHero.getZen() < fee) {
					responseError(user, GameErrorCode.NOT_ENOUGH_ZEN, UPGRADE_LEVEL);
					return;
				}

				if (consumHeroes.size() > 0) {
					heroClassManager.remove(consumHeroes);
				}

				if (consumeableItems.size() > 0) {
					List<HeroConsumeableItem> updateConsumeableItem = heroItemManager
							.updateConsumeableItem(consumeableItems);
					if (updateConsumeableItem != null) {
						QAntArray itemUdpArr = QAntArray.newInstance();
						for (HeroConsumeableItem heroItem : updateConsumeableItem) {
							itemUdpArr.addQAntObject(QAntObject.newFromObject(heroItem));
						}
						QAntObject updateObj = QAntObject.newInstance();
						updateObj.putQAntArray("items", itemUdpArr);
						params.putQAntObject("update", updateObj);
					}
				}

				gameHero.incrZen(-fee);
				gameHeroManager.update(gameHero);
				Map<String, Object> assetMap = new HashMap<>();
				assetMap.put("zen", gameHero.getZen());
				send(ExtensionEvent.CMD_NTF_ASSETS_CHANGE, MessageFactory.buildAssetsChange(assetMap), user);

				hero.incrExp(exp);
				heroClassManager.save(hero);

				params.putQAntObject("hero", QAntObject.newFromObject(hero));
				params.putInt("code", 1);
				send(ExtensionEvent.CMD_HERO, params, user);
				QAntTracer.debug(HeroRequestHandler.class, "[DEBUG] upgradeLevel finish: " + gameHeroId);
			}
		});

	}

}
