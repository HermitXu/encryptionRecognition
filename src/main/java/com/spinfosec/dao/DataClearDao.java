package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DataClearDao
 * @Description: 〈数据清理〉
 * @date 2019/1/15
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface DataClearDao
{
	/**
	 * 查找正在运行的任务
	 * @return
	 */
	List<SpTask> getRunningTasks();

	/**
	 * 执行删除SQL语句
	 * @param sql
	 */
	void execNativeSql(@Param("sql") String sql);

}
