package com.creants.muext;

import com.creants.creants_2x.core.QAntEventType;
import com.creants.creants_2x.core.extension.QAntExtension;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.controllers.ChatRequestHandler;
import com.creants.muext.controllers.CreateRoomRequestHandler;
import com.creants.muext.controllers.GetQuestListRequestHandler;
import com.creants.muext.controllers.JoinChapterRequestHandler;
import com.creants.muext.controllers.JoinGameRequestHandler;
import com.creants.muext.controllers.JoinZoneEventHandler;
import com.creants.muext.controllers.LoginEventHandler;
import com.creants.muext.controllers.LogoutEventHandler;
import com.creants.muext.controllers.StageFinishRequestHandler;
import com.creants.muext.controllers.StageRequestHandler;

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
		addRequestHandler("cmd_join_game", JoinGameRequestHandler.class);
		addRequestHandler("cmd_create_room", CreateRoomRequestHandler.class);
		addRequestHandler("cmd_get_quests", GetQuestListRequestHandler.class);
		addRequestHandler("cmd_join_stage", StageRequestHandler.class);
		addRequestHandler("cmd_stage_finish", StageFinishRequestHandler.class);
		addRequestHandler("cmd_join_chapter", JoinChapterRequestHandler.class);
	}

}
