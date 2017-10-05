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
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.skill.Skill;

/**
 * @author LamHM
 *
 */
@Document(collection = "heroes")
public class HeroClass implements SerializableQAntType {
	@Id
	public long id;
	@Indexed
	public transient String gameHeroId;
	public String name;
	public int level;
	public int exp;
	public int index;
	public int classGroup;
	public int rank;
	public String element;

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

	@Transient
	private transient HeroBase heroBase;

	public int skillPoint;
	public List<Skill> skillList;
	@Transient
	private transient List<HeroEquipment> equipments;


	public HeroClass() {
		level = 1;
		skillList = new ArrayList<>();
		equipments = new ArrayList<>();
	}


	public HeroClass(HeroBase heroBase) {
		level = 1;
		skillList = new ArrayList<>();
		equipments = new ArrayList<>();
		this.heroBase = heroBase;
		initBaseInfo();
	}


	public HeroClass(HeroBase heroBase, int level) {
		this.level = level;
		skillList = new ArrayList<>();
		equipments = new ArrayList<>();
		this.heroBase = heroBase;
		initBaseInfo();
	}


	private void initBaseInfo() {
		name = heroBase.getName();
		index = heroBase.getIndex();
		rank = heroBase.getRank();
		classGroup = heroBase.getClassGroup();
		maxMp = heroBase.getMaxMp();
		mpRec = heroBase.getMpRec();
		element = heroBase.getElement();
		updateBaseStats();
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


	public int nextRank() {
		return rank++;
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


	public void levelUp(int value) {
		level += value;
		this.skillPoint++;
		updateBaseStats();
	}


	public List<HeroEquipment> getEquipments() {
		return equipments;
	}


	public void setEquipments(List<HeroEquipment> equipments) {
		this.equipments = equipments;
	}


	private void updateBaseStats() {
		atk = heroBase.getAtk() + (level - 1) * heroBase.getLvUpATK();
		hp = heroBase.getHp() + (level - 1) * heroBase.getLvUpHP();
		def = (int) (heroBase.getDef() + (level - 1) * heroBase.getLvUpDEF());
		rec = (int) (heroBase.getRec() + (level - 1) * heroBase.getLvUpREC());
		maxExp = GameConfig.getInstance().getMaxExp(level + 1);
	}


	public boolean incrExp(int value) {
		if (value <= 0)
			return false;

		exp += value;
		boolean isLevelUp = false;
		while (exp >= maxExp) {
			exp -= maxExp;
			levelUp(1);
			isLevelUp = true;

			// System.out.println(this);
		}

		return isLevelUp;
	}


	public byte[] genCrit(int roundNo) {
		Random rd = new Random();
		int attackNo = roundNo * 20;

		int chance = (int) (heroBase.getCritch() * 100);
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


	public int getPower() {
		int heroPower = atk * 3 + def + hp + rec / 5;
		if (equipments.size() > 0) {
			for (HeroEquipment equipment : equipments) {
				heroPower += equipment.getPower();
			}
		}

		return heroPower;
	}


	@Override
	public String toString() {
		return "{level:" + level + ",exp:" + exp + ",maxExp:" + maxExp + "}";
	}

}
