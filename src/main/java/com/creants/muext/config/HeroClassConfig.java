package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.HeroBase;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.skill.Skill;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class HeroClassConfig {
	private static final String HEROES_CONFIG = "resources/heroes.xml";
	private static final int TEAM_SIZE = 5;
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private Map<Integer, HeroBase> heroes;
	private static HeroClassConfig instance;
	private Collection<Integer> heroIndexList;


	public static HeroClassConfig getInstance() {
		if (instance == null) {
			instance = new HeroClassConfig();
		}
		return instance;
	}


	private HeroClassConfig() {
		loadHeroes();
	}


	private void loadHeroes() {
		try {
			heroes = new HashMap<>();
			heroIndexList = new ArrayList<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(HEROES_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Heroes>
			sr.next(); // to point to root-element under Heroes

			HeroBase heroBase = null;
			while (sr.hasNext()) {
				try {
					heroBase = mapper.readValue(sr, HeroBase.class);
					heroIndexList.add(heroBase.getIndex());
					heroes.put(heroBase.getIndex(), heroBase);
				} catch (Exception e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public HeroBase getHeroBase(int index) {
		return heroes.get(index);
	}


	public HeroBase getRandomHero(int rate) {
		return getHeroes().get((new Random()).nextInt(heroes.size()));
	}


	public HeroBase getRandomHero(int rate, Collection<Integer> excepts) {
		List<Integer> clone = new ArrayList<>(heroIndexList);
		clone.removeAll(excepts);
		return getHeroBase(clone.get((new Random()).nextInt(clone.size())));
	}


	public List<HeroBase> getHeroes() {
		return new ArrayList<>(heroes.values());
	}


	/**
	 * Tạo NPC có power theo tỉ lệ power của người chơi & cân bằng level
	 * 
	 * @param heroPower
	 *            power của người chơi
	 * @param rate
	 *            tỉ lệ power của NPC so với người chơi
	 * @param maxLevel
	 *            level cao nhất của người chơi
	 * @return
	 */
	public List<HeroClass> genNPC(int heroPower, int rate, int maxLevel) {
		int maxNPCPower = heroPower * rate / 100;
		int totalPower = 0;
		Collection<Integer> heroIndexList = new ArrayList<>(TEAM_SIZE);

		List<HeroClass> heroList = new ArrayList<>(TEAM_SIZE);
		for (int i = 0; i < TEAM_SIZE; i++) {
			HeroBase heroBase = getRandomHero(100, heroIndexList);
			heroIndexList.add(heroBase.getIndex());

			HeroClass newHero = createNewHero(heroBase);
			newHero.setId(i);
			newHero.levelUp(maxLevel);
			totalPower += newHero.getPower();
			heroList.add(newHero);
			if (totalPower > maxNPCPower)
				break;
		}

		balancingLevel(heroList, totalPower, maxNPCPower);

		return heroList;
	}


	/**
	 * Cân bằng level
	 * 
	 * @param heroList
	 *            danh sách hero NPC được gen ra
	 * @param totalPower
	 *            tổng power của các hero NPC được gen ra
	 * @param maxNPCPower
	 *            power tối đa của NPC
	 */
	private void balancingLevel(List<HeroClass> heroList, int totalPower, int maxNPCPower) {
		if (totalPower > maxNPCPower)
			return;

		int index = 0;
		int size = heroList.size();
		while (totalPower < maxNPCPower) {
			if (index == size) {
				index = 0;
			}

			HeroClass heroClass = heroList.get(index);
			totalPower -= heroClass.getPower();
			heroClass.levelUp(1);
			totalPower += heroClass.getPower();
			index++;
		}
	}


	private HeroClass createNewHero(HeroBase heroBase) {
		HeroClass heroClass = new HeroClass(heroBase);
		resetSkill(heroClass, heroBase.getSkills());
		return heroClass;
	}


	private void resetSkill(HeroClass heroClass, int[] skillArr) {
		for (int i = 0; i < skillArr.length; i++) {
			int skillIndex = skillArr[i];
			Skill skill = new Skill();
			skill.setIndex(skillIndex);
			skill.setLevel(1);
			heroClass.addSkill(skill);
		}
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/heroes.json"), getHeroes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		HeroClassConfig.getInstance().writeToJsonFile();
	}
}
