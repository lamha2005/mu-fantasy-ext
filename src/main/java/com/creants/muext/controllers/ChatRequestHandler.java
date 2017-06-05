package com.creants.muext.controllers;

import java.util.Collection;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class ChatRequestHandler extends BaseClientRequestHandler {

	public ChatRequestHandler() {
		System.out.println("************* CHAT REQUEST HANDLER ******************");
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Collection<QAntUser> userList = getParentExtension().getParentZone().getUserList();
		QAntUser userByName = getApi().getUserByName(user.getName());
		String message = params.getUtfString("msg");
		System.out.println("message: " + message);
	}

}
