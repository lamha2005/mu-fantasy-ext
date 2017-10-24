package com.creants.muext.entities.arena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.entities.HeroClass;

/**
 * @author LamHM
 *
 */
@Document(collection = "arena-power")
public class ArenaPower implements SerializableQAntType {
	@Id
	private transient String gameHeroId;
	public String gameHeroName;
	public Long[] battleTeam;
	public int leaderIndex;
	private transient int maxPower;
	public int teamPower;
	private transient boolean isNPC;
	public List<HeroClass> heroes;
	public String rank;


	public ArenaPower() {
		battleTeam = new Long[] { -1L, -1L, -1L, -1L, -1L };
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public String getGameHeroName() {
		return gameHeroName;
	}


	public void setGameHeroName(String gameHeroName) {
		this.gameHeroName = gameHeroName;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public Long[] getBattleTeam() {
		return battleTeam;
	}


	public void setBattleTeam(Long[] battleTeam) {
		this.battleTeam = battleTeam;
	}


	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public void addHero(long heroId) {
		for (int i = 0; i < battleTeam.length; i++) {
			if (battleTeam[i] < 0) {
				battleTeam[i] = heroId;
				break;
			}
		}
	}


	public int getMaxPower() {
		return maxPower;
	}


	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
	}


	public int getTeamPower() {
		return teamPower;
	}


	public void setTeamPower(int teamPower) {
		this.teamPower = teamPower;
	}


	public boolean isNPC() {
		return isNPC;
	}


	public void setNPC(boolean isNPC) {
		this.isNPC = isNPC;
	}


	public int getTeamLeader() {
		return leaderIndex;
	}


	public void setTeamLeader(int teamLeader) {
		this.leaderIndex = teamLeader;
	}


	public void setBattleTeam(Collection<Long> heroes) {
		int index = 0;
		for (Long id : heroes) {
			this.battleTeam[index++] = id;
		}
	}


	public List<HeroClass> getHeroes() {
		return heroes;
	}


	public void setHeroes(List<HeroClass> heroes) {
		this.heroes = heroes;
	}


	public int getMaxLevelInTeam() {
		if (heroes == null || heroes.size() == 0) {
			return 0;
		}

		int level = 0;
		for (HeroClass hero : heroes) {
			if (level < hero.getLevel()) {
				level = hero.getLevel();
			}
		}

		return level;
	}


	public int getMinLevelInTeam() {
		if (heroes == null || heroes.size() == 0) {
			return 0;
		}

		int level = 0;
		for (HeroClass hero : heroes) {
			if (level > hero.getLevel()) {
				level = hero.getLevel();
			}
		}

		return level;
	}


	public List<Long> getFormation() {
		List<Long> heroIds = new ArrayList<>();
		for (int i = 0; i < battleTeam.length; i++) {
			heroIds.add(battleTeam[i]);
		}
		return heroIds;
	}


	@Override
	public String toString() {
		return "{gameHeroId: " + gameHeroId + ",teamPower:" + teamPower + ", rank:" + rank + "}";
	}

}
