package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [系统操作日志归档]
 * @ClassName: SpSystemOperateArchiveLog
 * @description [系统操作日志归档]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:41
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpSystemOperateArchiveLog implements Serializable
{

    private static final long serialVersionUID = -8965244962965619457L;
    /**
     * 主键
     */
    private String id;
    /**
     * 状态：0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败
     */
    private String status;
    /**
     * 开始时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /**
     * 截止时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    /**
     * 归档个数
     */
    private String incidentNum;
    /**
     * 路径
     */
    private String path;
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
     * 是否删除原记录
     */
    private String isdel;
    /**
     * 创建者
     */
    private String createdBy;
    /**
     *
     */
    private long downOptLock;
    /**
     *
     */
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
