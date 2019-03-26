package com.spinfosec.service.srv;

import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import org.springframework.stereotype.Service;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IDataClearSrv
 * @Description: 〈数据清理〉
 * @date 2019/1/15
 * @copyright All rights Reserved, Designed By SPINFO
 */

public interface IDataClearSrv
{
	/**
	 * 判断是否存在运行中的任务
	 * @return
	 */
	boolean isExistRunningTask();

	/**
	 * 启动守护线程
	 */
	void startCrond();

	/**
	 * 停止守护线程
	 */
	void stopCrond();

	/**
	 * 启动SE服务
	 * @return
	 */
	boolean startScanEngine();

	/**
	 * 停止SE服务
	 * @return
	 */
	boolean stopScanEngine();

	/**
	 * 清理数据
	 * @param dataArr 需要清理的数据类型
	 */
	void clearData(String[] dataArr, SpSystemOperateLogInfo systemOperateLogInfo);
}
