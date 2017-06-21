package com.creants.muext.admin.om;

/**
 * @author LamHM
 *
 */
public enum AdminRole {
	SUPER_ADMIN("Super Admin", 1),
	ADMIN("Admin", 2),
	MOD("Mod", 3),
	SUPER_MOD("Super Mod", 4),
	GAME_MASTER("Game Master", 5);

	String roleName;
	int id;


	AdminRole(String roleName, int id) {
		this.roleName = roleName;
		this.id = id;
	}


	public String getRoleName() {
		return roleName;
	}


	public int getId() {
		return id;
	}

}
