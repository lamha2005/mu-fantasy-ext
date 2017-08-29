package com.creants.muext.entities.npc;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
public class NPCGameHero {
	@JacksonXmlProperty(localName = "Id", isAttribute = true)
	private String id;
	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	private String name;
	@JacksonXmlProperty(localName = "Level", isAttribute = true)
	private int level;
	@JacksonXmlProperty(localName = "UserId", isAttribute = true)
	private int userId;
	@JacksonXmlProperty(localName = "HeroList")
	private List<NPCHeroClass> heroList;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public List<NPCHeroClass> getHeroList() {
		return heroList;
	}


	public void setHeroList(List<NPCHeroClass> heroList) {
		this.heroList = heroList;
	}
}
