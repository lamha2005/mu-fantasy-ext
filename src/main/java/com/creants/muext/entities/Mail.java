package com.creants.muext.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author LamHM
 *
 */
public class Mail {
	@Id
	private long id;
	@Indexed
	private String gameHeroId;
	private long createTime;
	private int type;
	private List<Object> properties;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public List<Object> getProperties() {
		return properties;
	}


	public void setProperties(List<Object> properties) {
		this.properties = properties;
	}

}
