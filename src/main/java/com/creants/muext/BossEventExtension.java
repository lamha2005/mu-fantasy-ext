package com.creants.muext;

import com.creants.creants_2x.core.extension.QAntExtension;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.controllers.StageRequestHandler;

/**
 * @author LamHM
 *
 */
public class BossEventExtension extends QAntExtension {

	@Override
	public void init() {
		QAntTracer.debug(this.getClass(), "========================= START BOT EVENT =========================");
		addEventRequestHandler();
		QAntTracer.debug(this.getClass(), "========================= BOT EVENT STARTED =========================");
	}


	private void addEventRequestHandler() {
		addRequestHandler("cmd_boss_join_event", StageRequestHandler.class);
	}

}
