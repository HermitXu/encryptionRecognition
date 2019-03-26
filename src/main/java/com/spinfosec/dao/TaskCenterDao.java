package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpTask;
import com.spinfosec.dao.entity.SpTaskFiles;
import com.spinfosec.dao.entity.SpTaskSipped;
import com.spinfosec.dto.pojo.system.TaskCenterRsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TaskCenterDao
 * @Description: 〈任务中心信息成就才能〉
 * @date 2018/10/26
 * All rights Reserved, Designed By SPINFO
 */
public interface TaskCenterDao
{
	/**
	 * 根据条件查询任务中心信息
	 * @param queryMap
	 * @return
	 */
	List<TaskCenterRsp> queryTaskCent(Map<String, Object> queryMap);

	/**
	 * 查询任务处理成功事件
	 * @param queryMap
	 * @return
	 */
	List<SpTaskFiles> queryTaskSuccessFile(Map<String, Object> queryMap);

	/**
	 * 查询任务处理失败事件
	 * @param queryMap
	 * @return
	 */
	List<SpTaskSipped> queryTaskFailFile(Map<String, Object> queryMap);

	/**
	 * 任务启动停止时修改资源的operationStatus值
	 * @param jobId
	 * @param operationStatus 操作状态值
	 */
	void updateTaskOperationStatus(@Param("jobId") String jobId,@Param("taskId")String taskId,@Param("operationStatus") String operationStatus);

	/**
	 * 根据策略ID获取所有相关的任务信息
	 * @param quereMap
	 * @return
	 */
	List<TaskCenterRsp> getTaskInfoByDisId(Map<String, Object> quereMap);

	/**
	 * 保存任务信息
	 * @param task
	 */
	void saveTask(SpTask task);

	/**
	 * 保存任务处理成功的文件信息
	 * @param taskFiles
	 */
	void saveTaskSuccessFile(SpTaskFiles taskFiles);

	/**
	 * 保存任务处理失败的文件信息
	 * @param taskSipped
	 */
	void saveTaskFailedFile(SpTaskSipped taskSipped);

	/**
	 * 删除任务处理成功的文件信息
	 * @param taskId
	 */
	void deleteTaskSuccessFile(String taskId);

	/**
	 * 删除任务处理失败的文件信息
	 * @param taskId
	 */
	void deleteTaskFailedFile(String taskId);

	/**
	 * 更新任务信息
	 * @param task
	 */
	void updateTask(SpTask task);

	/**
	 * 根据ID删除对应任务
	 * @param id
	 */
	void deleteTaskById(String id);

	/**
	 * 根据ID获取任务信息
	 * @param taskId 任务ID
	 * @return
	 */
	SpTask getTaskById(String taskId);

	/**
	 * 通过策略ID获取最新的任务信息
	 * @param jobid 策略ID
	 * @return
	 */
	SpTask getIsLastTaskByJobId(String jobid);

	/**
	 * 根据策略ID修改任务isLast状态
	 * @param jobid
	 * @param isLast 0最新任务 1历史任务
	 */
	void updateTaskIsLastByJobId(@Param("jobid") String jobid, @Param("isLast") String isLast);

	/**
	 *
	 * @return
	 */
	List<SpTask> getTaskByConditionMap(Map<String, Object> conditionMap);

	/**
	 * 删除任务更新时预置的任务信息
	 * @param jobId
	 */
	void deletePresetTask(@Param("jobid") String jobId);
	/**
	 * 获取任务总数
	 * @return
	 */
	int getTaskNum(@Param("status") String status, @Param("userId") String userId);

	/**
	 * 获取一周前的总任务数
	 * @return
	 */
	int getTaskNumTimeAgo(@Param("status") String status, @Param("userId") String userId);

}
