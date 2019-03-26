package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [任务成功文件信息表]
 * @ClassName: SpTaskFiles
 * @description [任务成功文件信息表]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 20:20
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpTaskFiles implements Serializable
{

    private static final long serialVersionUID = 1665634112739435829L;
    /**
     * 主键
     */
    private String id;
    /**
     * 最后更新时间
     */
    private Date lastModified;
    /**
     * 文件网络中全路径名称
     */
    private String name;
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


    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
