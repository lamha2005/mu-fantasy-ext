package com.creants.muext.om;

import com.creants.creants_2x.core.entities.Room;

/**
 * @author LamHa
 *
 */
public class RoomInfo {
	private int id;
	private String name;
	private long betCoin;
	private int maxPlayer;
	private int playerNo;

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

	public long getBetCoin() {
		return betCoin;
	}

	public void setBetCoin(long betCoin) {
		this.betCoin = betCoin;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	public int getPlayerNo() {
		return playerNo;
	}

	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}

	public static RoomInfo newInstanceFrom(Room room) {
		RoomInfo rext = new RoomInfo();
		rext.id = room.getId();
		rext.name = room.getName();
		rext.maxPlayer = room.getMaxUsers();
		rext.playerNo = room.getPlayersList().size();
		rext.betCoin = 0;
		return rext;
	}

}
