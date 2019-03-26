package com.spinfosec.dto.pojo.system;

import com.spinfosec.dao.entity.SpRoleModulePermissions;
import com.spinfosec.dao.entity.SpRoles;

import java.util.List;

/**
 * @title [标题]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-6-20 下午9:34:06
 */
public class RoleReq
{
	/**
	 * 角色表
	 */
	private SpRoles spRoles;
	/**
	 * 角色模块关系
	 */
	private List<SpRoleModulePermissions> spRoleModulePermissions;

	private List<String> adminIds;

	public SpRoles getSpRoles()
	{
		return spRoles;
	}

	public void setSpRoles(SpRoles spRoles)
	{
		this.spRoles = spRoles;
	}

	public List<SpRoleModulePermissions> getSpRoleModulePermissions()
	{
		return spRoleModulePermissions;
	}

	public void setSpRoleModulePermissions(List<SpRoleModulePermissions> spRoleModulePermissions)
	{
		this.spRoleModulePermissions = spRoleModulePermissions;
	}

	public List<String> getAdminIds()
	{
		return adminIds;
	}

	public void setAdminIds(List<String> adminIds)
	{
		this.adminIds = adminIds;
	}

}
