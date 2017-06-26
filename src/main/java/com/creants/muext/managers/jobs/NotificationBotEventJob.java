package com.creants.muext.managers.jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.entities.Zone;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.config.BossEventConfig;
import com.creants.muext.entities.event.BossEvent;

/**
 * @author LamHM
 *
 */
public class NotificationBotEventJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int eventIndex = context.getJobDetail().getJobDataMap().getInt("event_index");

		IQAntObject message = QAntObject.newInstance();
		BossEvent event = BossEventConfig.getInstance().getEvent(eventIndex);

		Date nextFireTime = context.getNextFireTime();
		int nextDate = nextFireTime.getDate();
		int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		IQAntObject params = null;

		// xuất hiện boss
		if (nextDate > currentDate) {
			params = QAntObject.newInstance();
			params.putUtfString("event_group", "boss");
			params.putUtfString("action", "unlock");
			params.putBool("open", true);
			params.putInt("boss_event_index", event.getIndex());

			message.putUtfString("msg", String.format("%s đã mở.", event.getEventName()));
		} else {
			int deltaTime = nextFireTime.getMinutes() - Calendar.getInstance().get(Calendar.MINUTE);
			message.putUtfString("msg", String.format("%s sẽ mở sau %d phút nữa.", event.getEventName(), deltaTime));
		}

		List<Zone> zoneList = QAntServer.getInstance().getZoneManager().getZoneList();
		for (Zone zone : zoneList) {
			Collection<QAntUser> userList = zone.getUserList();
			if (userList == null || userList.size() <= 0) {
				continue;
			}

			if (params != null) {
				Room room = zone.getRoomByName(event.getEventName());
				room.setProperty("open", true);
				zone.getExtension().send("cmd_event", message, new ArrayList<>(userList));
			}

			zone.getExtension().send("cmd_system_msg", message, new ArrayList<>(userList));
		}

	}

}
