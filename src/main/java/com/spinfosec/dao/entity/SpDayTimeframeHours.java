package com.spinfosec.dao.entity;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ank
 * @version v 1.0
 * @title [调度任务中整点时间信息]
 * @ClassName: SpDayTimeframeHours
 * @description [每周的某一天的整点时间。该表只有在按天或者按周调度才会用到]
 * @create 2018/9/6 17:39
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpDayTimeframeHours implements Serializable
{

    private static final long serialVersionUID = 4525055733170914854L;
    /**
     * 主键id
     */
    private String id;
    /**
     * sp_day_timeframe表id
     */
    private String dayTimeframeId;
    /**
     * 小时
     */
    private BigDecimal hour;


    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDayTimeframeId()
    {
        return this.dayTimeframeId;
    }

    public void setDayTimeframeId(String dayTimeframeId)
    {
        this.dayTimeframeId = dayTimeframeId;
    }

    public BigDecimal getHour()
    {
        return this.hour;
    }

    public void setHour(BigDecimal hour)
    {
        this.hour = hour;
    }
}
