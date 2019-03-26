package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [角色表]
 * @ClassName: SpRoles
 * @description [角色表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:13
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpRoles implements Serializable
{

    private static final long serialVersionUID = 7314107815352197413L;
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 角色分类   系统管理员 SYSTEM_MANAGER     业务管理员 BUSSINESS_MANAGER    审核管理员 AUDIT_MANAGER
     */
    private String roleType;
    /**
     * 备注
     */
    private String description;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 创建者
     */
    private String createdBy;
    /**
     * 预定义 C_PRE_DEFINE     自定义  C_USER_DEFINE
     */
    private String definitionType;


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


    public String getRoleType()
    {
        return roleType;
    }

    public void setRoleType(String roleType)
    {
        this.roleType = roleType;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
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
