package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [用户和信任主机的关系表]
 * @ClassName: SpAdminTrustHost
 * @description [用户和信任主机的关系表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 15:58
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpAdminTrustHost implements Serializable
{

    private static final long serialVersionUID = -885755597391435296L;
    /**
     * 主键
     */
    private String id;
    /**
     * 用户id
     */
    private String adminId;
    /**
     * 信任主机id
     */
    private String hostId;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getAdminId()
    {
        return adminId;
    }

    public void setAdminId(String adminId)
    {
        this.adminId = adminId;
    }


    public String getHostId()
    {
        return hostId;
    }

    public void setHostId(String hostId)
    {
        this.hostId = hostId;
    }

}
