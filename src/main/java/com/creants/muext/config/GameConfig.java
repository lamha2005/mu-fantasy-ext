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
	private static GameConfig instance;
	private Map<Integer, Float> critRateMap;


	public static GameConfig getInstance() {
		if (instance == null) {
			instance = new GameConfig();
		}
		return instance;
	}


	private GameConfig() {
		loadCritRate();
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


	public Float getCritRate(int chance) {
		Float crit = critRateMap.get(chance);
		return crit == null ? 0 : crit;
	}

}
