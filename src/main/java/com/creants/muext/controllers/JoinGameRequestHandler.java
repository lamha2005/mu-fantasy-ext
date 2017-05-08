package com.creants.muext.controllers;

import java.util.ArrayList;
import java.util.List;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.creants.creants_2x.core.controllers.SystemRequest;
import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.Response;
import com.creants.muext.om.RoomInfo;

/**
 * @author LamHa Tham khảo trả về response thế nào SFSResponseApi
 */
public class JoinGameRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		List<Room> uRooms = getParentExtension().getParentZone().getRoomList();

		QAntObject resObj = QAntObject.newInstance();
		resObj.putUtfString("c", "cmd_join_game");

		List<RoomInfo> rooms = new ArrayList<RoomInfo>();
		for (Room r : uRooms) {
			RoomInfo roomInfo = new RoomInfo();
			roomInfo.setId(r.getId());
			roomInfo.setName(r.getName());
			roomInfo.setMaxPlayer(4);
			roomInfo.setBetCoin(1000);
			roomInfo.setPlayerNo(3);
			rooms.add(roomInfo);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			String roomList = mapper.writeValueAsString(rooms);
			resObj.putUtfString("rl", roomList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Response response = new Response();
		response.setId(SystemRequest.CallExtension.getId());
		response.setTargetController((byte) 1);
		response.setContent(resObj);
		response.setRecipients(user.getChannel());
		response.write();
	}

}
