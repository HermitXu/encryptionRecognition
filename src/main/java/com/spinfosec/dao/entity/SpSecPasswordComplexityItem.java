package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [密码安全策略复杂度实体类]
 * @ClassName: SpSecPasswordComplexityItem
 * @description [密码安全策略复杂度实体类]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:29
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpSecPasswordComplexityItem implements Serializable
{

    private static final long serialVersionUID = -8330500751666349395L;
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 显示名称
     */
    private String displayName;
    /**
     * value 正则
     */
    private String value;
    /**
     * 是否启用
     */
    private String isEnable;
    /**
     * 备注
     */
    private String description;


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


    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }


    public String getIsEnable()
    {
        return isEnable;
    }

    public void setIsEnable(String isEnable)
    {
        this.isEnable = isEnable;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
