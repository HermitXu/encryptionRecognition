package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [角色模块关系表]
 * @ClassName: SpRoleModulePermissions
 * @description [角色模块关系表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:12
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpRoleModulePermissions implements Serializable
{

    private static final long serialVersionUID = 1593762088005455006L;
    /**
     * 主键
     */
    private String id;
    /**
     * 模块id
     */
    private String moduleId;
    /**
     * 角色id
     */
    private String roleId;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getModuleId()
    {
        return moduleId;
    }

    public void setModuleId(String moduleId)
    {
        this.moduleId = moduleId;
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
