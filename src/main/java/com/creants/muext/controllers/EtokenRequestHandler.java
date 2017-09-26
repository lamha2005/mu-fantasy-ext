package com.creants.muext.controllers;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.om.Etoken;
import com.creants.muext.services.EtokenService;

/**
 * @author LamHM
 *
 */
public class EtokenRequestHandler extends BaseClientRequestHandler {

	private EtokenService etokenService;


	public EtokenRequestHandler() {
		etokenService = Creants2XApplication.getBean(EtokenService.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Etoken token = etokenService.getToken(user.getName());
		params.putQAntObject("etoken", QAntObject.newFromObject(token));
		send(ExtensionEvent.CMD_GET_ETOKEN, params, user);
	}

}
