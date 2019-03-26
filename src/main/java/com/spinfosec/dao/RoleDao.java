package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dao.entity.SpRoleModulePermissions;
import com.spinfosec.dao.entity.SpRoles;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName RoleDao
 * @Description: 〈角色管理持久层〉
 * @date 2018/10/15
 * All rights Reserved, Designed By SPINFO
 */
public interface RoleDao
{
	/**
	 * 根据ID获取角色信息
	 * @param id
	 * @return
	 */
	SpRoles getRoleById(String id);

	/**
	 * 分页条件查询角色
	 * @param queryMap
	 * @return
	 */
	List<SpRoles> querySpRolesByPage(Map<String, Object> queryMap);

	/**
	 * 根据角色ID获取对应权限
	 * @return
	 */
	List<SpCodeDecodes> getPermissionByRole(String roleId);

	/**
	 * 根据角色ID获取对应模块
	 * @param roleId
	 * @return
	 */
	List<SpRoleModulePermissions> getModulePermissions(String roleId);
	/**
	 * 获取所有角色信息
	 * @return
	 */
	List<SpRoles> getAllSpRole(String roleId);

	/**
	 * 保存角色信息
	 * @param spRoles
	 */
	void savaSpRole(SpRoles spRoles);

	/**
	 * 更新角色信息
	 * @param spRoles
	 */
	void updateSpRole(SpRoles spRoles);

	/**
	 * 保存权限
	 * @param spRoleModulePermissions
	 */
	void saveModulePermissions(SpRoleModulePermissions spRoleModulePermissions);

	/**
	 * 根据ID删除角色信息
	 * @param id
	 */
	void deleteRoleById(String id);

	/**
	 * 根据角色信息删除对应的权限
	 * @param roleId
	 */
	void deleteModulePermissionsByRoleId(String roleId);

	/**
	 * 根据角色信息删除用户和角色关系表
	 * @param roleId
	 */
	void deleteUserAndRole(String roleId);
}
