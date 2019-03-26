package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [作业调度方案主键与星期之间的对应关系表]
 * @ClassName: SpSchedulingDayTf
 * @description [指出了该调度一周内那几天执行该表只有在按天或者按周调度才会用到]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:27
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpSchedulingDayTf implements Serializable
{

    private static final long serialVersionUID = -4586598848952111905L;
    /**
     * 主键
     */
    private String id;
    /**
     * 外键，参考sp_scheduling_data表
     */
    private String schedulingDataId;
    /**
     * sp_day_timeframe
     */
    private String dayTfId;


    public String getSchedulingDataId()
    {
        return schedulingDataId;
    }

    public void setSchedulingDataId(String schedulingDataId)
    {
        this.schedulingDataId = schedulingDataId;
    }


    public String getDayTfId()
    {
        return dayTfId;
    }

    public void setDayTfId(String dayTfId)
    {
        this.dayTfId = dayTfId;
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

}
