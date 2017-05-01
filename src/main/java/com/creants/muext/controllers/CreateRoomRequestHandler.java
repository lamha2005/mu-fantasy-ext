package com.creants.muext.controllers;

import com.creants.creants_2x.core.exception.QAntCreateRoomException;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.setting.CreateRoomSettings;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHa
 *
 */
public class CreateRoomRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String name = params.getUtfString("n");
		String pass = params.getUtfString("p");
		int maxUsers = params.getShort("mu");

		CreateRoomSettings roomSettings = new CreateRoomSettings();
		roomSettings.setName(name);
		roomSettings.setGroupId("MuFantasy");
		roomSettings.setPassword(pass);
		roomSettings.setGame(true);
		roomSettings.setMaxUsers(maxUsers);
		user.updateLastRequestTime();

		roomSettings.setExtension(
				new CreateRoomSettings.RoomExtensionSettings("MuExtension", "com.creants.muext.MuFantasyExtension"));
		try {
			getApi().createRoom(getParentExtension().getParentZone(), roomSettings, user, true, null, true, true);
		} catch (QAntCreateRoomException e) {
			e.printStackTrace();
		}
	}

}
