package com.creants.muext.entities;

/**
 * @author LamHM
 *
 */
public enum HeroClassType {
	DARK_KNIGHT(10, "Dragon Dark Knight", "Chiến Binh"),
	GREAT_DARK_KNIGHT(11, "Great Dragon Dark Knight", "Chiến Binh"),
	DARK_WIZARD(20, "Dark Wizard", "Pháp Sư"),
	FAIRY_ELF(30, "Fairy Elf", "Tiên Nữ");

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
