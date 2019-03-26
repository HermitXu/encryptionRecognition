package com.spinfosec.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ank
 * @version v 1.0
 * @title [安全日志归档]
 * @ClassName: SpEventArchiveLog
 * @description [安全日志归档]
 * @create 2018/9/13 11:05
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpEventArchiveLog implements Serializable
{

    private static final long serialVersionUID = 1543069034213289662L;

    private String id;
    private String status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    private String incidentNum;
    private String path;
    private String description;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    private String isdel;
    private String createdBy;
    private long downOptLock;
    private long recoverOptLock;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    public String getIncidentNum()
    {
        return incidentNum;
    }

    public void setIncidentNum(String incidentNum)
    {
        this.incidentNum = incidentNum;
    }


    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
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


    public String getIsdel()
    {
        return isdel;
    }

    public void setIsdel(String isdel)
    {
        this.isdel = isdel;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public long getDownOptLock()
    {
        return downOptLock;
    }

    public void setDownOptLock(long downOptLock)
    {
        this.downOptLock = downOptLock;
    }


    public long getRecoverOptLock()
    {
        return recoverOptLock;
    }

    public void setRecoverOptLock(long recoverOptLock)
    {
        this.recoverOptLock = recoverOptLock;
    }

}
