package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [任务失败的文件信息表]
 * @ClassName: SpTaskSipped
 * @description [任务失败的文件信息表]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 20:25
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpTaskSipped implements Serializable
{

    private static final long serialVersionUID = -2938878852191612223L;
    /**
     * 主键
     */
    private String id;
    /**
     * 文件网络中全路径名称
     */
    private String name;
    /**
     * 状态
     */
    private String status;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 扫描时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date scanTime;


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


    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }


    public Date getScanTime()
    {
        return scanTime;
    }

    public void setScanTime(Date scanTime)
    {
        this.scanTime = scanTime;
    }

}
