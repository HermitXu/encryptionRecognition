package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [任务中心]
 * @ClassName: SpTask
 * @description [任务中心]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 20:13
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpTask implements Serializable
{

    private static final long serialVersionUID = 7235321286984223212L;
    private String id;
    private String jobId;
    private String name;
    /**
     * 和策略类型一致
     */
    private String type;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private String failReason;
    private String result;
    private String status;
    private String isLast;
    private String size;
    private String orgId;
    private String orgName;
    /**
     * 数据库时，为总表数，非数据库时为总记录数
     */
    private String totalCount;
    /**
     * 数据库时为总记录数
     */
    private String totalRecordCount;
    /**
     * 数据库时，为成功表数，非数据库时为成功记录数
     */
    private String successCount;
    /**
     * 数据库时为成功记录数
     */
    private String successRecordCount;
    /**
     * 数据库时，为失败表数，非数据库时为失败记录数
     */
    private String failCount;
    /**
     * 数据库时为失败记录数
     */
    private String failRecordCount;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }


    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }


    public String getFailReason()
    {
        return failReason;
    }

    public void setFailReason(String failReason)
    {
        this.failReason = failReason;
    }


    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }


    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getIsLast()
    {
        return isLast;
    }

    public void setIsLast(String isLast)
    {
        this.isLast = isLast;
    }


    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }


    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }


    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }


    public String getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(String totalCount)
    {
        this.totalCount = totalCount;
    }


    public String getTotalRecordCount()
    {
        return totalRecordCount;
    }

    public void setTotalRecordCount(String totalRecordCount)
    {
        this.totalRecordCount = totalRecordCount;
    }


    public String getSuccessCount()
    {
        return successCount;
    }

    public void setSuccessCount(String successCount)
    {
        this.successCount = successCount;
    }


    public String getSuccessRecordCount()
    {
        return successRecordCount;
    }

    public void setSuccessRecordCount(String successRecordCount)
    {
        this.successRecordCount = successRecordCount;
    }


    public String getFailCount()
    {
        return failCount;
    }

    public void setFailCount(String failCount)
    {
        this.failCount = failCount;
    }


    public String getFailRecordCount()
    {
        return failRecordCount;
    }

    public void setFailRecordCount(String failRecordCount)
    {
        this.failRecordCount = failRecordCount;
    }

}
