package com.creants.muext.controllers.event;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.QAntEventParam;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.BossEventExtension;

/**
 * @author LamHM
 *
 */
public class JoinRoomBossEventHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		BossEventExtension parentExt = (BossEventExtension) getParentExtension();
		QAntUser user = (QAntUser) event.getParameter(QAntEventParam.USER);
		parentExt.join(user);

		send("cmd_boss_event_join", parentExt.getData(), user);
	}

}
