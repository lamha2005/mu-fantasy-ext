package com.creants.muext.controllers.event;

import java.util.List;

import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.io.Request;
import com.creants.muext.BossEventExtension;

/**
 * @author LamHM
 *
 */
public class HitBossEventRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		BossEventExtension extension = (BossEventExtension) getParentExtension();
		int dam = params.getInt("dam");
		params.putUtfString("game_hero_id", user.getName());

		int monsterHp = extension.hit(user.getName(), dam);
		Room parentRoom = extension.getParentRoom();
		List<QAntUser> userList = parentRoom.getUserList();

		IQAntObject response = QAntObject.newInstance();
		response.putInt("monster_hp", monsterHp);

		getApi().sendExtensionResponse("cmd_bot_event_hit", response, userList, parentRoom);

		extension.putMessage(new Request());
	}

}
