package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.skill.SkillBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class SkillConfig {
	private static final String SKILL_CONFIG = "resources/skills.xml";
	private static final String UPGRADE_COST_CONFIG = "resources/update_cost.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static SkillConfig instance;
	private Map<Integer, SkillBase> skillMap;
	private Map<Integer, List<Long>> upgradeCostMap;


	public static SkillConfig getInstance() {
		if (instance == null) {
			instance = new SkillConfig();
		}
		return instance;
	}


	private SkillConfig() {
		loadSkill();
		// loadUpgradeCost();
	}


	private void loadSkill() {
		try {
			skillMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(SKILL_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Monsters>
			sr.next();
			SkillBase skill = null;
			while (sr.hasNext()) {
				try {
					skill = mapper.readValue(sr, SkillBase.class);
					skillMap.put(skill.getIndex(), skill);
				} catch (NoSuchElementException e) {

				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void loadUpgradeCost() {
		upgradeCostMap = new HashMap<>();

		XMLStreamReader sr;
		try {
			sr = f.createXMLStreamReader(new FileInputStream(UPGRADE_COST_CONFIG));
			sr.next(); // to point to <Monsters>
			while (sr.hasNext()) {
				sr.next();

				if (sr.getEventType() != XMLStreamReader.START_ELEMENT) {
					continue;
				}

				List<Long> costList = new ArrayList<>();
				int skillIndex = -1;
				int attributeCount = sr.getAttributeCount();
				for (int i = 0; i <= attributeCount; i++) {
					String attName = sr.getAttributeLocalName(i);
					String attValue = sr.getAttributeValue(i);
					if (attName.equals("SkillIndex")) {
						skillIndex = Integer.parseInt(attValue);
					} else if (attName.startsWith("ZenCostLV")) {
						costList.add(Long.parseLong(attValue));
					}

				}

				upgradeCostMap.put(skillIndex, costList);
			}
			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public long getCost(int skillIndex, int level) {
		List<Long> list = upgradeCostMap.get(skillIndex);
		Long cost = list.get(level - 1);
		if (cost == null)
			return Long.MAX_VALUE;
		return cost;
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/skills.json"), getSkills());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean containSkill(int index) {
		return skillMap.containsKey(index);
	}


	private Object getSkills() {
		return skillMap.values();
	}


	public SkillBase getSkill(int index) {
		return skillMap.get(index);
	}


	public static void main(String[] args) {
		SkillConfig.getInstance().writeToJsonFile();
	}
}
