package com.creants.muext.entities.chaos;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.HeroClassConfig;
import com.creants.muext.entities.HeroClass;

/**
 * @author LamHM
 *
 */
@Document(collection = "chaos-castle-stage")
public class ChaosCastleStage implements SerializableQAntType {
	@Id
	private String gameHeroId;
	private transient String opponentId;
	public int stageIndex;
	public String rank;
	public boolean clamed;
	@Transient
	public ChaosCastlePower opponent;


	public ChaosCastleStage() {
	}


	public boolean isClamed() {
		return clamed;
	}


	public void setClamed(boolean clamed) {
		this.clamed = clamed;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public int getStageIndex() {
		return stageIndex;
	}


	public void setStageIndex(int stageIndex) {
		this.stageIndex = stageIndex;
	}


	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public String getOpponentId() {
		return opponentId;
	}


	public void setOpponentId(String opponentId) {
		this.opponentId = opponentId;
	}


	public ChaosCastlePower getOpponent() {
		return opponent;
	}


	public void setOpponent(ChaosCastlePower opponent) {
		this.opponent = opponent;
		List<HeroClass> heroes = opponent.getHeroes();
		HeroClassConfig heroConfig = HeroClassConfig.getInstance();
		for (HeroClass heroClass : heroes) {
			heroClass.setHeroBase(heroConfig.getHeroBase(heroClass.getIndex()));
		}
	}

}
