package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpSystemOperateLogInfo;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SysOperateLogDao
 * @Description: 〈系统操作日志持久层〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
public interface SysOperateLogDao
{

	/**
	 * 分页查询系统操作日志
	 * @param queryMap
	 * @return
	 */
	List<SpSystemOperateLogInfo> queryOperateLog(Map<String, Object> queryMap);

	/**
	 * 保存系统操作日志
	 * @param systemOperateLogInfo
	 */
	void saveOperateLog(SpSystemOperateLogInfo systemOperateLogInfo);

	/**
	 * 更新系统操作日志
	 * @param systemOperateLogInfo
	 */
	void updateOperateLog(SpSystemOperateLogInfo systemOperateLogInfo);

	/**
	 * 根据ID删除系统操作日志
	 * @param id
	 */
	void deleteOperateLogById(String id);

	/**
	 * 根据条件删除系统操作日志
	 * @param queryMap
	 */
	void deleteOperateLogByQueryMap(Map<String, Object> queryMap);

	/**
	 * 根据条件统计符合条件的系统操作日志记录
	 * @param conditionMap
	 * @return
	 */
	Long countSystemOperateLogByCondition(Map<String, Object> conditionMap);

	/**
	 * 统计系统操作日志记录
	 * @return
	 */
	Long countSystemOperateLog();

}
