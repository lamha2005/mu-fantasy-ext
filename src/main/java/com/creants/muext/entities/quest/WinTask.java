package com.creants.muext.entities.quest;

/**
 * @author LamHM
 *
 */
public class WinTask extends Quest {
	private int winNo;


	@Override
	public void convertBase() {
		splitReward();
		splitStageIndex();

		winNo = getCount();
	}


	public int getWinNo() {
		return winNo;
	}


	public void setWinNo(int winNo) {
		this.winNo = winNo;
	}

}
