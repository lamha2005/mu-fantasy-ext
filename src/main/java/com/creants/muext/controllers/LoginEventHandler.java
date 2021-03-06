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
	private static final String NAME_BY_SERVER = "$FS_NAME_BY_SERVER";


	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		IQAntObject outData = (IQAntObject) event.getParameter(QAntEventParam.LOGIN_OUT_DATA);
		// outData.putUtfString(NEW_LOGIN_NAME, "test");

		// outData.putUtfStringArray("zones", Arrays.asList("Mu Fantasy S1"));
		outData.putUtfString("media_url", "192.168.1.22:8889");
		outData.putLong("server_time", System.currentTimeMillis());
		// trường hợp là name_by_server thì login name sẽ là servername#userId
		outData.putBool(NAME_BY_SERVER, true);
	}

}
