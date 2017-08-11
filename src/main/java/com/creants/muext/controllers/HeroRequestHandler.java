package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.creants.muext.dao.BattleTeamRepository;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.BattleTeam;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.Team;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.exception.GameErrorCode;
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
	private static final String CMD = "cmd_hero";
	private static final int UPGRADE_FEE = 100;
	private static final int UPGRADE_LEVEL = 1;
	private static final int UPGRADE_RANK = 2;
	private static final int HERO_DETAIL = 3;
	private static final int HERO_LIST = 10;

	private GameHeroRepository gameHeroRepository;
	private HeroItemManager heroItemManager;
	private HeroClassManager heroClassManager;
	private BattleTeamRepository battleTeamRep;
	private KeyLockManager keyLockManager;


	public HeroRequestHandler() {
		heroItemManager = Creants2XApplication.getBean(HeroItemManager.class);
		heroClassManager = Creants2XApplication.getBean(HeroClassManager.class);
		gameHeroRepository = Creants2XApplication.getBean(GameHeroRepository.class);
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

			default:
				getHeroList(user, params);
				break;
		}
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
			teamArr.addQAntObject(teamObj);
		}
		params.putQAntArray("teams", teamArr);
		params.putInt("act", HERO_LIST);

		send(CMD, params, user);
	}


	private void getHeroDetail(QAntUser user, IQAntObject params) {
		Long heroId = params.getLong("heroId");
		if (heroId == null) {
			sendError(MessageFactory.createErrorMsg("cmd_hero", GameErrorCode.LACK_OF_INFOMATION), user);
			return;
		}

		HeroClass hero = heroClassManager.findHeroById(heroId);
		if (hero == null) {
			sendError(MessageFactory.createErrorMsg("cmd_hero", GameErrorCode.CAN_NOT_FIND_HERO), user);
			return;
		}

		hero.levelUp(1);
		IQAntObject response = QAntObject.newInstance();
		IQAntObject nextLevelInfo = QAntObject.newInstance();
		nextLevelInfo.putInt("atk", hero.getAtk());
		nextLevelInfo.putInt("hp", hero.getHp());
		nextLevelInfo.putInt("def", hero.getDef());
		nextLevelInfo.putInt("rec", hero.getRec());
		nextLevelInfo.putLong("heroId", heroId);
		response.putQAntObject("next_level", nextLevelInfo);
		response.putInt("act", HERO_DETAIL);
		send("cmd_hero", response, user);
	}


	private void upgradeRank(QAntUser user, IQAntObject params) {

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
					sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.CAN_NOT_FIND_HERO), user);
					return;
				}

				// hero cần upgrade
				HeroClass hero = heroClassManager.findHeroById(heroId);
				if (hero == null) {
					sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.CAN_NOT_FIND_HERO), user);
					return;
				}

				Collection<Long> heroIds = params.getLongArray("heroIds");
				List<HeroClass> heroes = new ArrayList<>();
				if (heroIds != null && heroIds.size() > 0) {
					heroes = heroClassManager.findHeroes(heroIds);
					if (heroes.size() != heroIds.size()) {
						sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.CAN_NOT_FIND_HERO), user);
						return;
					}

					fee = hero.getLevel() * UPGRADE_FEE;
					List<HeroItem> heroItems = new ArrayList<>();
					for (HeroClass heroClass : heroes) {
						int incrExp = GameConfig.getInstance().getExpFromHero(heroClass.getRank(),
								hero.getElement().equals(heroClass.getElement()));

						if (incrExp < 0) {
							QAntTracer.warn(HeroRequestHandler.class,
									"Can not use hero rank: " + heroClass.getRank() + "/gameHeroId:" + gameHeroId);
							sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.MATERIAL_CAN_NOT_USE), user);
							return;
						}

						exp += incrExp;

						List<HeroItem> items = heroItemManager.getItems(gameHeroId);
						if (items != null && items.size() > 0) {
							heroItems.addAll(items);
						}
					}
					
					if(heroItems.size() > 0){
						for (HeroItem heroItem : heroItems) {
							heroItem.takeOff();
						}
						
						heroItemManager.updateItem(heroItems);
					}
				}

				List<HeroItem> removeItems = new ArrayList<>();
				List<HeroItem> heroItems = new ArrayList<>();
				IQAntArray itemArr = params.getCASArray("items");
				if (itemArr != null && itemArr.size() > 0) {
					fee += hero.getLevel() * UPGRADE_FEE;
					Map<Long, Integer> updateMap = new HashMap<>();
					for (int i = 0; i < itemArr.size(); i++) {
						IQAntObject item = itemArr.getQAntObject(i);
						Long itemId = item.getLong("id");
						Integer itemNo = item.getInt("no");
						updateMap.put(itemId, itemNo);
					}

					heroItems = heroItemManager.getItems(updateMap.keySet());
					if (heroItems.size() != itemArr.size()) {
						// TODO send err
						return;
					}

					for (HeroItem heroItem : heroItems) {
						Integer no = updateMap.get(heroItem.getId());
						heroItem.decr(no);
						if (heroItem.getNo() <= 0) {
							removeItems.add(heroItem);
						}

						int incrExp = GameConfig.getInstance().getExpFromItem(heroItem.getIndex(),
								hero.getElement().equals(heroItem.getElement()));
						if (incrExp < 0) {
							sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.MATERIAL_CAN_NOT_USE), user);
							return;
						}

						exp += (incrExp * no);
					}

				}

				if (fee == 0) {
					sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.FEE_NULL), user);
					return;
				}

				if (heroes.size() > 0) {
					heroClassManager.remove(heroes);
				}

				if (removeItems.size() > 0) {
					heroItemManager.removeItems(removeItems);
				}

				if (heroItems.removeAll(removeItems)) {
					heroItemManager.updateItem(heroItems);
				}

				GameHero gameHero = gameHeroRepository.findOne(gameHeroId);
				gameHero.incrZen(-fee);
				gameHeroRepository.save(gameHero);

				hero.incrExp(exp);
				heroClassManager.save(hero);

				IQAntObject response = QAntObject.newInstance();
				response.putInt("act", UPGRADE_LEVEL);
				response.putQAntObject("hero", QAntObject.newFromObject(hero));
				send(CMD, response, user);
				QAntTracer.debug(HeroRequestHandler.class, "[DEBUG] upgradeLevel finish: " + gameHeroId);
			}
		});

	}

}
