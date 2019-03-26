package com.spinfosec.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.spinfosec.dao.TaskCenterDao;
import com.spinfosec.dao.entity.SpTask;
import com.spinfosec.encryptAnalyze.JobFileTypeAlgorithmHelper;
import com.spinfosec.encryptAnalyze.analyor.DatabaseAnalyze;
import com.spinfosec.encryptAnalyze.analyor.FileSystemAnalyze;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName PromoteActConsumer
 * @Description: 〈任务信息上报消费者〉
 * @date 2018/11/9
 * All rights Reserved, Designed By SPINFO
 */
@Component
public class TaskInfoConsumer
{
	private Logger log = LoggerFactory.getLogger(TaskInfoConsumer.class);

	@Autowired
	private TaskCenterDao taskCenterDao;

	/**
	 * 客户端消费
	 * @param content 接受消息内容
	 */
	@JmsListener(destination = "SP_MQ_TASK_INFO", containerFactory = "")
	public void receiveTaskInfo(String content)
	{
		log.debug("【任务信息】开始读取任务中心任务信息队列消息！");
		log.debug("【任务信息】队列接收信息:" + content);
		List<SpTask> taskInfoList = null;
		try
		{
			log.debug("【任务信息】队列接收信息:" + content);
			taskInfoList = JSON.parseArray(content, SpTask.class);
		}
		catch (Exception e)
		{
			log.debug("【任务信息】读取失败：" + e.getMessage());
		}
		if (null == taskInfoList)
		{
			log.debug("【任务信息】读取失败, 任务信息列表为空！");
			return;
		}
		log.debug("开始处理【任务信息】!");
		for (SpTask task : taskInfoList)
		{
			String taskId = task.getId();
			log.info("接收到的taskId:" + taskId);
			if (StringUtils.isEmpty(task.getJobId()))
			{
				log.info("接收的jobId为空，消息作废");
				return;
			}
			if (StringUtils.isEmpty(taskId))
			{
				if (StringUtils.isEmpty(task.getJobId()))
				{
					log.info("接收的jobId为空，消息作废");
					return;
				}
				// 如果taskId为空，则通过jobid获取相关任务中最新的那条记录
				SpTask taskFromDb = taskCenterDao.getIsLastTaskByJobId(task.getJobId());
				taskFromDb.setFailReason(task.getFailReason());
				taskFromDb.setStatus(task.getStatus());
				taskFromDb.setEndTime(task.getEndTime());
				taskFromDb.setResult(task.getResult());
				task = taskFromDb;
				taskId = taskFromDb.getId();
			}

			SpTask spTask = taskCenterDao.getTaskById(taskId);
			if (null != spTask)
			{
				log.info("查询到的task:" + spTask.toString());
				if ("1".equals(spTask.getIsLast()))
				{
					if (null == task.getStartTime())
					{
						task.setStartTime(spTask.getStartTime());
					}
					task.setIsLast(spTask.getIsLast());
					log.info("更新task:" + task.toString());
					taskCenterDao.updateTask(task);
					log.info("task.getStatus() = " + task.getStatus());

					if ("100".equalsIgnoreCase(task.getStatus()))
					{
						// 任务完成，则清理策略下的类型和算法的缓存信息
						JobFileTypeAlgorithmHelper.getInstance().clearJobFileTypeAlgorithmMap(task.getJobId());
					}
				}
			}
			else
			{
				// 删除该策略下预置的任务信息
				taskCenterDao.deletePresetTask(task.getJobId());
				// 将该策略下的所有任务设置为历史任务 即isLast设置为'0'
				taskCenterDao.updateTaskIsLastByJobId(task.getJobId(), "0");
				task.setIsLast("1");
				log.debug("保存task:" + task.toString());
				taskCenterDao.saveTask(task);
			}
		}

		log.debug("处理任务队列信息结束!");
	}
}
