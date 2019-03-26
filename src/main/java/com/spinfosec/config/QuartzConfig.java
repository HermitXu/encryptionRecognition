package com.spinfosec.config;

import com.spinfosec.task.SysArchiveLogAlarmTask;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName QuartzConfig
 * @Description: 〈配置定时任务〉
 * @date 2019/1/23
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Configuration
public class QuartzConfig
{
	// 配置系统日志容量告警监控定时任务
	@Bean(name = "archiveLogAlarmTaskJobDetail")
	public MethodInvokingJobDetailFactoryBean archiveLogAlarmTaskJobDetail(SysArchiveLogAlarmTask archiveLogAlarmTask)
	{
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		// 是否并发执行
		jobDetail.setConcurrent(false);
		// 为需要执行的实体类对应的对象
		jobDetail.setTargetObject(archiveLogAlarmTask);
		// 需要执行的方法
		jobDetail.setTargetMethod("checkCount");
		return jobDetail;
	}

	// 配置系统日志容量告警监控触发器
	@Bean(name = "archiveLogAlarmTaskTrigger")
	public CronTriggerFactoryBean firstTrigger(JobDetail archiveLogAlarmTaskJobDetail)
	{
		CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
		trigger.setJobDetail(archiveLogAlarmTaskJobDetail);
		// cron表达式 间隔多少时间使用SimpleTriggerFactoryBean 中的setRepeatInterval(5000)（每5秒执行一次）
		// 每天早上8点15执行
		trigger.setCronExpression("0 15 8 ? * *");
		return trigger;
	}

	// 配置Scheduler
	@Bean(name = "scheduler")
	public SchedulerFactoryBean schedulerFactory(Trigger archiveLogAlarmTaskTrigger)
	{
		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		// 延时启动，应用启动1秒后
		bean.setStartupDelay(1);
		// 注册触发器
		bean.setTriggers(archiveLogAlarmTaskTrigger);
		return bean;
	}

}
