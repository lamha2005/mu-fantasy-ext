package com.creants.muext.entities;

import org.springframework.data.annotation.Id;

/**
 * @author LamHM
 *
 */
public class GameHero {
	// server_id#user_id
	@Id
	private String id;
	private long userId;
	private String serverName;
	private String name;
	private int level;
	private long zen;
	private long soul;
	private int exp;
	private int statmina;
	private int vipLevel;
	private int vipPoint;


	public GameHero(String serverName, long userId) {
		this.serverName = serverName;
		this.userId = userId;
		this.id = genId();
	}


	private String genId() {
		return serverName + "#" + userId;
	}


	public int getStatmina() {
		return statmina;
	}


	public void setStatmina(int statmina) {
		this.statmina = statmina;
	}


	public String getServerName() {
		return serverName;
	}


	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getZen() {
		return zen;
	}


	public void setZen(long zen) {
		this.zen = zen;
	}


	public long getSoul() {
		return soul;
	}


	public void setSoul(long soul) {
		this.soul = soul;
	}


	public int getExp() {
		return exp;
	}


	public void setExp(int exp) {
		this.exp = exp;
	}


	public int getVipLevel() {
		return vipLevel;
	}


	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}


	public int getVipPoint() {
		return vipPoint;
	}


	public void setVipPoint(int vipPoint) {
		this.vipPoint = vipPoint;
	}

}
