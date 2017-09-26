package com.creants.muext.entities;

/**
 * @author LamHM
 *
 */
public enum HeroClassType {
	DARK_KNIGHT(100, "Dragon Dark Knight", "Chiến Binh"),
	GREAT_DARK_KNIGHT(101, "Great Dragon Dark Knight", "Chiến Binh"),
	BLUE_KNIGHT(102, "Blue Dragon Knight", "Chiến Binh"),
	GREEN_KNIGHT(103, "Blue Dragon Knight", "Chiến Binh"),
	DARK_WIZARD(200, "Legendary Dark Wizard", "Pháp Sư"),
	FAIRY_ELF(300, "Iris Fairy Elf", "Tiên Nữ"),
	Storm_Fighter(400, "Storm Fighter", ""),
	Sacred_Fighter(402, "Sacred Fighter", "");

	public int id;
	public String name;
	public String nameVi;


	HeroClassType(int id, String name, String nameVi) {
		this.id = id;
		this.name = name;
		this.nameVi = nameVi;
	}


	public int getId() {
		return id;
	}

}
