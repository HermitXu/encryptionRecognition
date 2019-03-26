package com.spinfosec.dao.entity;

import java.io.Serializable;

/**
 * @title [部门用户关系表]
 * @ClassName: SpOrgAdminRelation
 * @description [部门用户关系表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 18:44
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpOrgAdminRelation implements Serializable
{

    private static final long serialVersionUID = 1812833666327309613L;
    /**
     * 主键
     */
    private String id;
    /**
     * 部门id
     */
    private String orgId;
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


    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
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
