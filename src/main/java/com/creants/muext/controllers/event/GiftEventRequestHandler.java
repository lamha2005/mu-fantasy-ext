package com.creants.muext.controllers.event;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.entities.event.GiftEventBase;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class GiftEventRequestHandler extends BaseClientRequestHandler {
	private static final String CMD = "cmd_get_events";
	private static final int GET_CATEGORY_EVENT = 1;


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer revision = params.getInt("rvs");
		if (revision == null) {
			sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.LACK_OF_INFOMATION), user);
			return;
		}

		Integer action = params.getInt("act");
		if (action == null) {
			action = -1;
		}

		if (revision != GiftEventConfig.getInstance().getRevison()) {
			send("cmd_get_events", buildResponse(), user);
		}
	}


	private IQAntObject buildResponse() {
		List<GiftEventBase> events = GiftEventConfig.getInstance().getEvents();

		IQAntObject response = QAntObject.newInstance();
		IQAntArray eventArr = QAntArray.newInstance();
		for (GiftEventBase giftEventBase : events) {
			eventArr.addQAntObject(QAntObject.newFromObject(giftEventBase));
		}

		response.putQAntArray(CMD, eventArr);
		return response;
	}

}
