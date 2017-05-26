package com.creants.muext.util;

import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
public class UserHelper {
	private static final String GAME_HERO_ID_PRO = "GAME_HERO_ID";
	private static final String LOGIN_TIME_PRO = "LOGIN_TIME";


	public static String getGameHeroId(QAntUser user) {
		return (String) user.getProperty(GAME_HERO_ID_PRO);
	}


	public static long getLoginTime(QAntUser user) {
		return (long) user.getProperty(LOGIN_TIME_PRO);
	}


	public static void setHeroId(QAntUser user, String gameHeroId) {
		user.setProperty(GAME_HERO_ID_PRO, gameHeroId);
	}


	public static void setLoginTime(QAntUser user, long loginTime) {
		user.setProperty(LOGIN_TIME_PRO, loginTime);
	}


}
