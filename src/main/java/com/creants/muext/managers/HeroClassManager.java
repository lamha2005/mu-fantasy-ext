package com.creants.muext.managers;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.heroes.DarkKnight;
import com.creants.muext.entities.heroes.DarkWizard;
import com.creants.muext.entities.heroes.FairyElf;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
@Service
public class HeroClassManager implements InitializingBean {
	private static final String HEROES_CONFIG = "resources/heroes.xml";
	private static final String MONSTERS_CONFIG = "resources/monsters.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private Map<Integer, HeroClass> heroes;
	private Map<Integer, Monster> monsters;


	@Override
	public void afterPropertiesSet() throws Exception {
		loadHeroes();
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
					monsters.put(monster.getId(), monster);
				} catch (NoSuchElementException e) {

				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void loadHeroes() {
		try {
			heroes = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(HEROES_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Heros>
			sr.next(); // to point to root-element under Heros
			DarkKnight darkKnight = mapper.readValue(sr, DarkKnight.class).setDefaultStats();
			DarkWizard darkWizard = mapper.readValue(sr, DarkWizard.class).setDefaultStats();
			FairyElf fairyElf = mapper.readValue(sr, FairyElf.class).setDefaultStats();
			initHero(darkKnight, darkWizard, fairyElf);
			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void initHero(HeroClass hero) {
		heroes.put(hero.getId(), hero);
	}


	public void initHero(Map<Integer, HeroClass> heroes) {
		this.heroes = heroes;
	}


	private void initHero(HeroClass... heroes) {
		for (HeroClass t : heroes) {
			this.heroes.put(t.getId(), t);
		}
	}


	@SuppressWarnings("unchecked")
	public <T extends HeroClass> T getDefineHeroClass(HeroClassType heroClass) {
		return (T) heroes.get(heroClass.id);
	}


	public Monster getMonster(int id) {
		return monsters.get(id);
	}

}
