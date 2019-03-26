package com.spinfosec.dao.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * @title [标题]
 * @ClassName: SpServerStatus
 * @description [一句话描述]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 19:39
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpServerStatus implements Serializable
{

    private static final long serialVersionUID = 8603936236463447119L;
    /**
     * 主键
     */
    private long id;
	/**
	 * cpu使用率
	 */
    private double cpuUsage;
	/**
	 * 内存使用率
	 */
    private double memUsage;
	/**
	 * 磁盘剩余
	 */
    private double freeDisk;
	/**
	 * 获取时间
	 */
    private Date getTime;
	/**
	 * 主机名
	 */
    private String hostname;
	/**
	 * 网卡名称
	 */
    private String netName;
    /**
     * 网卡是否可用
     */
    private String isEnable;
	/**
	 * 主机IP
	 */
    private String hostIp;
    /**
     * 磁盘总大小
     */
    private String totalDisk;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }


    public double getCpuUsage()
    {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage)
    {
        this.cpuUsage = cpuUsage;
    }


    public double getMemUsage()
    {
        return memUsage;
    }

    public void setMemUsage(double memUsage)
    {
        this.memUsage = memUsage;
    }


    public double getFreeDisk()
    {
        return freeDisk;
    }

    public void setFreeDisk(double freeDisk)
    {
        this.freeDisk = freeDisk;
    }


    public Date getGetTime()
    {
        return getTime;
    }

    public void setGetTime(Date getTime)
    {
        this.getTime = getTime;
    }


    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }


    public String getNetName()
    {
        return netName;
    }

    public void setNetName(String netName)
    {
        this.netName = netName;
    }


    public String getIsEnable()
    {
        return isEnable;
    }

    public void setIsEnable(String isEnable)
    {
        this.isEnable = isEnable;
    }


    public String getHostIp()
    {
        return hostIp;
    }

    public void setHostIp(String hostIp)
    {
        this.hostIp = hostIp;
    }


    public String getTotalDisk()
    {
        return totalDisk;
    }

    public void setTotalDisk(String totalDisk)
    {
        this.totalDisk = totalDisk;
    }

}
