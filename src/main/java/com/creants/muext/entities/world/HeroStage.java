package com.creants.muext.entities.world;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_stages")
public class HeroStage extends Stage {
	@Id
	private long id;
	private String heroId;

	public boolean clear;
	public boolean unlock;
	public int startNo;


	public HeroStage() {

	}


	public HeroStage(Stage stage) {
		this.index = stage.getIndex();
		this.chapterIndex = stage.getChapterIndex();
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getHeroId() {
		return heroId;
	}


	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}


	public boolean isClear() {
		return clear;
	}


	public void setClear(boolean clear) {
		this.clear = clear;
	}


	public boolean isUnlock() {
		return unlock;
	}


	public void setUnlock(boolean unlock) {
		this.unlock = unlock;
	}


	public int getStartNo() {
		return startNo;
	}


	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}

}
