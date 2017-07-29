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
	public int exp;
	@Transient
	public int maxExp;
	@Transient
	public int atk;
	@Transient
	public int hp;
	@Transient
	public int def;
	@Transient
	public int rec;

	@Transient
	public int maxMp;
	@Transient
	public int mpRec;
	@Indexed
	public transient String gameHeroId;
	public String name;
	public int level;
	public int index;
	public int classGroup;
	public int rank;
	public String element;

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
		rank = heroBase.getRank();
		classGroup = heroBase.getClassGroup();
		maxMp = heroBase.getSubStats().getMaxMp();
		mpRec = heroBase.getSubStats().getMpRec();
		element = heroBase.getElement();
		maxExp = GameConfig.getInstance().getMaxExp(level + 1);
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


	public int getMaxMp() {
		return maxMp;
	}


	public int getMpRec() {
		return mpRec;
	}


	public int getExp() {
		return exp;
	}


	public int getMaxExp() {
		return maxExp;
	}


	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
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


	public int getHp() {
		return hp;
	}


	public int getDef() {
		return def;
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


	public int getRec() {
		return rec;
	}


	public void setRec(int rec) {
		this.rec = rec;
	}


	public int getClassGroup() {
		return classGroup;
	}


	public void setClassGroup(int classGroup) {
		this.classGroup = classGroup;
	}


	public String getElement() {
		return element;
	}


	public void setElement(String element) {
		this.element = element;
	}


	public void levelUp(int levelUp) {
		BaseStats baseStats = heroBase.getBaseStats();
		AdditionLevelUpStats levelUpStats = heroBase.getLevelUpStats();
		atk = baseStats.getAtk() + (levelUp - 1) * levelUpStats.getAtk();
		hp = baseStats.getHp() + (levelUp - 1) * levelUpStats.getHp();
		def = (int) (baseStats.getDef() + (levelUp - 1) * levelUpStats.getDef());
		rec = (int) (baseStats.getRec() + (levelUp - 1) * levelUpStats.getRec());
		this.skillPoint++;
	}


	public boolean incrExp(int value) {
		if (value <= 0)
			return false;

		exp += value;
		boolean isLevelUp = false;
		while (exp >= maxExp) {
			exp -= maxExp;
			level++;
			maxExp = GameConfig.getInstance().getMaxExp(level);
			levelUp(level);
			isLevelUp = true;
		}

		return isLevelUp;
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
