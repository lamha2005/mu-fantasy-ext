package com.creants.muext.controllers;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.om.UserInfo;
import com.creants.muext.repository.UserRepository;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class ChatRequestHandler extends BaseClientRequestHandler {
	private UserRepository userRepository;


	public ChatRequestHandler() {
		System.out.println("************* CHAT REQUEST HANDLER ******************");
		userRepository = Creants2XApplication.getBean(UserRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		String message = params.getUtfString("msg");
		System.out.println("=====================> " + message);
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("lamha");
		UserInfo save = userRepository.save(userInfo);
		System.out.println("save success");
	}

}

