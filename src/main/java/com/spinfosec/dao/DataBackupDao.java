package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpDataBackups;
import com.spinfosec.dto.pojo.system.SystemArchiveLogRsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SystemAchiveLogDao
 * @Description: 〈系统备份〉
 * @date 2019/1/16
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface DataBackupDao
{
	/**
	 * 查询系统备份信息
	 * @param queryMap
	 * @return
	 */
	List<SystemArchiveLogRsp> queryData(Map<String, Object> queryMap);

	/**
	 * 保存系统备份记录
	 * @param dataBackups
	 */
	void saveDataBackups(SpDataBackups dataBackups);

	/**
	 * 更新系统备份记录
	 * @param dataBackups
	 */
	void updateDataBackups(SpDataBackups dataBackups);

	/**
	 * 根据ID删除系统备份记录
	 * @param id
	 */
	void deleteDataBackupsById(@Param("id") String id);

	/**
	 * 根据ID获取系统备份记录
	 * @param id
	 * @return
	 */
	SpDataBackups getystemAchiveLogById(@Param("id") String id);

	/**
	 * 通过路径查找系统备份记录
	 * @param path
	 * @return
	 */
	SpDataBackups getDataBackupsByPath(@Param("path") String path);
}
