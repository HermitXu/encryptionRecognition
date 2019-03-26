package com.spinfosec.service.srv;

import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dao.entity.SpServerStatus;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.system.RspCode;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IBasicSettingSrv
 * @Description: 〈基本设置业务接口〉
 * @date 2018/10/25
 * All rights Reserved, Designed By SPINFO
 */
public interface IConfigPropertiesSrv
{
	/**
	 * 根据基本设置类型名获取设置参数
	 * @param groupName
	 * @return
	 */
	List<SpConfigProperties> getSettingByGroupName(String groupName);

	/**
	 * 保存配置参数
	 * @param configPropertiesList
	 */
	void saveConfigProperties(List<SpConfigProperties> configPropertiesList) throws Exception;

	/**
	 * 修改系统时间
	 * @param data
	 * @return
	 */
	RspCode saveBaseTimeSetting(String data, SpSystemOperateLogInfo operateLogInfo);

	/**
	 * 获取服务器所在系统的CPU、内存、磁盘空间等信息
	 * @return
	 */
	SpServerStatus getServerStatus();
}
