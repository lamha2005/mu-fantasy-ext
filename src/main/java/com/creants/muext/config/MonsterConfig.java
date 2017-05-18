package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.Monster;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class MonsterConfig {
	private static final String MONSTERS_CONFIG = "resources/monsters.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	private static MonsterConfig instance;
	private Map<Integer, Monster> monsters;


	public static MonsterConfig getInstance() {
		if (instance == null) {
			instance = new MonsterConfig();
		}
		return instance;
	}


	private MonsterConfig() {
		loadMonsters();
	}


	private void loadMonsters() {
		try {
			monsters = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(MONSTERS_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Monsters>
			sr.next();
			Monster monster = null;
			while (sr.hasNext()) {
				try {
					monster = mapper.readValue(sr, Monster.class);
					monsters.put(monster.getIndex(), monster);
				} catch (NoSuchElementException e) {

				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public Monster getMonster(int resourceId) {
		return monsters.get(resourceId);
	}


	public Collection<Monster> getMonsters() {
		return monsters.values();
	}


	public Monster createMonster(int index) {
		return new Monster(getMonster(index));
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/monsters.json"), getMonsters());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		MonsterConfig.getInstance().writeToJsonFile();
	}

}
