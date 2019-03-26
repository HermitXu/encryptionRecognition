package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [策略调度]
 * @ClassName: SpSchedulingData
 * @description [策略调度]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:16
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpSchedulingData implements Serializable
{

    private static final long serialVersionUID = 1951788508700491373L;
    /**
     * 主键
     */
    private String id;
    /**
     * 开始时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /**
     * 结束时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    /**
     * 结束时间类型
     */
    private String endDateType;
    /**
     * 是否限制持续时间
     */
    private double isDurationLimit;
    /**
     * 间隔时间
     */
    private double durationValue;
    /**
     * 重复周期类型
     */
    private String frequencyType;
    /**
     * 计划时间
     */
    private String scheduledays;
    /**
     * 重复类型：周、日、月
     */
    private String recurDates;
    /**
     * 停止继续的时间间隔 默认为10，单位分钟，该选项对 “持续”选项有效，其他都为10
     */
    private double continuousStopInterval;
    /**
     * 是否选中“不早于”CHECKBOX 时间为 START_DATE字段
     */
    private double useInitialRecur;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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


    public String getEndDateType()
    {
        return endDateType;
    }

    public void setEndDateType(String endDateType)
    {
        this.endDateType = endDateType;
    }


    public double getIsDurationLimit()
    {
        return isDurationLimit;
    }

    public void setIsDurationLimit(double isDurationLimit)
    {
        this.isDurationLimit = isDurationLimit;
    }


    public double getDurationValue()
    {
        return durationValue;
    }

    public void setDurationValue(double durationValue)
    {
        this.durationValue = durationValue;
    }


    public String getFrequencyType()
    {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType)
    {
        this.frequencyType = frequencyType;
    }


    public String getScheduledays()
    {
        return scheduledays;
    }

    public void setScheduledays(String scheduledays)
    {
        this.scheduledays = scheduledays;
    }


    public String getRecurDates()
    {
        return recurDates;
    }

    public void setRecurDates(String recurDates)
    {
        this.recurDates = recurDates;
    }


    public double getContinuousStopInterval()
    {
        return continuousStopInterval;
    }

    public void setContinuousStopInterval(double continuousStopInterval)
    {
        this.continuousStopInterval = continuousStopInterval;
    }


    public double getUseInitialRecur()
    {
        return useInitialRecur;
    }

    public void setUseInitialRecur(double useInitialRecur)
    {
        this.useInitialRecur = useInitialRecur;
    }

}
