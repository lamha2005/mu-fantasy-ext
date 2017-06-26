package com.creants.muext.util;

import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
public class UserHelper {
	private static final String LOGIN_TIME_PRO = "LOGIN_TIME";
	public static final String GAME_HERO_NAME = "GAME_HERO_NAME";


	public static long getLoginTime1(QAntUser user) {
		return (long) user.getProperty(LOGIN_TIME_PRO);
	}


	public static void setLoginTime(QAntUser user, long loginTime) {
		user.setProperty(LOGIN_TIME_PRO, loginTime);
	}


	public static void setGameHeroName(QAntUser user, String name) {
		user.setProperty(GAME_HERO_NAME, name);
	}


	public static String getGameHeroName(QAntUser user) {
		return (String) user.getProperty(GAME_HERO_NAME);
	}

}
