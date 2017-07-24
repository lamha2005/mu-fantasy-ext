package com.creants.muext.controllers;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class GetFriendListRequestHandler extends BaseClientRequestHandler {

	public GetFriendListRequestHandler() {
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {

	}

}
