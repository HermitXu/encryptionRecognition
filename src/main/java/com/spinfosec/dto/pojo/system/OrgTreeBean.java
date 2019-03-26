package com.spinfosec.dto.pojo.system;

import java.util.List;

/**
 * 
 * @title [标题]
 * @description [组织资源dto]
 * @copyright Copyright 2013 SHIPING INFO Corporation
 * @company SHIPING INFO.
 * @author ZHANGPF
 * @version v1.0
 * @create 2013-8-7 下午3:38:19
 */
public class OrgTreeBean
{
	/**
	 * 组织单位ID
	 */
	private String id;

	/**
	 * 组织名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 父组织
	 */
	private String parentId;

	/**
	 * 父组织名称
	 */
	private String parentName;

	/**
	 * 子组织
	 */
	private List<OrgTreeBean> children;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public List<OrgTreeBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<OrgTreeBean> children)
	{
		this.children = children;
	}

	public String getParentName()
	{
		return parentName;
	}

	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}
}
