package com.creants.muext.entities.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "hero_gift")
public class HeroGift {
	@Id
	private String gameHeroId;
	private int revision;
	private Map<Integer, GiftInfo> giftEventMap;


	public HeroGift() {
		giftEventMap = new HashMap<>();
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public boolean addGiftInfo(GiftInfo giftInfo) {
		if (giftEventMap.containsKey(giftInfo.getGiftIndex())) {
			return false;
		}

		giftEventMap.put(giftInfo.getGiftIndex(), giftInfo);
		return true;
	}


	public int getRevision() {
		return revision;
	}


	public void setRevision(int revision) {
		this.revision = revision;
	}


	public Map<Integer, GiftInfo> getGiftEventMap() {
		return giftEventMap;
	}


	public void setGiftEventMap(Map<Integer, GiftInfo> giftEventMap) {
		this.giftEventMap = giftEventMap;
	}


	public GiftInfo getGift(int giftIndex) {
		return giftEventMap.get(giftIndex);
	}


	public boolean isEmpty() {
		return giftEventMap == null || giftEventMap.size() <= 0;
	}

}
