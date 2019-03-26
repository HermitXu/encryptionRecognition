package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpAdminHostSetting;
import com.spinfosec.dto.pojo.system.TrustHostBean;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AdminHostSettingDao
 * @Description: 〈信任主机管理持久层〉
 * @date 2018/10/15
 * All rights Reserved, Designed By SPINFO
 */
public interface TrustHostDao
{

	/**
	 * 分页查询信任主机信息
	 * @param queryMap
	 * @return
	 */
	List<TrustHostBean> querByPage(Map<String, Object> queryMap);

	/**
	 * 获取所有信任主机信息
	 * @return
	 */
	List<SpAdminHostSetting> getAllSecHost(String userId);

	/**
	 * 根据id删除对应主机
	 * @param id
	 */
	void deleteHostSecById(String id);

	/**
	 * 保存主机管理
	 * @param spAdminHostSetting
	 */
	void saveHostSec(SpAdminHostSetting spAdminHostSetting);

	/**
	 * 更新主机管理
	 * @param spAdminHostSetting
	 */
	void updateHostSec(SpAdminHostSetting spAdminHostSetting);

	/**
	 * 通过信息主机ID删除关系
	 * @param hostId
	 */
	void deldeteAdminTrustHostByHostId(String hostId);

	/**
	 * 根据ID获取对应信任主机信息
	 * @param id
	 * @return
	 */
	SpAdminHostSetting getHostSettingById(String id);
}
