package com.creants.muext.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.dao.HeroStageRepository;
import com.creants.muext.entities.world.HeroStage;
import com.creants.muext.entities.world.Stage;

/**
 * Tham khảo trả về response thế nào SFSResponseApi
 * 
 * @author LamHa
 */
public class JoinChapterRequestHandler extends BaseClientRequestHandler {
	private HeroStageRepository stageRepository;


	public JoinChapterRequestHandler() {
		stageRepository = Creants2XApplication.getBean(HeroStageRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer chapterId = params.getInt("cid");
		if (chapterId == null) {
			// TODO throw exception
			return;
		}

		String gameHeroId = user.getName();
		// reset lại số lần được farm nếu qua ngày
		List<HeroStage> findStages = stageRepository.findStages(gameHeroId, chapterId, getStartOfDateMilis());
		if (findStages != null && findStages.size() > 0) {
			for (HeroStage heroStage : findStages) {
				heroStage.setSweepTimes(0);
				heroStage.setLastestSweepTime(System.currentTimeMillis());
			}
			stageRepository.save(findStages);
		}

		List<HeroStage> stages = stageRepository.findStages(gameHeroId, chapterId);

		params = QAntObject.newInstance();
		IQAntArray stageArr = QAntArray.newInstance();
		for (Stage stage : stages) {
			stageArr.addQAntObject(QAntObject.newFromObject(stage));
		}
		params.putQAntArray("stages", stageArr);
		params.putInt("cid", chapterId);

		send(ExtensionEvent.CMD_JOIN_CHAPTER, params, user);
	}


	private long getStartOfDateMilis() {
		return DateUtils.truncate(new Date(), Calendar.DATE).getTime();
	}

}
