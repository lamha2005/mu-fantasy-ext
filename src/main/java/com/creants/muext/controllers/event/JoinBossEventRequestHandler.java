package com.creants.muext.controllers.event;

import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.exception.QAntJoinRoomException;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.controllers.ExtensionEvent;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class JoinBossEventRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String roomName = params.getUtfString("rn");
		Room roomBoss = getParentExtension().getParentZone().getRoomByName(roomName);
		if (roomBoss == null) {
			sendError(MessageFactory.createErrorMsg(ExtensionEvent.CMD_BOSS_EVENT_JOIN, GameErrorCode.STAGE_NOT_FOUND),
					user);
			return;
		}

		boolean isOpen = (boolean) roomBoss.getProperty("open");
		if (!isOpen) {
			sendError(
					MessageFactory.createErrorMsg(ExtensionEvent.CMD_BOSS_EVENT_JOIN, GameErrorCode.EVENT_NOT_OPEN_YET),
					user);
			return;
		}

		try {
			getApi().joinRoom(user, roomBoss, null, false, null, true, true);
		} catch (QAntJoinRoomException e) {
			e.printStackTrace();
		}
	}

}
