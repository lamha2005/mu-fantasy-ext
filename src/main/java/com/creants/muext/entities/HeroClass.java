package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.GameConfig;
import com.creants.muext.entities.skill.Skill;
import com.creants.muext.entities.states.AdditionLevelUpStats;
import com.creants.muext.entities.states.BaseStats;

/**
 * @author LamHM
 *
 */
@Document(collection = "heroes")
public class HeroClass implements SerializableQAntType {
	@Id
	public long id;
	@Transient
	public int exp;
	@Transient
	public int atk;
	@Transient
	public int mag;
	@Transient
	public int hp;
	@Transient
	public int mp;
	@Transient
	public int def;
	@Transient
	public int res;
	@Transient
	public int spd;
	@Transient
	public boolean ranger;
	@Indexed
	public transient String gameHeroId;
	public String name;
	public int level;
	public int index;
	public int rank;

	@Transient
	private transient HeroBase heroBase;

	public int skillPoint;
	public List<Skill> skillList;


	public HeroClass() {
		level = 1;
		skillList = new ArrayList<>();
	}


	public HeroClass(HeroBase heroBase) {
		level = 1;
		skillList = new ArrayList<>();
		this.heroBase = heroBase;
		initBaseInfo();
	}


	private void initBaseInfo() {
		name = heroBase.getName();
		index = heroBase.getIndex();
		ranger = heroBase.isRanger();
		levelUp(level);
	}


	public void setId(long id) {
		this.id = id;
	}


	public HeroBase getHeroBase() {
		return heroBase;
	}


	public void setHeroBase(HeroBase heroBase) {
		this.heroBase = heroBase;
		initBaseInfo();
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
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


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getExpPoolSize() {
		return -1;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public List<Skill> getSkillList() {
		return skillList;
	}


	public void setSkillList(List<Skill> skillList) {
		this.skillList = skillList;
	}


	public void addSkill(Skill skill) {
		skillList.add(skill);
	}


	public int getSkillPoint() {
		return skillPoint;
	}


	public void setSkillPoint(int skillPoint) {
		this.skillPoint = skillPoint;
	}


	public void levelUp(int levelUp) {
		BaseStats baseStats = heroBase.getBaseStats();
		AdditionLevelUpStats levelUpStats = heroBase.getLevelUpStats();
		atk = baseStats.getAtk() + (levelUp - 1) * levelUpStats.getAtk();
		mag = baseStats.getMag() + (levelUp - 1) * levelUpStats.getMag();
		hp = baseStats.getHp() + (levelUp - 1) * levelUpStats.getHp();
		mp = (int) (baseStats.getMp() + (levelUp - 1) * levelUpStats.getMp());
		def = (int) (baseStats.getDef() + (levelUp - 1) * levelUpStats.getDef());
		res = (int) (baseStats.getRes() + (levelUp - 1) * levelUpStats.getRes());
		spd = (int) (baseStats.getSpd() + (levelUp - 1) * levelUpStats.getSpd());
		this.skillPoint++;
	}


	public byte[] genCrit(int roundNo) {
		Random rd = new Random();
		int attackNo = roundNo * 20;

		int chance = (int) (heroBase.getSubStats().getCritch() * 100);
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
