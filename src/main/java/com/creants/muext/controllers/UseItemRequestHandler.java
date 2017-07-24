package com.creants.muext.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.QuestStatsRepository;

/**
 * @author LamHM
 *
 */
public class UseItemRequestHandler extends BaseClientRequestHandler {

	@Autowired
	private QuestStatsRepository questStateRepository;


	public UseItemRequestHandler() {
		questStateRepository = Creants2XApplication.getBean(QuestStatsRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		long itemId = params.getLong("id");
	}

}
