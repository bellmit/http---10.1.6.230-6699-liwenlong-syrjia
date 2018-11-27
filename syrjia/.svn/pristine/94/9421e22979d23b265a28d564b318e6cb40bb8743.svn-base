package cn.syrjia.util;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Controller;

import cn.syrjia.quartz.ServerOverJob;

@Controller
public class QuartzManager {
	
	static SchedulerFactoryBean schedulerFactoryBean;

	private static final Logger logger = LogManager
			.getLogger(QuartzManager.class);

	public void start() {
		logger.info("start category update notify scheduler");
		schedulerFactoryBean.start();

	}

	@Autowired
	public void setSchedulerFactoryBean(
			SchedulerFactoryBean schedulerFactoryBean) {
		QuartzManager.schedulerFactoryBean = schedulerFactoryBean;
	}

	/**
	 * 添加任务
	 * 
	 * @param allPushMessage
	 */
	public static void addJob(String jobId,Date startTime,String orderNo) {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = getJobKey(jobId);
		try {
			if (scheduler.checkExists(jobKey)) {
				logger.info("all push job existed!:" + jobKey.getName());
				return;
			}
		} catch (SchedulerException e) {
			logger.error("get exception:" + e.getMessage(), e);
		}

		JobDataMap jobData = new JobDataMap();
		jobData.put("orderNo",orderNo);
		JobDetail serverOverJob = JobBuilder.newJob(ServerOverJob.class)
				.setJobData(jobData).withIdentity(jobKey).build();
		SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
		trigger.setName("trigger-" + jobKey.getName());
		trigger.setJobDetail(serverOverJob);
		trigger.setStartTime(startTime);
		trigger.setRepeatCount(0);
		trigger.afterPropertiesSet();

		try {
			schedulerFactoryBean.getScheduler().scheduleJob(serverOverJob,
					trigger.getObject());
		} catch (SchedulerException e) {
			logger.error("get exception when executing quartz job" + e);
		}

	}

	/**
	 * 删除任务
	 * 
	 * @param allPushMessage
	 * @throws Exception
	 */
	public static void deleteJob(String jobId){

		try {
			// 删除定时任务时 先暂停任务，然后再删除
			JobKey jobKey = getJobKey(jobId);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.pauseJob(jobKey);
			Boolean b=scheduler.deleteJob(jobKey);
			System.out.println(b);
		} catch (Exception e) {
			System.out.println("删除定时任务失败" + e);
			throw new RuntimeException("删除定时任务失败");
		}
	}

	/**
	 * 获取jobKey
	 * 
	 * @param allPushMessage
	 * @return
	 */
	public static JobKey getJobKey(String jobId) {

		return JobKey.jobKey(String.valueOf(jobId));
	}

	/**
	 * 更新定时任务
	 * 
	 * @param
	 * @param
	 * @throws Exception
	 */
	public static void updateJob(String jobId) throws Exception {
		try {
			TriggerKey triggerKey = getTriggerKey(String.valueOf(jobId));
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			CronTrigger trigger = (CronTrigger) scheduler
					.getTrigger(triggerKey);

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			System.out.println("更新定时任务失败" + e);
			throw new Exception("更新定时任务失败");
		}
	}

	/**
	 * 获取触发器key
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static TriggerKey getTriggerKey(String jobkey) {

		return TriggerKey.triggerKey(jobkey);
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param allPushMessage
	 * @throws Exception
	 */
	public static void pauseJob(String jobId) throws Exception {

		JobKey jobKey = JobKey.jobKey(String.valueOf(jobId));
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			System.out.println("暂停定时任务失败" + e);
			throw new Exception("暂停定时任务失败");
		}
	}

	/**
	 * 恢复任务
	 * 
	 * @param
	 * @param
	 * @param
	 * @throws Exception
	 */
	public static void resumeJob(String jobId) throws Exception {

		JobKey jobKey = JobKey.jobKey(String.valueOf(jobId));
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			System.out.println("恢复定时任务失败" + e);
			throw new Exception("恢复定时任务失败");
		}
	}

	/**
	 * 运行一次任务
	 * 
	 * @param allPushMessage
	 * @throws Exception
	 */
	public static void runOnce(String jobId) throws Exception {
		JobKey jobKey = JobKey.jobKey(String.valueOf(jobId));
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			System.out.println("运行任务失败" + e);
			throw new Exception("运行一次定时任务失败");
		}
	}
}
