package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [策略表]
 * @ClassName: SpDiscoveryTasks
 * @description [策略表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 17:44
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpDiscoveryTasks implements Serializable
{

    private static final long serialVersionUID = 8060505385531494787L;
    /**
     * 主键
     */
    private String id;
    /**
     * 策略名称
     */
    private String name;
    /**
     * 备注
     */
    private String description;
    /**
     * 策略类型   文件  数据库  exchange 等
     */
    private String discoveryTaskType;
    /**
     * 部署状态  SYNCHRONIZED, UNSYNCHRONIZED_NEW, UNSYNCHRONIZED_EDIT
     */
    private String elementStatus;
    /**
     * 目标id
     */
    private String targetId;
    /**
     * 调度id
     */
    private String schedulingId;
    /**
     * 是否启用文件名称过滤
     */
    private double isFileNameEnabled;

	/**
	 * 常用文件类型 0 或 自定义文件类型 1
	 */
	private double commonOrcustomFileType;
    /**
     * 文件时间过滤是否启用
     */
    private double isFileAgeEnabled;
    /**
     * 对应文件时间过滤选项内的三个radio选项，指定文件修改时间的范围 如： WITHIN，多少个月之内修改的文件 MORE_THAN，多少个月之前修改的文件 BETWEEN ，某个时间区间内修改的文件
     */
    private String scanPeriodType;
    /**
     * 多少个月之内修改过的文件
     */
    private double modifiedWithinMonths;
    /**
     * 多少个月之前修改的文件
     */
    private double modifiedMonthsAgo;
    /**
     * 修改时间开始
     */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date modifiedFromDate;
    /**
     * 修改日期结束
     */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date modifiedToDate;
    /**
	 * 是否启用文件大小的上限
	 */
    private double isLargerThanEnabled;
    /**
     * 上限文件大小
     */
    private double sizeLargerThan;
    /**
	 * 是否启用文件大小的下限
	 */
    private double isSmallerThanEnalbed;
    /**
     * 文件下限大小
     */
    private double sizeSmallerThan;
    /**
     * C_USER_DEFINE C_PRE_DEFINE
     */
    private String definitionType;
    /**
     * 文档内ocr启用
     */
    private double ocrEnabled;
    /**
     * cpu限制启用
     */
    private double isCpuLimitEnabled;
    /**
     * cpu限制
     */
    private double cpuLimitValue;

	/**
	 * 忙时带宽限制启用
	 */
	private double busyBandwidthEnabled;
	/**
	 * 忙时带宽值
	 */
	private String busyBandwidthLimit;
	/**
	 * 忙时开始时间
	 */
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date busyBandwidthStartdate;
	/**
	 * 忙时结束时间
	 */
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date busyBandwidthEnddate;
    /**
     * 扫描百分比
     */
    private double percentEnabled;
    /**
     * 百分比扫描百分数
     */
    private String percentAge;
    /**
     * 计时器扫描（扫描时间限制）是否启用
     */
    private double timerEnabled;
    /**
     * 计时器扫描值
     */
    private String timerAge;
    /**
     * 被检单位
     */
	private String beCheckedOrgId;
    /**
     * 检查单位
     */
    private String checkOrgName;
    /**
     * 检查人
     */
    private String checkUser;
    /**
     * 检查时间
     */
    private String checkDate;
    /**
     * ocr预处理
     */
    private double ocrPreprocess;
    /**
     * 创建者
     */
    private String createdBy;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

	public double getCommonOrcustomFileType()
	{
		return commonOrcustomFileType;
	}

	public void setCommonOrcustomFileType(double commonOrcustomFileType)
	{
		this.commonOrcustomFileType = commonOrcustomFileType;
	}

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDiscoveryTaskType()
    {
        return discoveryTaskType;
    }

    public void setDiscoveryTaskType(String discoveryTaskType)
    {
        this.discoveryTaskType = discoveryTaskType;
    }


    public String getElementStatus()
    {
        return elementStatus;
    }

    public void setElementStatus(String elementStatus)
    {
        this.elementStatus = elementStatus;
    }


    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }


    public String getSchedulingId()
    {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId)
    {
        this.schedulingId = schedulingId;
    }


    public double getIsFileNameEnabled()
    {
        return isFileNameEnabled;
    }

    public void setIsFileNameEnabled(double isFileNameEnabled)
    {
        this.isFileNameEnabled = isFileNameEnabled;
    }


    public double getIsFileAgeEnabled()
    {
        return isFileAgeEnabled;
    }

    public void setIsFileAgeEnabled(double isFileAgeEnabled)
    {
        this.isFileAgeEnabled = isFileAgeEnabled;
    }


    public String getScanPeriodType()
    {
        return scanPeriodType;
    }

    public void setScanPeriodType(String scanPeriodType)
    {
        this.scanPeriodType = scanPeriodType;
    }


    public double getModifiedWithinMonths()
    {
        return modifiedWithinMonths;
    }

    public void setModifiedWithinMonths(double modifiedWithinMonths)
    {
        this.modifiedWithinMonths = modifiedWithinMonths;
    }


    public double getModifiedMonthsAgo()
    {
        return modifiedMonthsAgo;
    }

    public void setModifiedMonthsAgo(double modifiedMonthsAgo)
    {
        this.modifiedMonthsAgo = modifiedMonthsAgo;
    }


    public Date getModifiedFromDate()
    {
        return modifiedFromDate;
    }

    public void setModifiedFromDate(Date modifiedFromDate)
    {
        this.modifiedFromDate = modifiedFromDate;
    }


    public Date getModifiedToDate()
    {
        return modifiedToDate;
    }

    public void setModifiedToDate(Date modifiedToDate)
    {
        this.modifiedToDate = modifiedToDate;
    }


    public double getIsLargerThanEnabled()
    {
        return isLargerThanEnabled;
    }

    public void setIsLargerThanEnabled(double isLargerThanEnabled)
    {
        this.isLargerThanEnabled = isLargerThanEnabled;
    }


    public double getSizeLargerThan()
    {
        return sizeLargerThan;
    }

    public void setSizeLargerThan(double sizeLargerThan)
    {
        this.sizeLargerThan = sizeLargerThan;
    }


    public double getIsSmallerThanEnalbed()
    {
        return isSmallerThanEnalbed;
    }

    public void setIsSmallerThanEnalbed(double isSmallerThanEnalbed)
    {
        this.isSmallerThanEnalbed = isSmallerThanEnalbed;
    }


    public double getSizeSmallerThan()
    {
        return sizeSmallerThan;
    }

    public void setSizeSmallerThan(double sizeSmallerThan)
    {
        this.sizeSmallerThan = sizeSmallerThan;
    }


    public String getDefinitionType()
    {
        return definitionType;
    }

    public void setDefinitionType(String definitionType)
    {
        this.definitionType = definitionType;
    }


    public double getOcrEnabled()
    {
        return ocrEnabled;
    }

    public void setOcrEnabled(double ocrEnabled)
    {
        this.ocrEnabled = ocrEnabled;
    }


    public double getIsCpuLimitEnabled()
    {
        return isCpuLimitEnabled;
    }

    public void setIsCpuLimitEnabled(double isCpuLimitEnabled)
    {
        this.isCpuLimitEnabled = isCpuLimitEnabled;
    }


    public double getCpuLimitValue()
    {
        return cpuLimitValue;
    }

    public void setCpuLimitValue(double cpuLimitValue)
    {
        this.cpuLimitValue = cpuLimitValue;
    }


    public Date getBusyBandwidthStartdate()
    {
        return busyBandwidthStartdate;
    }

    public void setBusyBandwidthStartdate(Date busyBandwidthStartdate)
    {
        this.busyBandwidthStartdate = busyBandwidthStartdate;
    }


    public Date getBusyBandwidthEnddate()
    {
        return busyBandwidthEnddate;
    }

    public void setBusyBandwidthEnddate(Date busyBandwidthEnddate)
    {
        this.busyBandwidthEnddate = busyBandwidthEnddate;
    }


    public double getBusyBandwidthEnabled()
    {
        return busyBandwidthEnabled;
    }

    public void setBusyBandwidthEnabled(double busyBandwidthEnabled)
    {
        this.busyBandwidthEnabled = busyBandwidthEnabled;
    }


    public String getBusyBandwidthLimit()
    {
        return busyBandwidthLimit;
    }

    public void setBusyBandwidthLimit(String busyBandwidthLimit)
    {
        this.busyBandwidthLimit = busyBandwidthLimit;
    }


    public double getPercentEnabled()
    {
        return percentEnabled;
    }

    public void setPercentEnabled(double percentEnabled)
    {
        this.percentEnabled = percentEnabled;
    }


    public String getPercentAge()
    {
        return percentAge;
    }

    public void setPercentAge(String percentAge)
    {
        this.percentAge = percentAge;
    }


    public double getTimerEnabled()
    {
        return timerEnabled;
    }

    public void setTimerEnabled(double timerEnabled)
    {
        this.timerEnabled = timerEnabled;
    }


    public String getTimerAge()
    {
        return timerAge;
    }

    public void setTimerAge(String timerAge)
    {
        this.timerAge = timerAge;
    }

	public String getBeCheckedOrgId()
    {
		return beCheckedOrgId;
    }

	public void setBeCheckedOrgId(String beCheckedOrgId)
    {
		this.beCheckedOrgId = beCheckedOrgId;
    }

    public String getCheckOrgName()
    {
        return checkOrgName;
    }

    public void setCheckOrgName(String checkOrgName)
    {
        this.checkOrgName = checkOrgName;
    }


    public String getCheckUser()
    {
        return checkUser;
    }

    public void setCheckUser(String checkUser)
    {
        this.checkUser = checkUser;
    }


    public String getCheckDate()
    {
        return checkDate;
    }

    public void setCheckDate(String checkDate)
    {
        this.checkDate = checkDate;
    }


    public double getOcrPreprocess()
    {
        return ocrPreprocess;
    }

    public void setOcrPreprocess(double ocrPreprocess)
    {
        this.ocrPreprocess = ocrPreprocess;
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

}
