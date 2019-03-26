package com.spinfosec.dao.common;

import com.spinfosec.dto.pojo.common.ValidationBean;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ValidationDao
 * @Description: 〈后台校验持久层〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
public interface ValidationDao
{
	/**
	 * 重名校验
	 * @param checkData
	 * @return
	 */
	List<Object> duplicate(ValidationBean checkData);

	/**
	 * 查询角色是否被引用
	 * @param roleId 角色ID
	 * @return
	 */
	List<String> findRolesIsUsed(String roleId);

	/**
	 * 查询信任主机是否被引用
	 * @param hostId 主机ID
	 * @return
	 */
	List<String> findHostIsUsed(String hostId);

	/**
	 * 查询组织资源是否被引用
	 * @param orgId 组织ID
	 * @return
	 */
	List<String> findOrgIsUsed(String orgId);
}
