package com.creants.muext.entities.quest;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.entities.Reward;

/**
 * @author LamHM
 *
 */
public class AbstractQuest implements SerializableQAntType {
	public String name;
	public String desc;
	public int groupId;
	public int taskType;
	public Task task;
	private transient Reward reward;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public int getGroupId() {
		return groupId;
	}


	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


	public int getTaskType() {
		return taskType;
	}


	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}


	public Task getTask() {
		return task;
	}


	public void setTask(Task task) {
		this.task = task;
	}


	public Reward getReward() {
		return reward;
	}


	public void setReward(Reward reward) {
		this.reward = reward;
	}

}
