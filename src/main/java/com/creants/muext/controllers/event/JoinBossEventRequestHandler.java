package com.creants.muext.controllers.event;

import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.exception.QAntJoinRoomException;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.extension.IQAntExtension;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.BossEventExtension;
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
			sendError(MessageFactory.createErrorMsg("cmd_boss_event_join", GameErrorCode.STAGE_NOT_FOUND), user);
			return;
		}

		// boolean isOpen = (boolean) roomBoss.getProperty("open");
		// if (!isOpen) {
		// sendError(MessageFactory.createErrorMsg("cmd_boss_event_join",
		// GameErrorCode.EVENT_NOT_OPEN_YET), user);
		// return;
		// }

		IQAntExtension extension = roomBoss.getExtension();
		if (extension instanceof BossEventExtension) {
			System.out.println("is Boss event extension");
		}

		try {
			getApi().joinRoom(user, roomBoss, null, false, null, true, true);
			// TODO response boss info
		} catch (QAntJoinRoomException e) {
			e.printStackTrace();
		}
	}

}
