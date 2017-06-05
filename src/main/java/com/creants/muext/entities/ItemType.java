package com.creants.muext.entities;

/**
 * @author LamHM
 *
 */
public enum ItemType {
	WEAPON(0, "Vũ khí"),
	POTISON(1, "Thuốc"),
	HERO_FRAGMENT(2, "Mảnh Hero"),
	EQUIPMENT_FRAGMENT(3, "Mảnh vũ khí"),
	SKILL_BOOK_FRAGMENT(4, "Mảnh sách kĩ năng");
	int groupIndex;
	String groupName;


	ItemType(int groupIndex, String groupName) {
		this.groupIndex = groupIndex;
		this.groupName = groupName;
	}


	public int getGroupIndex() {
		return groupIndex;
	}


	public String getGroupName() {
		return groupName;
	}

}
