package com.creants.muext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creants.creants_2x.core.QAntEventType;
import com.creants.creants_2x.core.exception.QAntCreateRoomException;
import com.creants.creants_2x.core.extension.QAntExtension;
import com.creants.creants_2x.core.setting.CreateRoomSettings;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.admin.controllers.AdminLoginRequestHandler;
import com.creants.muext.admin.controllers.CCURequestHandler;
import com.creants.muext.admin.managers.AdminManager;
import com.creants.muext.config.BossEventConfig;
import com.creants.muext.controllers.ChatRequestHandler;
import com.creants.muext.controllers.CreateRoomRequestHandler;
import com.creants.muext.controllers.DisconnectEventHandler;
import com.creants.muext.controllers.GetQuestListRequestHandler;
import com.creants.muext.controllers.HeroListRequestHandler;
import com.creants.muext.controllers.JoinChapterRequestHandler;
import com.creants.muext.controllers.JoinGameRequestHandler;
import com.creants.muext.controllers.JoinZoneEventHandler;
import com.creants.muext.controllers.LoginEventHandler;
import com.creants.muext.controllers.LogoutEventHandler;
import com.creants.muext.controllers.QuestClaimRequestHandler;
import com.creants.muext.controllers.StageFinishRequestHandler;
import com.creants.muext.controllers.StageRequestHandler;
import com.creants.muext.controllers.SummonRequestHandler;
import com.creants.muext.controllers.UpdateBattleTeamRequestHandler;
import com.creants.muext.controllers.event.GiftEventRequestHandler;
import com.creants.muext.controllers.event.JoinBossEventRequestHandler;
import com.creants.muext.entities.event.BossEvent;

/**
 * @author LamHM
 *
 */
public class MuFantasyExtension extends QAntExtension {

	@Override
	public void init() {
		QAntTracer.debug(this.getClass(), "========================= START MU =========================");
		addEventRequestHandler();

		try {
			loadBossEventExtension();
		} catch (QAntCreateRoomException e) {
			e.printStackTrace();
		}

		Creants2XApplication.getBean(AdminManager.class).initQAntApi();
		QAntTracer.debug(this.getClass(), "========================= MU STARTED =========================");
	}


	private void addEventRequestHandler() {
		addEventHandler(QAntEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(QAntEventType.USER_LOGOUT, LogoutEventHandler.class);
		addEventHandler(QAntEventType.USER_JOIN_ZONE, JoinZoneEventHandler.class);
		addEventHandler(QAntEventType.USER_DISCONNECT, DisconnectEventHandler.class);

		addRequestHandler("cmd_chat", ChatRequestHandler.class);
		addRequestHandler("cmd_join_game", JoinGameRequestHandler.class);
		addRequestHandler("cmd_create_room", CreateRoomRequestHandler.class);
		addRequestHandler("cmd_get_quests", GetQuestListRequestHandler.class);
		addRequestHandler("cmd_join_stage", StageRequestHandler.class);
		addRequestHandler("cmd_stage_finish", StageFinishRequestHandler.class);
		addRequestHandler("cmd_join_chapter", JoinChapterRequestHandler.class);
		addRequestHandler("cmd_quest_claim", QuestClaimRequestHandler.class);
		addRequestHandler("cmd_boss_event_join", JoinBossEventRequestHandler.class);
		addRequestHandler("cmd_summon", SummonRequestHandler.class);
		addRequestHandler("cmd_get_events", GiftEventRequestHandler.class);
		addRequestHandler("cmd_heroes", HeroListRequestHandler.class);
		addRequestHandler("cmd_upd_battle_team", UpdateBattleTeamRequestHandler.class);

		addRequestHandler("cmd_ccu", CCURequestHandler.class);
		addRequestHandler("cmd_admin_login", AdminLoginRequestHandler.class);
	}


	private void loadBossEventExtension() throws QAntCreateRoomException {
		List<BossEvent> events = BossEventConfig.getInstance().getEvents();
		for (BossEvent bossEvent : events) {
			CreateRoomSettings roomSettings = new CreateRoomSettings();
			roomSettings.setName(bossEvent.getEventName());
			roomSettings.setGroupId("Boss Event");
			roomSettings.setGame(true);
			roomSettings.setMaxUsers(1000);
			roomSettings.setExtension(new CreateRoomSettings.RoomExtensionSettings("BossExtension",
					"com.creants.muext.BossEventExtension"));

			Map<Object, Object> roomProperties = new HashMap<>();
			roomProperties.put("open", false);
			roomProperties.put("monsterIndex", bossEvent.getMonsterIndex());

			roomSettings.setRoomProperties(roomProperties);
			getApi().createRoom(getParentZone(), roomSettings, null, false, null, false, false);
		}

	}
}
