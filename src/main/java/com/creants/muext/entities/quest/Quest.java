package com.creants.muext.entities.quest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "quests")
public class Quest extends AbstractQuest {
	@Id
	private int id;

	public Quest(int groupId){
		this.setGroupId(groupId);
	}
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

}
