package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ank
 * @version v 1.0
 * @title [信任主机]
 * @ClassName: SpAdminHostSetting
 * @description [信任主机]
 * @create 2018/9/6 15:47
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpAdminHostSetting implements Serializable
{
    private static final long serialVersionUID = -1476593828466937521L;
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * ip
     */
    private String hostIp;
    /**
     * mac
     */
    private String hostMac;
    /**
     * 创建者
     */
    private String createdBy;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
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


    public String getHostIp()
    {
        return hostIp;
    }

    public void setHostIp(String hostIp)
    {
        this.hostIp = hostIp;
    }


    public String getHostMac()
    {
        return hostMac;
    }

    public void setHostMac(String hostMac)
    {
        this.hostMac = hostMac;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
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
