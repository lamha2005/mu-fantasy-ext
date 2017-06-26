package com.creants.muext.entities.event;

/**
 * @author LamHM
 *
 */
public class DAMInfo implements Comparable<DAMInfo> {
	private String gameHeroId;
	private String gameHeroName;
	private int point;
	private int turnNo;


	@Override
	public int compareTo(DAMInfo o) {
		if (point == o.point)
			return 0;

		if (point > o.point)
			return 1;

		return -1;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
	}


	public void incDam(int value) {
		this.point += value;
	}


	public String getGameHeroName() {
		return gameHeroName;
	}


	public void setGameHeroName(String gameHeroName) {
		this.gameHeroName = gameHeroName;
	}


	public int getTurnNo() {
		return turnNo;
	}


	public void setTurnNo(int turnNo) {
		this.turnNo = turnNo;
	}

}
