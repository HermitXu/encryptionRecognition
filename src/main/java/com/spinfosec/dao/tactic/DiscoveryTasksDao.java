package com.spinfosec.dao.tactic;

import com.spinfosec.dao.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DiscoveryTasksDao
 * @Description: 〈扫描任务持久层〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
public interface DiscoveryTasksDao
{
	/**
	 * 保存主机资源
	 * @param targetRes
	 */
	void saveTargetRes(SpTargetRes targetRes);

	/**
	 * 保存列设置的相关信息
	 * @param columnResInfo
	 */
	void saveColumnResInfos(SpColumnResInfo columnResInfo);

	/**
	 * 根据主机资源ID删除列设置的相关信息
	 * @param targetId
	 */
	void deleteColumnResInfos(String targetId);

	/**
	 * 根据主机资源ID获取列设置的相关信息
	 * @param targetId
	 */
	List<SpColumnResInfo> getColumnResInfosByTargetId(String targetId);

	/**
	 * 根据主机资源ID和表名获取列名
	 * @param targetId
	 * @param tableName
	 * @return
	 */
	List<SpColumnResInfo> getColumnsByTargetIdAndTableName(@Param("targetId") String targetId,
			@Param("tableName") String tableName);

	/**
	 * 保存主机资源详情
	 * @param targetResDetail
	 */
	void saveTargetResDetail(SpTargetResDetail targetResDetail);

	/**
	 * 通过ID查询主机资源
	 * @param id
	 * @return
	 */
	SpTargetRes getTargetResById(String id);

	/**
	 * 根据密钥名称查找该密钥是否在被使用
	 * @param name
	 * @return
	 */
	List<String> getPublicKeyNameByName(String name);

	/**
	 * 保存时间调度
	 * @param schedulingData
	 */
	void saveSpSchedulingData(SpSchedulingData schedulingData);

	/**
	 * 保存扫描策略排除文件类型列表
	 * @param excldFileTypes
	 */
	void saveSpDscvrExcldFileTypes(SpDscvrExcldFileTypes excldFileTypes);

	/**
	 * 保存扫描策略包含文件类型列表
	 * @param incldFileTypes
	 */
	void saveSpDscvrIncldFileTypes(SpDscvrIncldFileTypes incldFileTypes);

	/**
	 * 通过扫描策略ID查询任务排除文件类型列表
	 * @param discoveryTaskId
	 * @return
	 */
	SpDscvrExcldFileTypes getExcldFileByDisId(String discoveryTaskId);

	/**
	 * 通过扫描策略ID查询任务包含文件类型列表
	 * @param discoveryTaskId
	 * @return
	 */
	SpDscvrIncldFileTypes getIncldFileByDisId(String discoveryTaskId);

	/**
	 * 根据ID获取时间调度信息
	 * @param id
	 * @return
	 */
	SpSchedulingData getSchedulingDataById(String id);

	/**
	 * 根据策略ID获取加密算法库和文件类型的关系
	 * @param disId
	 * @return
	 */
	List<SpAlgorithmFileType> getAlgorithmFileTypeByDisId(String disId);

	/**
	 * 根据ID删除时间调度信息
	 * @param id
	 * @return
	 */
	void deleteSchedulingDataById(String id);

	/**
	 * 通过schedulingDataId获取作业调度方案主键与星期之间的对应关系表
	 * @param schedulingDataId
	 * @return
	 */
	List<SpSchedulingDayTf> getDayTfByschedulingDataId(String schedulingDataId);

	/**
	 * 通过ID获取作业调度方案主键与星期之间的对应关系表
	 * @param id
	 * @return
	 */
	void deleteDayTfById(String id);

	/**
	 * 通过ID查询任务调度中天的信息]
	 * @param id
	 * @return
	 */
	SpDayTimeframe getDayTimeframeById(String id);

	/**
	 * 通过ID删除任务调度中天的信息]
	 * @param id
	 * @return
	 */
	void deleteDayTimeframeById(String id);

	/**
	 * 通过dayTimeframeId查询调度任务中整点时间信息
	 * @param dayTimeframeId
	 * @return
	 */
	List<SpDayTimeframeHours> getDayTimeframeHoursBydayTimeframeId(String dayTimeframeId);

