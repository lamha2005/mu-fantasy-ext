package com.creants.muext.entities.quest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_quests")
public class QuestStats extends AbstractQuest {
	@Id
	public long id;
	public int questId;
	@Indexed
	private transient String heroId;
	public boolean finish;
	public long createTime;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getQuestId() {
		return questId;
	}


	public void setQuestId(int questId) {
		this.questId = questId;
	}


	public String getHeroId() {
		return heroId;
	}


	public void setHeroId(String heroId) {
		this.heroId = heroId;
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

}
