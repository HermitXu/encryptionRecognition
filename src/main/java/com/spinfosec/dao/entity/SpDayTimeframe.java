package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [任务调度中天的信息]
 * @ClassName: SpDayTimeframe
 * @description [对于以“周”为周期的调度，该表存储了此调度需要在一周中的那几天执行。 该表存储了周一到周日的大写字符串 对于以天为周期的调度，该表默认存储“SUNDAY” 该表只有在按天或者按周调度才会用到]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 17:34
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpDayTimeframe implements Serializable
{

    private static final long serialVersionUID = -5042661651700898915L;

    /**
     * 主键id
     */
    private String id;
    /**
     * 周几
     */
    private String dayOfWeek;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDayOfWeek()
    {
        return this.dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

}