	/**
	 * 通过dayTimeframeId删除调度任务中整点时间信息
	 * @param dayTimeframeId
	 */
	void deleteSpDayTimeframeHoursBydayTimeframeId(String dayTimeframeId);

	/**
	 *  保存天的信息
	 * @param dayTimeframe
	 */
	void saveSpDayTimeframe(SpDayTimeframe dayTimeframe);

	/**
	 * 保存作业调度方案主键与星期之间的对应关系表 SpSchedulingDayTf
	 * @param schedulingDayTf
	 */
	void saveSpSchedulingDayTf(SpSchedulingDayTf schedulingDayTf);

	/**
	 * 	// 保存整点的信息
	 * @param dayTimeframeHours
	 */
	void saveSpDayTimeframeHours(SpDayTimeframeHours dayTimeframeHours);

	/**
	 * 根据扫描策略ID删除排除文件类型列表
	 * @param discoveryTaskId
	 */
	void deleteExcldFileTypesBydiscoveryTaskId(String discoveryTaskId);

	/**
	 * 根据扫描策略ID删除包含文件类型列表
	 * @param discoveryTaskId
	 */
	void deleteIncldFileTypesBydiscoveryTaskId(String discoveryTaskId);

	/**
	 * 保存扫描策略主表
	 * @param discoveryTasks
	 */
	void saveDiscoveryTask(SpDiscoveryTasks discoveryTasks);

	/**
	 * 保存加密算法库文件类型关系表
	 * @param algorithmFileType
	 */
	void saveAlgorithmFileType(SpAlgorithmFileType algorithmFileType);

	/**
	 * 通过主机资源ID查询该主机资源详情
	 * @param targetId
	 * @return
	 */
	List<SpTargetResDetail> getTargetResDetailByTargetId(String targetId);

	/**
	 * 通过扫描策略ID查询扫描策略详细信息
	 * @param id
	 * @return
	 */
	SpDiscoveryTasks getDiscoveryTaskById(String id);

	/**
	 * 更新扫描策略
	 * @param discoveryTasks
	 */
	void updateDiscoveryTask(SpDiscoveryTasks discoveryTasks);

	/**
	 * 通过主机资源ID删除该主机资源详情
	 * @param targetId
	 * @return
	 */
	void deleteTargetResDetailByTargetId(String targetId);

	/**
	 * 通过ID删除主机资源
	 * @param id
	 * @return
	 */
	void deleteTargetResById(String id);

	/**
	 * 通过ID删除扫描策略主表
	 * @param id
	 */
	void deleteDiscoveryTaskById(String id);

	/**
	 * 根据策略ID删除该策略下的所有任务信息
	 * @param disId
	 */
	void deleteTaskInfoByDisId(String disId);

	/**
	 * 根据策略ID删除该策略下加密算法库文件类型关系表
	 * @param disId
	 */
	void deleteAlgorithmFileType(String disId);

	/**
	 * 获取所有文件类型
	 * @return
	 */
	List<SpPlcFileTypes> getFileType(Map<String, Object> queryMap);

	/**
	 * 获取常用文件类型
	 * @return
	 */
	List<SpPlcFileTypes> getCommonFileType();

	/**
	 * 添加/移除常用文件类型
	 * @param id
	 * @param state
	 */
	void updateFileTypeCommon(@Param("id") String id, @Param("state") String state);

	/**
	 * 获取加密算法库
	 * @return
	 */
	List<SpEncryptionAlgorithm> getEncryptionAlgorithm();

	/**
	 * 修改策略状态
	 * @param status
	 */
	void updateTaskStatus(@Param("status") String status, @Param("disId") String disId);

	/**
	 * 通过主机资源ID查询密码
	 * @param targetId
	 * @return
	 */
	String getTargetPassByTargeId(String targetId);

	/**
	 * 根据策略ID获取主机资源ID
	 * @param disId
	 * @return
	 */
	String getTargetIdByDisId(String disId);

	/**
	 * 根据用户ID获取其创建的所有任务ID
	 * @param userId
	 * @return
	 */
	List<String> getJobIdListByUserId(String userId);

}
