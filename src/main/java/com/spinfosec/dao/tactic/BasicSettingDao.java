package com.spinfosec.dao.tactic;

import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dao.entity.SpServerStatus;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName BasicSettingDao
 * @Description: 〈基本设置持久层〉
 * @date 2018/10/25
 * All rights Reserved, Designed By SPINFO
 */
public interface BasicSettingDao
{
	/**
	 * 根据分组名获取响应配置
	 * @param groupName
	 * @return
	 */
	List<SpConfigProperties> getConfigByGroupName(String groupName);

	/**
	 * 保存配置信息
	 * @param configProperties
	 */
	void saveConfigProperties(SpConfigProperties configProperties);

	/**
	 * 更新配置信息
	 * @param configProperties
	 */
	void updateConfigProperties(SpConfigProperties configProperties);

	/**
	 * 保存服务器状态信息
	 * @param serverStatus
	 */
	void saveServerStatus(SpServerStatus serverStatus);

	/**
	 * 更新服务器状态信息
	 * @param serverStatus
	 */
	void updateServerStatus(SpServerStatus serverStatus);

	/**
	 * 获取服务器状态信息
	 * @return
	 */
	SpServerStatus getServerStatus();

}
