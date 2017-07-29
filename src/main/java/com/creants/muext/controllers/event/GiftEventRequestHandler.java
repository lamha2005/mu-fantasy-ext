package com.creants.muext.controllers.event;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.entities.event.GiftEventBase;

/**
 * @author LamHM
 *
 */
public class GiftEventRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		send("cmd_get_events", buildResponse(), user);
	}


	private IQAntObject buildResponse() {
		List<GiftEventBase> events = GiftEventConfig.getInstance().getEvents();

		IQAntObject response = QAntObject.newInstance();

		for (GiftEventBase giftEventBase : events) {

		}

		response.putQAntObject("events", QAntObject.newFromObject(events));
		return response;
	}

}
