package com.creants.muext.entities.quest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_quests")
public class HeroQuest extends AbstractQuest {
	@Id
	public long id;
	public int questIndex;
	@Indexed
	private transient String heroId;
	public transient boolean finish;
	public transient long createTime;
	public boolean claim;


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


	public boolean isClaim() {
		return claim;
	}


	public void setClaim(boolean claim) {
		this.claim = claim;
	}

}
