package com.creants.muext.entities.quest;

import java.util.List;

/**
 * @author LamHM
 *
 */
public class Chapter {
	private int id;
	private String name;
	private List<Mission> missions;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Mission> getMissions() {
		return missions;
	}


	public void setMissions(List<Mission> missions) {
		this.missions = missions;
	}

}
