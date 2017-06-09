package com.creants.muext.util;

import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
public class UserHelper {
	private static final String LOGIN_TIME_PRO = "LOGIN_TIME";


	public static long getLoginTime1(QAntUser user) {
		return (long) user.getProperty(LOGIN_TIME_PRO);
	}


	public static void setLoginTime(QAntUser user, long loginTime) {
		user.setProperty(LOGIN_TIME_PRO, loginTime);
	}

}
