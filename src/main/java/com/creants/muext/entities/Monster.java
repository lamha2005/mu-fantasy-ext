package com.creants.muext.entities;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
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
	@JacksonXmlProperty(localName = "Map", isAttribute = true)
	public String map;
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	public int level;
	@JacksonXmlProperty(localName = "MonsterRank", isAttribute = true)
	public int rank;

	@JacksonXmlProperty(localName = "Image", isAttribute = true)
	public String img;
	@JacksonXmlProperty(localName = "Type", isAttribute = true)
	public String type;
	@JacksonXmlProperty(localName = "Element", isAttribute = true)
	public String element;

	@JacksonXmlProperty(localName = "Damage", isAttribute = true)
	public int dam;
	@JacksonXmlProperty(localName = "HealthPoint", isAttribute = true)
	public int hp;
	@JacksonXmlProperty(localName = "Defense", isAttribute = true)
	public int def;

	@JacksonXmlProperty(localName = "Recovery", isAttribute = true)
	public int rec;

	@JacksonXmlProperty(localName = "BCPerHit", isAttribute = true)
	public int bcPerHit;

	@JacksonXmlProperty(localName = "MaxBC", isAttribute = true)
	public int maxBC;
	@JacksonXmlProperty(localName = "EXPReward", isAttribute = true)
	public int expReward;


	public Monster() {

	}


	public Monster(Monster monster) {
		this.index = monster.getIndex();
		this.name = monster.getName();
		this.level = monster.getLevel();
		this.img = monster.getImg();
		this.dam = monster.getDam();
		this.hp = monster.getHp();
		this.def = monster.getDef();
		this.rec = monster.getRec();
		this.bcPerHit = monster.getBcPerHit();
		this.maxBC = monster.getMaxBC();
		this.rank = monster.getRank();
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


	public int getBcPerHit() {
		return bcPerHit;
	}


	public void setBcPerHit(int bcPerHit) {
		this.bcPerHit = bcPerHit;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getMaxBC() {
		return maxBC;
	}


	public void setMaxBC(int maxBC) {
		this.maxBC = maxBC;
	}


	public int getLevel() {
		return level;
	}


	public int getIndex() {
		return index;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getRec() {
		return rec;
	}


	public void setRec(int rec) {
		this.rec = rec;
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


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
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


	public String getElement() {
		return element;
	}


	public void setElement(String element) {
		this.element = element;
	}


	public String getName() {
		return name;
	}


	// public byte[] genX2Dam(int roundNo) {
	// Random rd = new Random();
	// int attackNo = roundNo * 20;
	//
	// int chance = (int) (damX2Change * 100);
	// float critRate = GameConfig.getInstance().getCritRate(chance);
	// List<Byte> dam2x = new ArrayList<>();
	// int count = 1;
	// for (byte i = 0; i < attackNo; i++) {
	// int rate = (int) (critRate * count);
	// count++;
	// if (rate >= 100) {
	// dam2x.add(i);
	// count = 1;
	// continue;
	// }
	//
	// boolean b = (rd.nextInt(100 - rate) + 1) == 1;
	// if (b) {
	// dam2x.add(i);
	// count = 1;
	// }
	// }
	//
	// byte[] result = new byte[dam2x.size()];
	// for (int i = 0; i < dam2x.size(); i++) {
	// result[i] = dam2x.get(i);
	// }
	//
	// return result;
	// }

	public String getMap() {
		return map;
	}


	public void setMap(String map) {
		this.map = map;
	}


	public byte[] genX2Dam(int roundNo) {
		return new byte[] {};
	}


	public int getExpReward() {
		return expReward;
	}


	public void setExpReward(int expReward) {
		this.expReward = expReward;
	}

}
