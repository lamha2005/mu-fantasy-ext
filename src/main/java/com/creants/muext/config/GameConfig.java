package com.creants.muext.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author LamHM
 *
 */
public class GameConfig {
	private static final String CRIT_RATE = "resources/crit_dam_rate.json";
	private static final String HERO_EXP = "resources/hero_exp.json";
	private static final String ACC_EXP = "resources/acc_exp.json";
	private static final String UPGRADE_SYSTEM = "resources/upgrade_system.json";
	private static GameConfig instance;
	private Map<Integer, Float> critRateMap;
	private Map<Integer, Integer> heroExpMap;
	private Map<Integer, Integer> accExpMap;

	private Map<Integer, Integer[]> heroMaterialMap;
	private Map<Integer, Integer[]> itemMaterialMap;


	public static GameConfig getInstance() {
		if (instance == null) {
			instance = new GameConfig();
		}
		return instance;
	}


	private GameConfig() {
		loadCritRate();
		loadHeroExp();
		loadAccountExp();
		loadUpgradeSystem();
	}


	private void loadCritRate() {
		try {
			critRateMap = new HashMap<Integer, Float>();
			ObjectMapper mapper = new ObjectMapper();
			critRateMap = mapper.readValue(new File(CRIT_RATE), new TypeReference<Map<Integer, Float>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void loadHeroExp() {
		try {
			heroExpMap = new HashMap<Integer, Integer>();
			ObjectMapper mapper = new ObjectMapper();
			heroExpMap = mapper.readValue(new File(HERO_EXP), new TypeReference<Map<Integer, Integer>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void loadAccountExp() {
		try {
			accExpMap = new HashMap<Integer, Integer>();
			ObjectMapper mapper = new ObjectMapper();
			accExpMap = mapper.readValue(new File(ACC_EXP), new TypeReference<Map<Integer, Integer>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	private void loadUpgradeSystem() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> value = mapper.readValue(new File(UPGRADE_SYSTEM),
					new TypeReference<Map<String, Object>>() {
					});

			heroMaterialMap = (Map<Integer, Integer[]>) value.get("material_hero");
			itemMaterialMap = (Map<Integer, Integer[]>) value.get("material_item");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public int getExpFromHero(int rank, boolean sameElement) {
		Integer[] valueArr = heroMaterialMap.get(rank);
		if (valueArr == null) {
			return -1;
		}

		return valueArr[sameElement ? 0 : 1];
	}


	public int getExpFromItem(int itemIndex, boolean sameElement) {
		Integer[] valueArr = itemMaterialMap.get(itemIndex);
		if (valueArr == null) {
			return -1;
		}

		return valueArr[sameElement ? 0 : 1];
	}


	public Float getCritRate(int chance) {
		Float crit = critRateMap.get(chance);
		return crit == null ? 0 : crit;
	}


	public int getMaxExp(int level) {
		Integer maxExp = heroExpMap.get(level);
		return maxExp == null ? Integer.MAX_VALUE : maxExp;
	}


	public int getAccMaxExp(int level) {
		Integer maxExp = accExpMap.get(level);
		return maxExp == null ? Integer.MAX_VALUE : maxExp;
	}

}
