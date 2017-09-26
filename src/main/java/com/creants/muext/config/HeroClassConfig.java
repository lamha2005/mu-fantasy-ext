package com.creants.muext.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.HeroBase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class HeroClassConfig {
	private static final String HEROES_CONFIG = "resources/heroes.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private Map<Integer, HeroBase> heroes;
	private static HeroClassConfig instance;


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
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(HEROES_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Heroes>
			sr.next(); // to point to root-element under Heroes

			HeroBase heroBase = null;
			while (sr.hasNext()) {
				try {
					heroBase = mapper.readValue(sr, HeroBase.class);
					heroes.put(heroBase.getIndex(), heroBase);
				} catch (NoSuchElementException e) {

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


	public List<HeroBase> getHeroes() {
		return new ArrayList<>(heroes.values());
	}
}
