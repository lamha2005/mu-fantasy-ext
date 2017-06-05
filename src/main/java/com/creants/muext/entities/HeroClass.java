package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.muext.config.GameConfig;
import com.creants.muext.entities.states.AdditionLevelUpStats;
import com.creants.muext.entities.states.AdditionStats;
import com.creants.muext.entities.states.BaseStats;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@Document(collection = "heroes")
public abstract class HeroClass extends Character {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	public int index;
	@JacksonXmlProperty(localName = "Ranger", isAttribute = true)
	public boolean ranger;
	@Id
	public long id;
	public int exp;
	public int rank;

	public int atk;
	public int mag;
	public int hp;
	public int mp;
	public int def;
	public int res;
	public int spd;
	@Indexed
	public transient String gameHeroId;

	private AdditionStats additionStats;
	@JacksonXmlProperty(localName = "LevelUpStats")
	@Transient
	private AdditionLevelUpStats levelUpStats;


	public HeroClass() {
		init();
	}


	public void setId(long id) {
		this.id = id;
	}


	public abstract void init();


	@SuppressWarnings("unchecked")
	public <T> T setDefaultStats() {
		BaseStats baseStats = getBaseStats();
		rank = 1;
		atk = baseStats.getAtk();
		mag = baseStats.getMag();
		hp = baseStats.getHp();
		mp = baseStats.getMp();
		def = baseStats.getDef();
		res = baseStats.getRes();
		spd = baseStats.getSpd();
		return (T) this;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public long getId() {
		return id;
	}


	public int getExp() {
		return exp;
	}


	public void setExp(int exp) {
		this.exp = exp;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public AdditionStats getAdditionStats() {
		return additionStats;
	}


	public void setAdditionStats(AdditionStats additionStats) {
		this.additionStats = additionStats;
	}


	public AdditionLevelUpStats getLevelUpStats() {
		return levelUpStats;
	}


	public void setLevelUpStats(AdditionLevelUpStats levelUpStats) {
		this.levelUpStats = levelUpStats;
	}


	public int getAtk() {
		return atk;
	}


	public int getMag() {
		return mag;
	}


	public int getHp() {
		return hp;
	}


	public int getMp() {
		return mp;
	}


	public int getDef() {
		return def;
	}


	public int getRes() {
		return res;
	}


	public int getSpd() {
		return spd;
	}


	public int getExpPoolSize() {
		return -1;
	}


	public boolean isRanger() {
		return ranger;
	}


	public void setRanger(boolean ranger) {
		this.ranger = ranger;
	}


	public void levelUp(int levelUp) {
		atk = getBaseStats().getAtk() + (levelUp - 1) * levelUpStats.getAtk();
		mag = getBaseStats().getMag() + (levelUp - 1) * levelUpStats.getMag();
		hp = getBaseStats().getHp() + (levelUp - 1) * levelUpStats.getHp();
		mp = (int) (getBaseStats().getMp() + (levelUp - 1) * levelUpStats.getMp());
		def = (int) (getBaseStats().getDef() + (levelUp - 1) * levelUpStats.getDef());
		res = (int) (getBaseStats().getRes() + (levelUp - 1) * levelUpStats.getRes());
		spd = (int) (getBaseStats().getSpd() + (levelUp - 1) * levelUpStats.getSpd());
	}


	public byte[] genCrit(int roundNo) {
		Random rd = new Random();
		int attackNo = roundNo * 20;

		int chance = (int) (getSubStats().getCritch() * 100);
		float critRate = GameConfig.getInstance().getCritRate(chance);
		List<Byte> critList = new ArrayList<Byte>();
		int count = 1;
		for (Byte i = 0; i < attackNo; i++) {
			int rate = (int) (critRate * count);
			count++;
			if (rate >= 100) {
				critList.add(i);
				count = 1;
				continue;
			}

			boolean b = (rd.nextInt(100 - rate) + 1) == 1;
			if (b) {
				critList.add(i);
				count = 1;
			}
		}

		byte[] result = new byte[critList.size()];
		for (int i = 0; i < critList.size(); i++) {
			result[i] = critList.get(i);
		}

		return result;
	}

}
