package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.config.BossEventConfig;
import com.creants.muext.entities.event.BossEvent;
import com.creants.muext.managers.jobs.NotificationBossEventJob;

/**
 * @author LamHM
 *
 */
@Service
public class BossEventManager implements InitializingBean {
	@Autowired
	HeroClassManager heroClassManager;

	private List<String> bossEvents;


	@Override
	public void afterPropertiesSet() throws Exception {
		bossEvents = new ArrayList<>();

		try {
			QAntTracer.debug(this.getClass(), "///////////////////////// Create Boss Events /////////////////////////");
			List<BossEvent> events = BossEventConfig.getInstance().getEvents();

			for (BossEvent bossEvent : events) {
				QAntTracer.debug(this.getClass(), "- Boss event " + bossEvent.toString());

				// báo cho player trước khi boss xuất hiện
				JobDetail job = JobBuilder.newJob(NotificationBossEventJob.class)
						.withIdentity("BossEventName_" + bossEvent.getIndex(), "BossEventGroup").build();
				JobDataMap jobDataMap = job.getJobDataMap();
				jobDataMap.put("event_index", bossEvent.getIndex());

				Trigger notificationTrigger = TriggerBuilder.newTrigger()
						.withIdentity("NotificationTrigger_" + bossEvent.getIndex(), "BossEventGroup")
						.withSchedule(CronScheduleBuilder.cronSchedule(bossEvent.getNotificationTime())).build();

				Scheduler scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.start();
				scheduler.scheduleJob(job, notificationTrigger);

				// boss xuất hiện
			}

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Lấy danh sách bot event đang mở
	 * 
	 * @return
	 */
	public List<String> getBossEvents() {
		return bossEvents;
	}

}
