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

		String taskString = getTaskString();
		if (taskString != null) {
			winNo = Integer.parseInt(taskString);
		}
	}


	public int getWinNo() {
		return winNo;
	}


	public void setWinNo(int winNo) {
		this.winNo = winNo;
	}

}
