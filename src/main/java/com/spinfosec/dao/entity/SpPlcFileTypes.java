package com.spinfosec.dao.entity;

import java.io.Serializable;

/**
 * @title [文件类型表]
 * @ClassName: SpPlcFileTypes
 * @description [文件类型表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 18:49
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpPlcFileTypes implements Serializable
{

    private static final long serialVersionUID = 1486494278467496835L;
    /**
     * 主键
     */
	private String id;
    /**
     * 操作标识
     */
    private double optlock;
    /**
     * 备注
     */
    private String description;
    /**
     * 名称
     */
    private String name;
    /**
     * 整型值，同id
     */
    private double intVal;
    /**
     * 后缀
     */
    private String extension;
    /**
     * 格式分组
     */
    private String formatGroup;
    /**
     *
     */
    private double isUnicast;
    /**
     *
     */
    private String policyEntityStatus;
    /**
     * 定义类型
     */
    private String definitionType;

	/**
	 * 是否是常用类型 1常用 0自定义
	 */
	private String commonUse;

	public String getCommonUse()
	{
		return commonUse;
	}

	public void setCommonUse(String commonUse)
	{
		this.commonUse = commonUse;
	}

	public String getId()
    {
        return id;
    }

	public void setId(String id)
    {
        this.id = id;
    }


    public double getOptlock()
    {
        return optlock;
    }

    public void setOptlock(double optlock)
    {
        this.optlock = optlock;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public double getIntVal()
    {
        return intVal;
    }

    public void setIntVal(double intVal)
    {
        this.intVal = intVal;
    }


    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }


    public String getFormatGroup()
    {
        return formatGroup;
    }

    public void setFormatGroup(String formatGroup)
    {
        this.formatGroup = formatGroup;
    }


    public double getIsUnicast()
    {
        return isUnicast;
    }

    public void setIsUnicast(double isUnicast)
    {
        this.isUnicast = isUnicast;
    }


    public String getPolicyEntityStatus()
    {
        return policyEntityStatus;
    }

    public void setPolicyEntityStatus(String policyEntityStatus)
    {
        this.policyEntityStatus = policyEntityStatus;
    }


    public String getDefinitionType()
    {
        return definitionType;
    }

    public void setDefinitionType(String definitionType)
    {
        this.definitionType = definitionType;
    }

}
