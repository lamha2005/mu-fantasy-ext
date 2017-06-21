package com.creants.muext.admin.controllers;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.admin.managers.AdminManager;
import com.creants.muext.admin.om.Admin;
import com.creants.muext.admin.om.AdminRole;

/**
 * @author LamHM
 *
 */
public class AdminLoginRequestHandler extends BaseClientRequestHandler {
	private static final long SUPER_ADMIN_ID = 313;
	private AdminManager adminManager;


	public AdminLoginRequestHandler() {
		adminManager = Creants2XApplication.getBean(AdminManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		adminManager = Creants2XApplication.getBean(AdminManager.class);
		long creantsUserId = user.getCreantsUserId();
		if (adminManager.containUser(creantsUserId)) {
			adminManager.login(user);
			return;
		}

		if (creantsUserId == SUPER_ADMIN_ID) {
			Admin superAdmin = new Admin();
			superAdmin.setId(creantsUserId);
			superAdmin.setAvatar(user.getAvatar());
			superAdmin.setFullName("Lam Ha");
			superAdmin.setCreateTime(System.currentTimeMillis());
			superAdmin.setRole(AdminRole.SUPER_ADMIN);
			adminManager.registerAdmin(superAdmin);
			adminManager.login(user);
		}
	}

}
