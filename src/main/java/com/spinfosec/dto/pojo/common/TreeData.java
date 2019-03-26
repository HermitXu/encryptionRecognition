package com.spinfosec.dto.pojo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TreeData
 * @Description: 〈树结构体〉
 * @date 2018/10/10
 * All rights Reserved, Designed By SPINFO
 */
@ApiModel(value = "菜单树")
public class TreeData
{

	/**
	 * 树节点ID
	 */
	@ApiModelProperty(value = "主键", required = true)
	private String id;

	/**
	 * 树节点名称
	 */
	@ApiModelProperty(value = "菜单名称", required = true)
	private String name;

	/**
	 * url
	 */
	@ApiModelProperty(value = "url", required = false)
	private String url;

	/**
	 * 树节点图标
	 */
	@ApiModelProperty(value = "树节点图标", required = false)
	private String icon;

	/**
	 * 父节点ID
	 */
	@ApiModelProperty(value = "父节点ID", required = false)
	private String parentId;

	/**
	 * 是否展现子节点
	 */
	@ApiModelProperty(value = "是否展现子节点", required = false)
	private Boolean showChild;

	/**
	 * 是否在菜单中显示
	 */
	@ApiModelProperty(value = "是否在菜单中显示 0 显示 1 不显示", required = true)
	private Boolean isShow;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序", required = false)
	private int order;

	/**
	 * 是否为预置菜单
	 */
	@ApiModelProperty(value = "是否预置菜单 0 预置 1 自定义", required = true)
	private int isPreset;

	public int getIsPreset()
	{
		return isPreset;
	}

	public void setIsPreset(int isPreset)
	{
		this.isPreset = isPreset;
	}

	/**
	 * 子节点
	 */
	private List<TreeData> children;

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

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

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public List<TreeData> getChildren()
	{
		return children;
	}

	public void setChildren(List<TreeData> children)
	{
		this.children = children;
	}

	public Boolean getShowChild()
	{
		return showChild;
	}

	public void setShowChild(Boolean showChild)
	{
		this.showChild = showChild;
	}

	public Boolean getIsShow()
	{
		return isShow;
	}

	public void setIsShow(Boolean isShow)
	{
		this.isShow = isShow;
	}
}
