package com.creants.muext.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "heros")
public class GameHero implements SerializableQAntType {
	// server_id#user_id
	@Id
	private String id;
	private long userId;
	private transient String serverName;
	private String name;
	private int level;
	private long zen;
	private long soul;
	private int exp;
	@SuppressWarnings("unused")
	private int maxExp;

	private int stamina;
	private int vipLevel;
	private int vipPoint;
	@SuppressWarnings("unused")
	private int maxVipPoint;


	public GameHero(String serverName, long userId) {
		this.serverName = serverName;
		this.userId = userId;
		this.id = genId();
	}


	private String genId() {
		return serverName + "#" + userId;
	}


	public int getMaxExp() {
		return level * 10000;
	}


	public int getMaxVipPoint() {
		return vipLevel * 300;
	}


	public int getStamina() {
		return stamina;
	}


	public void setStamina(int stamina) {
		this.stamina = stamina;
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
