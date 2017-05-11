package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.WorldStatsRepository;
import com.creants.muext.entities.world.Stage;
import com.creants.muext.entities.world.WorldStats;

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

		WorldStats worldStats = worldRepository.findOne(user.getName());
		List<Stage> states = worldStats.getStates(chapterId);

		params = QAntObject.newInstance();
		IQAntArray stages = QAntArray.newInstance();
		for (Stage stage : states) {
			stages.addQAntObject(QAntObject.newFromObject(stage));
		}
		params.putQAntArray("stages", stages);

	}

}
