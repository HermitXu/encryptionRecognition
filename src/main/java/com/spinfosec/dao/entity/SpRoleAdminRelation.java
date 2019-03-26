package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [角色用户关系表]
 * @ClassName: SpRoleAdminRelation
 * @description [角色用户关系表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:09
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpRoleAdminRelation implements Serializable
{

    private static final long serialVersionUID = -55882466422379699L;
    /**
     * 主键
     */
    private String id;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 用户id
     */
    private String adminId;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }


    public String getAdminId()
    {
        return adminId;
    }

    public void setAdminId(String adminId)
    {
        this.adminId = adminId;
    }

}
