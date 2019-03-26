package com.spinfosec.dao.entity;

import java.io.Serializable;

/**
 * @title [部门表]
 * @ClassName: SpOrgUnitDict
 * @description [部门表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 18:46
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpOrgUnitDict implements Serializable
{

    private static final long serialVersionUID = 9202176976788775337L;
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * C_USER_DEFINE     C_PRE_DEFINE
     */
    private String definitionType;
    /**
     * 备注
     */
    private String description;
    /**
     * 父部门id
     */
    private String parentId;


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


    public String getDefinitionType()
    {
        return definitionType;
    }

    public void setDefinitionType(String definitionType)
    {
        this.definitionType = definitionType;
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

}
