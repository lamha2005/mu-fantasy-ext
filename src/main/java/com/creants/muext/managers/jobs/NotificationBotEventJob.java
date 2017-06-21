package com.creants.muext.managers.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.entities.Zone;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHM
 *
 */
public class NotificationBotEventJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String name = context.getJobDetail().getKey().getName();
		IQAntObject params = QAntObject.newInstance();
		params.putUtfString("msg", "BOSS Cốt Tinh Vương sẽ xuất hiện sau 5 phút nữa.");

		List<Zone> zoneList = QAntServer.getInstance().getZoneManager().getZoneList();
		for (Zone zone : zoneList) {
			Collection<QAntUser> userList = zone.getUserList();
			if (userList == null || userList.size() <= 0) {
				continue;
			}

			zone.getExtension().send("cmd_boss_event_notification", params, new ArrayList<>(userList));
		}

	}

}
