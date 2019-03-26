package com.spinfosec.dto.pojo.system.tatic;

import com.spinfosec.dao.entity.SpDayTimeframeHours;
import com.spinfosec.dao.entity.SpSchedulingData;

import java.util.List;
import java.util.Map;

/**
 * @title [扫描任务时间调度信息结构体]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-7-6 下午9:33:19
 */
public class SchedulingData
{

	/**
	 * 调度信息主表
	 */
	private SpSchedulingData spSchedulingData;

	/**
	 * 调度天和时间点的 对应关系MAP key为周一到周日的大写英文 时间点为调度整点对象的链表 如果有天和星期 需要存SpSchedulingDayTf中的关联关系
	 */
	private Map<String, List<SpDayTimeframeHours>> dayTimeMap;

	public SpSchedulingData getSpSchedulingData()
	{
		return spSchedulingData;
	}

	public void setSpSchedulingData(SpSchedulingData spSchedulingData)
	{
		this.spSchedulingData = spSchedulingData;
	}

	public Map<String, List<SpDayTimeframeHours>> getDayTimeMap()
	{
		return dayTimeMap;
	}

	public void setDayTimeMap(Map<String, List<SpDayTimeframeHours>> dayTimeMap)
	{
		this.dayTimeMap = dayTimeMap;
	}
}
