package com.creants.muext.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
@Document(collection = "accounts")
public class GameHero implements SerializableQAntType {
	// server_id#user_id
	@Id
	public String id;
	public long userId;
	public transient String serverName;
	public String name;
	public int level;
	public long zen;
	public long soul;
	public int exp;
	public int maxExp;

	public int stamina;
	public int vipLevel;
	public int vipPoint;
	public int maxVipPoint;

	@Transient
	public List<HeroClass> heroes;


	public GameHero(String serverName, long userId) {
		this.serverName = serverName;
		this.userId = userId;
		this.id = genId();
	}


	public List<HeroClass> getHeroes() {
		return heroes;
	}


	public void setHeroes(List<HeroClass> heroes) {
		this.heroes = heroes;
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
