package com.creants.muext.config;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.creants.muext.entities.HeroClass;

/**
 * @author LamHM
 *
 */
public class HeroClassConfigTest {
	private HeroClassConfig heroClassConfig;
	private GameConfig gameConfig;


	@Before
	public void init() {
		heroClassConfig = HeroClassConfig.getInstance();
		gameConfig = GameConfig.getInstance();
	}


//	@Test
	public void genBattleTeamTest() {
		List<HeroClass> genNPC = heroClassConfig.genNPC(58881, 200, 30);
		int totalPower = 0;
		for (HeroClass heroClass : genNPC) {
			int power = heroClass.getPower();
			System.out.println(heroClass.getName() + ", level:" + heroClass.getLevel() + ", power:" + power);
			totalPower += power;
		}

		System.out.println(totalPower);
	}
	
	@Test
	public void testIncrExp(){
		int expFromHero = gameConfig.getExpFromHero(3, true);
		System.out.println(expFromHero);
	}

}
