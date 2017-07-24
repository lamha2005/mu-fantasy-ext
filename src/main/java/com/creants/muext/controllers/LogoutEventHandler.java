package com.creants.muext.controllers;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.admin.managers.AdminManager;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class LogoutEventHandler extends BaseServerEventHandler {
	private AdminManager adminManager;


	public LogoutEventHandler() {
		adminManager = Creants2XApplication.getBean(AdminManager.class);
	}


	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		QAntTracer.debug(this.getClass(), "DisconnectEventHandler ..........");
		adminManager.decrAndNotifyCCU();
	}

}
