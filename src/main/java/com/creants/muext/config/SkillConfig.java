package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
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
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static SkillConfig instance;
	private Map<Integer, SkillBase> skillMap;


	public static SkillConfig getInstance() {
		if (instance == null) {
			instance = new SkillConfig();
		}
		return instance;
	}


	private SkillConfig() {
		loadSkill();
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


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/skills.json"), getSkills());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
