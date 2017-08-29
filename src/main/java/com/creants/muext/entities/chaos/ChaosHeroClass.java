package com.creants.muext.entities.chaos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.data.annotation.Id;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.GameConfig;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.skill.Skill;

/**
 * @author LamHM
 *
 */
public class ChaosHeroClass implements SerializableQAntType {
	@Id
	public long id;
	public String name;
	public int level;
	public int index;
	public int classGroup;
	public int rank;
	public String element;

	public int atk;
	public int hp;
	public int def;
	public int rec;
	public int maxMp;
	public int mpRec;

	public List<Skill> skillList;


	public ChaosHeroClass() {
		level = 1;
		skillList = new ArrayList<>();
	}


	public ChaosHeroClass(HeroClass heroClass) {
		id = heroClass.getId();
		name = heroClass.getName();
		level = heroClass.getLevel();
		index = heroClass.getIndex();
		classGroup = heroClass.getClassGroup();
		rank = heroClass.getRank();
		element = heroClass.getElement();

		atk = heroClass.getAtk();
		hp = heroClass.getHp();
		def = heroClass.getDef();
		rec = heroClass.getRec();
		maxMp = heroClass.getMaxExp();
		mpRec = heroClass.getMpRec();

		List<HeroEquipment> equipments = heroClass.getEquipments();
		if (equipments != null && equipments.size() > 0) {
			for (HeroEquipment heroEquipment : equipments) {
				atk += heroEquipment.getAtk();
				hp += heroEquipment.getHp();
				def += heroEquipment.getDef();
				rec += heroEquipment.getDef();
			}
		}

	}


	public void setId(long id) {
		this.id = id;
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


	public byte[] genCrit(int roundNo) {
		Random rd = new Random();
		int attackNo = roundNo * 20;

		// int chance = (int) (heroBase.getCritch() * 100);
		int chance = 1;
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
