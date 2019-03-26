package com.spinfosec.dao.tactic;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName JobScheduleDao
 * @Description: 〈任务调度部署持久层〉
 * @date 2018/11/12
 * All rights Reserved, Designed By SPINFO
 */
public interface JobScheduleDao
{

	/**
	 * 根据ID获取数据库类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getDbDiscoveryTaskInfo(List<String> ids);

	/**
	 * 根据ID获取文件共享类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getFileDiscoveryTaskInfo(List<String> ids);

	/**
	 * 根据ID获取FTP类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getFtpDiscoveryTaskInfo(List<String> ids);

	/**
	 * 根据ID获取Linux类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getSftpDiscoveryTaskInfo(List<String> ids);

	/**
	 * 根据ID获取SharePoint类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getSharePointDiscoveryTaskInfo(List<String> ids);

	/**
	 * 根据ID获取Lotus类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getLotusDiscoveryTaskInfo(List<String> ids);

	/**
	 * 根据ID获取Exchange类型策略信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getExchangeDiscoveryTaskInfo(List<String> ids);

	/**
	 * 数据库扫描目标、Lotus扫描目标信息
	 * @param jobId
	 * @return
	 */
	List<Map<String, Object>> dbLoutsTargetInfo(String jobId);

	/**
	 * 根据策略ID获取主机资源ID
	 * @param dis
	 * @return
	 */
	String getTargetResIdByDisId(String dis);

	/**
	 * 查询资源下的所有模式或数据库
	 * @param targetResId
	 * @return
	 */
	List<Map<String, Object>> getDatabaseNameOrSchemaName(String targetResId);

	/**
	 * 数据库扫描目标、Lotus扫描目标、Exchange扫描目标包含资源详细信息
	 * @param targetResId
	 * @return
	 */
	List<String> getDbLoTusExchageTargetDetail(String targetResId);

	/**
	 * 按天和按周
	 * @param scheduleId
	 * @return
	 */
	List<Map<String, Object>> getJbTime(String scheduleId);

	/**
	 * 持续执行和一次
	 * @param scheduleId
	 * @return
	 */
	List<Map<String, Object>> getJobOnce(String scheduleId);

	/**
	 * 根据主机资源ID和数据库名获取数据库表名
	 * @param resIdAndDatabaseName
	 * @return
	 */
	List<String> targetIncludeTables(Map<String, String> resIdAndDatabaseName);

	/**
	 * 根据策略ID获取文件共享主机资源信息
	 * @param jobId
	 * @return
	 */
	List<Map<String, Object>> getFileTarInfoByJobId(String jobId);

	/**
	 * 根据策略ID获取选择的共享文件路径
	 * @param jobId
	 * @return
	 */
	List<String> getFileSharePath(String jobId);

	/**
	 * 根据策略ID获取包含的文件类型
	 * @param jobId
	 * @return
	 */
	List<String> getFileIncludeFileType(String jobId);

	/**
	 * 根据策略ID获取排除的文件类型
	 * @param jobId
	 * @return
	 */
	List<String> getFileExcludeFileType(String jobId);

}
