package com.spinfosec.dto.pojo.system;

import com.spinfosec.dao.entity.SpAdmins;

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
public class AdminReq
{
	/**
	 * 用户信息表
	 */
	private SpAdmins spAdmins;

	/**
	 * 组织ID
	 */
	private String orgId;

	/**
	 * 角色ID
	 */
	private String roleId;

	public SpAdmins getSpAdmins()
	{
		return spAdmins;
	}

	public void setSpAdmins(SpAdmins spAdmins)
	{
		this.spAdmins = spAdmins;
	}

	/**
	 * 用户信任主机关系
	 */
	private List<String> hostItems;

	public List<String> getHostItems()
	{
		return hostItems;
	}

	public void setHostItems(List<String> hostItems)
	{
		this.hostItems = hostItems;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getRoleId()
	{
		return roleId;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
}
