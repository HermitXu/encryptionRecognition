package com.spinfosec.mq.consumer;

import java.util.List;

import com.spinfosec.dao.entity.SpTaskFiles;
import com.spinfosec.dao.entity.SpTaskSipped;
import com.spinfosec.dto.pojo.system.tatic.TaskFilesMsg;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.spinfosec.dao.TaskCenterDao;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName PromoteActConsumer
 * @Description: 〈任务处理成功或失败文件上报消费者〉
 * @date 2018/11/9
 * All rights Reserved, Designed By SPINFO
 */
@Component
public class TaskFileInfoConsumer
{
	private Logger log = LoggerFactory.getLogger(TaskFileInfoConsumer.class);

	@Autowired
	private TaskCenterDao taskCenterDao;

	/**
	 * 客户端消费
	 * @param content 接受消息内容
	 */
	@JmsListener(destination = "SP_MQ_TASK_FILES", containerFactory = "")
	public void receiveFileInfo(String content)
	{
		log.info("开始读取任务队列消息！");
		List<TaskFilesMsg> taskFilesList = null;
		try
		{
			log.debug("任务队列接收信息:" + content);
			taskFilesList = JSON.parseArray(content, TaskFilesMsg.class);
		}
		catch (Exception e)
		{
			log.debug("任务消息读取失败：" + e.getMessage());
		}
		log.debug("开始处理任务队列信息!");
		for (TaskFilesMsg taskFilesMsg : taskFilesList)
		{
			// 处理成功文件
			SpTaskFiles successFile = taskFilesMsg.getSuccessFile();
			if (null != successFile && StringUtils.isNotEmpty(successFile.getId()))
			{
				taskCenterDao.saveTaskSuccessFile(successFile);
			}
			// 处理失败文件
			SpTaskSipped failedFile = taskFilesMsg.getFailedFile();
			if (null != failedFile && StringUtils.isNotEmpty(failedFile.getId()))
			{
				taskCenterDao.saveTaskFailedFile(failedFile);
			}
		}

	}
}
