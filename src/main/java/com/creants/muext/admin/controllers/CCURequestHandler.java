package com.creants.muext.admin.controllers;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.admin.managers.AdminManager;

/**
 * @author LamHM
 *
 */
public class CCURequestHandler extends BaseClientRequestHandler {
	private AdminManager adminManager;


	public CCURequestHandler() {
		adminManager = Creants2XApplication.getBean(AdminManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		adminManager.notifyCCU(user);

		// CreateRoomSettings roomSettings = new CreateRoomSettings();
		// roomSettings.setName("Boss Event");
		// roomSettings.setGroupId("Boss Event");
		// roomSettings.setGame(true);
		// roomSettings.setMaxUsers(1000);
		// user.updateLastRequestTime();
		//
		// roomSettings.setExtension(
		// new CreateRoomSettings.RoomExtensionSettings("BossExtension",
		// "com.creants.muext.BossEventExtension"));
		// try {
		// Room roomBoss =
		// getApi().createRoom(getParentExtension().getParentZone(),
		// roomSettings, null, false, null,
		// false, false);
		// System.out.println(roomBoss);
		// } catch (QAntCreateRoomException e) {
		// e.printStackTrace();
		// }

	}

}
