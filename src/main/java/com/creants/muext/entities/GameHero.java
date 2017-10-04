package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.GameConfig;
import com.creants.muext.entities.item.HeroItem;

/**
 * @author LamHM
 *
 */
@Document(collection = "game-heroes")
public class GameHero implements SerializableQAntType {
	@Id
	public String id;
	public long userId;
	public String avatar;
	public String name;
	public int level;
	public int exp;
	public int maxExp;

	public long zen;
	public long soul;
	public int stamina;
	public int vipPoint;
	public int friendPoint;
	public int bless;

	public int maxStamina;
	public long staUpdTime;
	public int vipLevel;
	public int maxVipPoint;
	public boolean isNPC;

	@Transient
	public List<HeroClass> heroes;
	@Transient
	public List<HeroItem> items;


	public GameHero() {
		maxStamina = 100;
		items = new ArrayList<HeroItem>();
	}


	public List<HeroClass> getHeroes() {
		return heroes;
	}


	public void setHeroes(List<HeroClass> heroes) {
		this.heroes = heroes;
	}


	public List<HeroItem> getItems() {
		return items;
	}


	public void setItems(List<HeroItem> items) {
		this.items = items;
	}


	public int getMaxExp() {
		return maxExp;
	}


	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}


	public int getMaxVipPoint() {
		return vipLevel * 300;
	}


	public int getFriendPoint() {
		return friendPoint;
	}


	public void setFriendPoint(int friendPoint) {
		this.friendPoint = friendPoint;
	}


	public int getStamina() {
		return stamina;
	}


	public long getStaUpdTime() {
		return staUpdTime;
	}


	public void setStaUpdTime(long staUpdTime) {
		this.staUpdTime = staUpdTime;
	}


	public void setStamina(int stamina) {
		this.stamina = stamina;
	}


	public int getMaxStamina() {
		return maxStamina;
	}


	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
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


	public void incrZen(long value) {
		this.zen += value;
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


	public boolean incrExp(int value) {
		if (value <= 0)
			return false;

		exp += value;
		boolean isLevelUp = false;
		while (exp >= maxExp) {
			exp -= maxExp;
			level++;
			maxExp = GameConfig.getInstance().getAccMaxExp(level);
			isLevelUp = true;
		}

		return isLevelUp;
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


	public void setMaxVipPoint(int maxVipPoint) {
		this.maxVipPoint = maxVipPoint;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public boolean isNPC() {
		return isNPC;
	}


	public void setNPC(boolean isNPC) {
		this.isNPC = isNPC;
	}


	public int getBless() {
		return bless;
	}


	public void setBless(int bless) {
		this.bless = bless;
	}
	

}
