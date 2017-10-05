package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.upgrade.EvolveHero;
import com.creants.muext.entities.upgrade.UpgradeSystem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class GameConfig {
	private static final String CRIT_RATE = "resources/crit_dam_rate.json";
	private static final String HERO_EXP = "resources/hero_exp.json";
	private static final String ACC_EXP = "resources/acc_exp.json";
	private static final String UPGRADE_SYSTEM_XML = "resources/upgrade_system.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static GameConfig instance;
	private Map<Integer, Float> critRateMap;
	private Map<Integer, Integer> heroExpMap;
	private Map<Integer, Integer> accExpMap;

	private UpgradeSystem upgradeSystem;


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
		loadUpgradSystem();
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


	public int getExpFromHero(int rank, boolean sameElement) {
		return upgradeSystem.getExpFromHero(rank, sameElement);
	}


	public int getExpFromItem(int itemIndex, boolean sameElement) {
		return upgradeSystem.getExpFromItem(itemIndex, sameElement);
	}


	public Float getCritRate(int chance) {
		Float crit = critRateMap.get(chance);
		return crit == null ? 0 : crit;
	}


	public UpgradeSystem getUpgradeSystem() {
		return upgradeSystem;
	}


	public EvolveHero getEvolveHero(String id) {
		return upgradeSystem.getEvolveHero(id);
	}


	public int getMaxExp(int level) {
		Integer maxExp = heroExpMap.get(level);
		return maxExp == null ? Integer.MAX_VALUE : maxExp;
	}


	public int getAccMaxExp(int level) {
		Integer maxExp = accExpMap.get(level);
		return maxExp == null ? Integer.MAX_VALUE : maxExp;
	}


	public void loadUpgradSystem() {
		try {
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(UPGRADE_SYSTEM_XML));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Unicode>

			upgradeSystem = mapper.readValue(sr, UpgradeSystem.class);
			upgradeSystem.convertBase();

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/upgrade_system_test.json"), upgradeSystem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		GameConfig.getInstance().writeToJsonFile();
	}

}
