package com.spinfosec.dao.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @title [权限模块表]
 * @ClassName: SpCodeDecodes
 * @description [权限模块表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 16:03
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@ApiModel(value = "权限模块表")
public class SpCodeDecodes implements Serializable
{

    private static final long serialVersionUID = 4581261519948434092L;
    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
    private String id;
    /**
     * 模块名
     */
	@ApiModelProperty(value = "模块名", required = true)
    private String name;
    /**
     * 父节点
     */
	@ApiModelProperty(value = "父节点", required = false)
    private String parentId;
    /**
     * 路径
     */
	@ApiModelProperty(value = "路径", required = true)
    private String url;
    /**
     * 排序
     */
	@ApiModelProperty(value = "排序", required = false)
	private int orders;

	/**
	 * 图标
	 */
	@ApiModelProperty(value = "图标", required = false)
	private String icon;

	/**
	 * 是否在菜单中显示
	 */
	@ApiModelProperty(value = "是否在菜单中显示 0 显示 1 不显示", required = true)
	private BigDecimal isShow;

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


    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }


    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

	public int getOrders()
	{
		return orders;
	}

	public void setOrders(int orders)
	{
		this.orders = orders;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public BigDecimal getIsShow()
	{
		return isShow;
	}

	public void setIsShow(BigDecimal isShow)
	{
		this.isShow = isShow;
	}
}
