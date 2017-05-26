package com.creants.muext.entities.quest;

import com.creants.muext.services.QuestManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Nhiệm vụ phân bổ theo level, nhiệm vụ thường
 * 
 * @author LamHM
 *
 */
@JsonIgnoreProperties(value = { "minLevel", "maxLevel" })
public class GroupMainQuest extends Quest {

	private int minLevel;

	private int maxLevel;


	public GroupMainQuest() {
		super(QuestManager.GROUP_MAIN_QUEST);
	}


	public int getMinLevel() {
		return minLevel;
	}


	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}


	public int getMaxLevel() {
		return maxLevel;
	}


	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

}
