package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.GameConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class Monster implements SerializableQAntType {
	public Integer id;

	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	public int index;

	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	public String name;
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	public int level;

	@JacksonXmlProperty(localName = "Image", isAttribute = true)
	public String img;
	@JacksonXmlProperty(localName = "MonsterX2DamageChance", isAttribute = true)
	public float damX2Change;

	@JacksonXmlProperty(localName = "Dam", isAttribute = true)
	public int dam;
	@JacksonXmlProperty(localName = "HealthPoint", isAttribute = true)
	public int hp;
	@JacksonXmlProperty(localName = "Defense", isAttribute = true)
	public int def;

	@JacksonXmlProperty(localName = "MagicResistance", isAttribute = true)
	public int res;
	@JacksonXmlProperty(localName = "Speed", isAttribute = true)
	public int spd;


	public Monster() {

	}


	public Monster(Monster monster) {
		this.index = monster.getIndex();
		this.name = monster.getName();
		this.level = monster.getLevel();
		this.img = monster.getImg();
		this.damX2Change = monster.getDamX2Change();
		this.dam = monster.getDam();
		this.hp = monster.getHp();
		this.def = monster.getDef();
		this.res = monster.getRes();
		this.spd = monster.getSpd();
	}


	/**
	 * id của monster trong trận đấu
	 */
	public Integer getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getLevel() {
		return level;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public void setDam(int dam) {
		this.dam = dam;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public void setDef(int def) {
		this.def = def;
	}


	public void setRes(int res) {
		this.res = res;
	}


	public void setSpd(int spd) {
		this.spd = spd;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public float getDamX2Change() {
		return damX2Change;
	}


	public void setDamX2Change(float damX2Change) {
		this.damX2Change = damX2Change;
	}


	public int getDam() {
		return dam;
	}


	public int getHp() {
		return hp;
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


	public String getName() {
		return name;
	}


	public byte[] genX2Dam(int roundNo) {
		Random rd = new Random();
		int attackNo = roundNo * 20;

		int chance = (int) (damX2Change * 100);
		float critRate = GameConfig.getInstance().getCritRate(chance);
		List<Byte> dam2x = new ArrayList<>();
		int count = 1;
		for (byte i = 0; i < attackNo; i++) {
			int rate = (int) (critRate * count);
			count++;
			if (rate >= 100) {
				dam2x.add(i);
				count = 1;
				continue;
			}

			boolean b = (rd.nextInt(100 - rate) + 1) == 1;
			if (b) {
				dam2x.add(i);
				count = 1;
			}
		}

		byte[] result = new byte[dam2x.size()];
		for (int i = 0; i < dam2x.size(); i++) {
			result[i] = dam2x.get(i);
		}

		return result;
	}

}
