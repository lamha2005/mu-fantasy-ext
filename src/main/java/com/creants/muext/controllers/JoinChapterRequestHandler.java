package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.IResponse;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.WorldStatsRepository;
import com.creants.muext.entities.world.Stage;
import com.creants.muext.entities.world.WorldStats;
import com.creants.muext.util.UserHelper;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class JoinChapterRequestHandler extends BaseClientRequestHandler {
	private WorldStatsRepository worldRepository;


	public JoinChapterRequestHandler() {
		worldRepository = Creants2XApplication.getBean(WorldStatsRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer chapterId = params.getInt("cid");
		if (chapterId == null) {
			// TODO throw exception
			return;
		}

		String gameHeroId = UserHelper.getGameHeroId(user);
		WorldStats worldStats = worldRepository.findOne(gameHeroId);
		List<Stage> states = worldStats.getStates(chapterId);

		params = QAntObject.newInstance();
		IQAntArray stages = QAntArray.newInstance();
		for (Stage stage : states) {
			stages.addQAntObject(QAntObject.newFromObject(stage));
		}
		params.putQAntArray("stages", stages);
		params.putInt("cid", chapterId);
		sendExtResponse("cmd_join_chapter", params, user);
	}


	public void sendExtResponse(String cmdName, IQAntObject params, QAntUser recipient) {
		IQAntObject resObj = QAntObject.newInstance();
		resObj.putUtfString("c", cmdName);
		resObj.putQAntObject("p", (params != null) ? params : new QAntObject());

		IResponse response = new Response();
		response.setId(SystemRequest.CallExtension.getId());
		response.setTargetController((byte) 1);
		response.setContent(resObj);
		response.setRecipients(recipient.getChannel());
		response.write();
	}

}
