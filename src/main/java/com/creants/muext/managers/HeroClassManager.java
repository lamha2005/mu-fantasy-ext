package com.creants.muext.managers;

import java.util.HashMap;
import java.util.Map;

import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;

/**
 * @author LamHM
 *
 */
public class HeroClassManager {
	private static HeroClassManager instance;
	private Map<Integer, HeroClass> heroes;


	public static HeroClassManager getInstance() {
		if (null == instance) {
			instance = new HeroClassManager();
		}

		return instance;
	}


	private HeroClassManager() {
		heroes = new HashMap<>();
	}


	public void initHero(HeroClass hero) {
		heroes.put(hero.getId(), hero);
	}


	public void initHero(Map<Integer, HeroClass> heroes) {
		this.heroes = heroes;
	}


	public void initHero(HeroClass... heroes) {
		for (HeroClass t : heroes) {
			this.heroes.put(t.getId(), t);
		}
	}


	@SuppressWarnings("unchecked")
	public <T extends HeroClass> T getHero(HeroClassType heroClass) {
		return (T) heroes.get(heroClass.id);
	}

}
