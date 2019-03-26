package com.spinfosec.service.srv;

import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpEventArchiveLog;
import com.spinfosec.dao.entity.SpSystemOperateArchiveLog;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.system.EventArchiveLogRsp;
import com.spinfosec.dto.pojo.system.SysOperateArchiveLogRsp;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IArchiveSrv
 * @Description: 〈日志归档接口〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
public interface IArchiveSrv
{
	/**
	 * 分页查询安全日志归档信息
	 * @param queryMap
	 * @return
	 */
	PageInfo<EventArchiveLogRsp> queryEventByPage(Map<String, Object> queryMap);

	/**
	 * 分页查询系统操作日志归档记录
	 * @param queryMap
	 * @return
	 */
	PageInfo<SysOperateArchiveLogRsp> querySysOperateByPage(Map<String, Object> queryMap);

	/**
	 *创建安全日志归档
	 * @param archive
	 * @throws TMCException
	 */
	SpEventArchiveLog createEventLogArchive(SpEventArchiveLog archive, String userId, String folderPath,
			SpSystemOperateLogInfo operateLogInfo)
			throws TMCException;

	/**
	 * 创建系统操作日志归档记录
	 * @param operateArchiveLog
	 * @param userId
	 * @param folderPath
	 * @return
	 */
	SpSystemOperateArchiveLog createSysOperateLogArchive(SpSystemOperateArchiveLog operateArchiveLog, String userId,
			String folderPath, SpSystemOperateLogInfo operateLogInfo);

	/**
	 * 根据ID获取安全日志归档记录
	 * @param id
	 * @return
	 */
	SpEventArchiveLog getAcrhiveLogById(String id);

	/**
	 * 根据ID获取系统操作日志归档记录
	 * @param id
	 * @return
	 */
	SpSystemOperateArchiveLog getOperateArchiveLogById(String id);

	/**
	 * 开始安全日志归档进程
	 * @param archive
	 * @param userId
	 */
	void startEventAcrhive(SpEventArchiveLog archive, String userId);

	/**
	 * 开始系统操作日志归档进程
	 * @param operateArchiveLog
	 */
	void startSysOperaRecover(SpSystemOperateArchiveLog operateArchiveLog, String userId);

	/**
	 * 恢复安全日志归档记录
	 * @param spEventArchiveLog
	 * @throws Exception
	 */
	void recoverEventLogArchive(SpEventArchiveLog spEventArchiveLog, SpSystemOperateLogInfo operateLogInfo);

	/**
	 * 恢复系统操作日志归档记录
	 * @param operateArchiveLog
	 */
	void recoverSysOperaAcrhive(SpSystemOperateArchiveLog operateArchiveLog, SpSystemOperateLogInfo operateLogInfo);

	/**
	 * 更新安全日志归档记录状态为恢复中
	 * @param id 事件日志id
	 * @throws Exception
	 */
	SpEventArchiveLog startEventRecover(String id) throws TMCException;

	/**
	 * 更新操作日志归档记录状态为恢复中
	 * @param id
	 * @return
	 */
	SpSystemOperateArchiveLog startSysOperaRecover(String id);

	/**
	 * 更新安全日志进程状态
	 * @param eventArchiveLog
	 */
	void updateEventArchiveLog(SpEventArchiveLog eventArchiveLog, String status);

	/**
	 * 更新系统操作日志归档
	 * @param operateArchiveLog
	 */
	void updateOperateArchiveLog(SpSystemOperateArchiveLog operateArchiveLog, String status);

	/**
	 * 删除安全日志归档记录
	 * @param ids
	 */
	void deleteEventArchiveLog(List<String> ids);

	/**
	 * 删除系统操作日志归档记录
	 * @param ids
	 */
	void deleteOperateArchiveLog(List<String> ids);

	/**
	 * 是否有正在归档或恢复的任务在执行中
	 * @param archiveStatus
	 * @param recoverStatus
	 * @return
	 */
	RspCode isArchiveOrRecoverRunning(String archiveStatus, String recoverStatus);

	/**
	 * 创建上传zip文件进行恢复记录
	 * @param logPath
	 * @param userId
	 * @return
	 */
	SpEventArchiveLog processArchiveLogRecord(String logPath, String userId);

}
