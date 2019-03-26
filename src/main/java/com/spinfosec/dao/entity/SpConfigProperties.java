package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @author ank
 * @version v 1.0
 * @title [信息配置表]
 * @ClassName: SpConfigProperties
 * @description [信息配置表]
 * @create 2018/9/6 16:05
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpConfigProperties implements Serializable
{

    private static final long serialVersionUID = 2651739009358142402L;
    private String id;
    private String name;
    private String groupName;
    private String value;
    private String extraData;
    private double groupOrder;
    private String bundleKey;
    private String defaultValue;
    private double optlock;


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


    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }


    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }


    public String getExtraData()
    {
        return extraData;
    }

    public void setExtraData(String extraData)
    {
        this.extraData = extraData;
    }


    public double getGroupOrder()
    {
        return groupOrder;
    }

    public void setGroupOrder(double groupOrder)
    {
        this.groupOrder = groupOrder;
    }


    public String getBundleKey()
    {
        return bundleKey;
    }

    public void setBundleKey(String bundleKey)
    {
        this.bundleKey = bundleKey;
    }


    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public double getOptlock()
    {
        return optlock;
    }

    public void setOptlock(double optlock)
    {
        this.optlock = optlock;
    }

}
