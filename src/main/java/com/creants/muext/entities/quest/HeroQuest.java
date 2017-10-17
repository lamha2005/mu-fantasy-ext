package com.creants.muext.entities.quest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_quests")
public class HeroQuest implements SerializableQAntType {
	@Id
	public long id;
	@Indexed
	private transient String gameHeroId;
	public int questIndex;
	public transient boolean finish;
	public transient long createTime;
	public boolean claim;
	public boolean seen;
	public int taskType;
	public int count;
	@Indexed
	public String groupId;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getQuestIndex() {
		return questIndex;
	}


	public void setQuestIndex(int questIndex) {
		this.questIndex = questIndex;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public boolean isFinish() {
		return finish;
	}


	public void setFinish(boolean finish) {
		this.finish = finish;
	}


	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public boolean isClaim() {
		return claim;
	}


	public void setClaim(boolean claim) {
		this.claim = claim;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public int getTaskType() {
		return taskType;
	}


	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}


	public boolean isSeen() {
		return seen;
	}


	public void setSeen(boolean seen) {
		this.seen = seen;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

}
