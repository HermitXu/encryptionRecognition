package com.spinfosec.service.srv;

import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpDataBackups;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.SystemArchiveLogRsp;
import com.spinfosec.system.TMCException;

import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ISystemDataMngSrv
 * @Description: 〈系统备份〉
 * @date 2019/1/16
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface ISystemDataMngSrv
{
	/**
	 * 分页查询系统备份信息
	 * @param queryMap
	 * @return
	 */
	PageInfo<SystemArchiveLogRsp> querySpAdminsByPage(Map<String, Object> queryMap);

	/**
	 * 创建系统备份记录
	 * @param type
	 * @param auditInfo
	 * @param userId
	 * @return
	 */
	SpDataBackups createSystemData(String type, SpSystemOperateLogInfo auditInfo, String userId);

	/**
	 * 根据ID获取系统备份记录
	 * @param id
	 * @return
	 */
	SpDataBackups getystemAchiveLogById(String id);

	/**
	 * 更新系统备份记录状态
	 * @param dataBackups
	 * @param status
	 */
	void updateDataBackups(SpDataBackups dataBackups, String status);

	/**
	 * 更新系统备份信息
	 * @param dataBackups
	 */
	void updateDataBackups(SpDataBackups dataBackups);

	/**
	 * 进行系统数据备份
	 * @param dataBackups
	 */
	void achiveAllData(SpDataBackups dataBackups);

	/**
	 * 系统备份恢复
	 * @param dataBackups
	 * @param auditInfo
	 */
	void recoverDataById(SpDataBackups dataBackups, SpSystemOperateLogInfo auditInfo);

	/**
	 * 删除系统备份数据压缩包
	 * @param id
	 * @param auditInfo
	 * @return
	 */
	CodeRsp deleteLog(String id, SpSystemOperateLogInfo auditInfo);

	/**
	 * 创建系统备份恢复记录
	 * @param logPath
	 * @param tabType
	 * @param userId
	 * @return
	 * @throws TMCException
	 */
	SpDataBackups createDataBackups(String logPath, String tabType, String userId) throws TMCException;

}
