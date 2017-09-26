package com.creants.muext.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.creants.muext.config.HeroClassConfig;

/**
 * @author LamHM
 *
 */
public class HeroClassTest {
	private HeroClassConfig heroConfig;


	@Before
	public void init() {
		heroConfig = HeroClassConfig.getInstance();
	}


	@Test
	public void incrementExpTest() {
		int reqExpLvl6 = 1800;
		int reqExpLvl7 = 2450;
		int incrExp = 3000;
		HeroClass hero = new HeroClass(heroConfig.getRandomHero(100), 5);
		Assert.assertEquals(hero.getLevel(), 5);
		Assert.assertEquals(hero.getMaxExp(), reqExpLvl6);
		hero.incrExp(incrExp);
		Assert.assertEquals(hero.getLevel(), 6);
		Assert.assertEquals(hero.getExp(), incrExp - reqExpLvl6);
		Assert.assertEquals(hero.getMaxExp(), reqExpLvl7);
	}


	@Test
	public void levelUpTest() {
		int fromLevel = 4;
		int incrExp = 37500;
		HeroClass hero = new HeroClass(heroConfig.getRandomHero(100), fromLevel);
		hero.incrExp(incrExp);
		System.out.println("[DEBUG]" + hero);

	}

}
