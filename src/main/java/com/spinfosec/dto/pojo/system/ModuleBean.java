package com.spinfosec.dto.pojo.system;

import com.spinfosec.dao.entity.SpCodeDecodes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ModuleBean
 * @Description: 〈模块对象〉
 * @date 2019/3/21
 * @copyright All rights Reserved, Designed By SPINFO
 */
@ApiModel(value = "模块和角色")
public class ModuleBean
{

	/**
	 * 模块信息
	 */
	@ApiModelProperty(name = "模块信息", required = true)
	private SpCodeDecodes codeDecodes;

	/**
	 * 角色ID集合
	 */
	@ApiModelProperty(name = "角色ID集合", required = true)
	private List<String> roleIds;

	public SpCodeDecodes getCodeDecodes()
	{
		return codeDecodes;
	}

	public void setCodeDecodes(SpCodeDecodes codeDecodes)
	{
		this.codeDecodes = codeDecodes;
	}

	public List<String> getRoleIds()
	{
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds)
	{
		this.roleIds = roleIds;
	}
}
