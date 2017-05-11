package com.creants.muext.entities.quest;

/**
 * @author LamHM
 *
 */
public enum TaskType {
	MonsterKill(1), WinCampain(2), WinChaos(3), TargetLevel(4), CollectItem(5), UpgradeVip(6), ConsumingGem(7);

	int id;


	private TaskType(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}

}
