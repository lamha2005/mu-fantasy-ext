package com.creants.muext.controllers;

import java.util.Collection;

import com.creants.creants_2x.core.annotations.Instantiation;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.om.UserInfo;

/**
 * @author LamHM
 *
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class ChatRequestHandler extends BaseClientRequestHandler {
	private GameHeroRepository repository;


	public ChatRequestHandler() {
		System.out.println("************* CHAT REQUEST HANDLER ******************");
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Collection<QAntUser> userList = getParentExtension().getParentZone().getUserList();
		String message = params.getUtfString("msg");
		System.out.println("=====================> " + message);
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("lamha");
	}

}
