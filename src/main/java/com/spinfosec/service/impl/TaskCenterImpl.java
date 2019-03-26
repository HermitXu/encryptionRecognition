package com.spinfosec.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.TaskCenterDao;
import com.spinfosec.dao.entity.SpTaskFiles;
import com.spinfosec.dao.entity.SpTaskSipped;
import com.spinfosec.dto.pojo.system.OperateMsg;
import com.spinfosec.dto.pojo.system.TaskCenterRsp;
import com.spinfosec.service.srv.ITaskCenterSrv;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.MQUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TaskCenterImpl
 * @Description: 〈任务中心业务实现类〉
 * @date 2018/10/26
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("taskCenterSrv")
public class TaskCenterImpl implements ITaskCenterSrv
{
	private Logger log = LoggerFactory.getLogger(TaskCenterImpl.class);

	@Autowired
	private TaskCenterDao taskCenterDao;

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;

	/**
	 * 分页查询任务中心信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<TaskCenterRsp> queryTaskCentByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<TaskCenterRsp> taskCenterRspList = taskCenterDao.queryTaskCent(queryMap);
		PageInfo<TaskCenterRsp> pageInfo = new PageInfo<>(taskCenterRspList);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageInfo;
	}

	/**
	 * 分页查询任务处理成功文件
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SpTaskFiles> querySuccessFileByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SpTaskFiles> taskFiles = taskCenterDao.queryTaskSuccessFile(queryMap);
		PageInfo<SpTaskFiles> pageInfo = new PageInfo<>(taskFiles);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageInfo;
	}

	/**
	 * 分页查询任务处理失败文件
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SpTaskSipped> queryTaskFailFileByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SpTaskSipped> taskFiles = taskCenterDao.queryTaskFailFile(queryMap);
		PageInfo<SpTaskSipped> pageInfo = new PageInfo<>(taskFiles);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageInfo;
	}

	/**
	 * 根据策略ID获取更多的任务信息
	 * @param quereMap
	 * @return
	 */
	@Override
	public List<TaskCenterRsp> getTaskInfoByDisId(Map<String, Object> quereMap)
	{
		return taskCenterDao.getTaskInfoByDisId(quereMap);
	}

	/**
	 * 根据ID删除任务
	 * @param ids
	 */
	@Override
	public void deleteTaskById(List<String> ids)
	{
		for (String id : ids)
		{
			taskCenterDao.deleteTaskById(id);

			// 删除相关的处理成功或失败信息
			Thread delThread = new Thread()
			{
				@Override
				public void run()
				{
					taskCenterDao.deleteTaskSuccessFile(id);
					taskCenterDao.deleteTaskFailedFile(id);
				}
			};
			delThread.start();

		}

	}

	/**
	 * 获取任务总数
	 * @return
	 */
	@Override
	public int getTaskNum(String status, String userId)
	{
		return taskCenterDao.getTaskNum(status, userId);
	}

	/**
	 * 获取一周前的总任务数
	 * @return
	 */
	@Override
	public int getTaskNumTimeAgo(String status, String userId)
	{
		return taskCenterDao.getTaskNumTimeAgo(status, userId);
	}

	/**
	 * 启动任务
	 * @param jobId 任务ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public OperateMsg startJob(String jobId) throws Exception
	{
		String msg = "{\"type\": \"startJob\",\"content\": [{\"jobId\": \"" + jobId + "\"}]}";
		log.debug("startJob send :" + msg);
		String res = MQUtil.sendMessage(connectionFactory, Contants.JOB_OPERATE, msg, 2 * 60 * 1000);
		// 解析返回内容
		log.debug("startJob return :" + res);
		OperateMsg result = JSON.parseObject(res, OperateMsg.class);
		return result;
	}

	/**
	 * 暂停任务
	 * @param jobId 任务ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public OperateMsg pauseJob(String jobId) throws Exception
	{
		String msg = "{\"type\": \"pauseJob\",\"content\": [{\"jobId\": \"" + jobId + "\"}]}";
		log.debug("pauseJob send :" + msg);
		String res = MQUtil.sendMessage(connectionFactory, Contants.JOB_OPERATE, msg, 2 * 60 * 1000);
		// 解析返回内容
		log.debug("pauseJob return :" + res);
		OperateMsg result = JSON.parseObject(res, OperateMsg.class);
		return result;
	}

	/**
	 * 任务启动停止时修改资源的operationStatus值
	 * @param jobId
	 * @param taskId
	 * @param operationStatus 操作状态值
	 */
	@Override
	public void updateTaskOperationStatus(String jobId,String taskId, String operationStatus)
	{
		taskCenterDao.updateTaskOperationStatus(jobId,taskId, operationStatus);
	}
}
