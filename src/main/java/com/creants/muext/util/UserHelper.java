package com.creants.muext.util;

import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
public class UserHelper {

	public static String getGameHeroId(QAntUser user) {
		return (String) user.getProperty("GAME_HERO_ID");
	}
}
