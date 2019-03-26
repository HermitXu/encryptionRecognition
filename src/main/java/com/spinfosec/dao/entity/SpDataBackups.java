package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [系统备份表]
 * @ClassName: SpDataBackups
 * @description [系统备份表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 16:08
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpDataBackups implements Serializable
{

    private static final long serialVersionUID = 8075148011116056216L;
    /**
     * 主键
     */
    private String id;
    /**
     * 备份文件路径
     */
    private String localpath;
    /**
     * 下载标识
     */
    private long downloadOptlock;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     *
     */
    private String description;
    /**
     * 文件大小
     */
    private double filesize;
    /**
     * 类型  业务数据    系统数据    安全数据
     */
    private String type;
    /**
     * 恢复标识
     */
    private long recoverOptlock;
    /**
     * 状态：0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败
     */
    private String status;
    /**
     * 创建者
     */
    private String createdBy;
    /**
     * 安全数据按策略备份用
     */
    private String policyId;
    /**
     * 安全数据 备份开始时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 安全数据 备份结束时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 是否删除
     */
    private double isDel;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getLocalpath()
    {
        return localpath;
    }

    public void setLocalpath(String localpath)
    {
        this.localpath = localpath;
    }


    public long getDownloadOptlock()
    {
        return downloadOptlock;
    }

    public void setDownloadOptlock(long downloadOptlock)
    {
        this.downloadOptlock = downloadOptlock;
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

    public double getFilesize()
    {
        return filesize;
    }

    public void setFilesize(double filesize)
    {
        this.filesize = filesize;
    }


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }


    public long getRecoverOptlock()
    {
        return recoverOptlock;
    }

    public void setRecoverOptlock(long recoverOptlock)
    {
        this.recoverOptlock = recoverOptlock;
    }


    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public String getPolicyId()
    {
        return policyId;
    }

    public void setPolicyId(String policyId)
    {
        this.policyId = policyId;
    }


    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }


    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }


    public double getIsDel()
    {
        return isDel;
    }

    public void setIsDel(double isDel)
    {
        this.isDel = isDel;
    }

}
