package com.creants.muext.entities.quest;

import com.creants.muext.services.QuestManager;

/**
 * Nhiệm vụ phân bổ theo level, nhiệm vụ thường
 * 
 * @author LamHM
 *
 */
public class GroupMainQuest extends Quest {

	private int mixLevel;

	private int maxLevel;


	public GroupMainQuest() {
		super(QuestManager.GROUP_MAIN_QUEST);
	}


	public int getMixLevel() {
		return mixLevel;
	}


	public void setMinLevel(int mixLevel) {
		this.mixLevel = mixLevel;
	}


	public int getMaxLevel() {
		return maxLevel;
	}


	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

}
