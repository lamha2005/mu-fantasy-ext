package com.creants.muext;

import com.creants.creants_2x.core.QAntEventType;
import com.creants.creants_2x.core.extension.QAntExtension;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.controllers.ChatRequestHandler;
import com.creants.muext.controllers.JoinZoneEventHandler;
import com.creants.muext.controllers.LoginEventHandler;
import com.creants.muext.controllers.LogoutEventHandler;

/**
 * @author LamHM
 *
 */
public class MuFantasyExtension extends QAntExtension {

	@Override
	public void init() {
		QAntTracer.debug(this.getClass(), "========================= START MU =========================");
		addEventRequestHandler();
		QAntTracer.debug(this.getClass(), "========================= MU STARTED =========================");
	}


	private void addEventRequestHandler() {
		addEventHandler(QAntEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(QAntEventType.USER_LOGOUT, LogoutEventHandler.class);
		addEventHandler(QAntEventType.USER_JOIN_ZONE, JoinZoneEventHandler.class);

		addRequestHandler("cmd_chat", ChatRequestHandler.class);
	}

}
