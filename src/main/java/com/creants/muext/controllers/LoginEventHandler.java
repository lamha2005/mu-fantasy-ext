package com.creants.muext.controllers;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.QAntEventParam;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;

/**
 * @author LamHM
 *
 */
public class LoginEventHandler extends BaseServerEventHandler {
	private static final String NEW_LOGIN_NAME = "$FS_NEW_LOGIN_NAME";
	
	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		System.out.println("Fire event login");
		IQAntObject outData = (IQAntObject) event.getParameter(QAntEventParam.LOGIN_OUT_DATA);
		outData.putUtfString(NEW_LOGIN_NAME, "test");
	}

}
