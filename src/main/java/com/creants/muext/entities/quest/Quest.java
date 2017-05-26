package com.creants.muext.entities.quest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.muext.entities.Reward;

/**
 * @author LamHM
 *
 */
@Document(collection = "quests")
public class Quest extends AbstractQuest {
	@Id
	private int index;

	private transient Reward reward;


	public Quest(int groupId) {
		this.setGroupId(groupId);
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public Reward getReward() {
		return reward;
	}


	public void setReward(Reward reward) {
		this.reward = reward;
	}

}
