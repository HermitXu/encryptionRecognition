package com.spinfosec.dto.pojo.deploy;

import java.util.List;

/**
 * @ClassName:     JobDeployObj
 * @Description:(这里用一句话描述这个类的作用)
 * @author:    zhangpf
 * @date:        2014-8-29 下午12:35:41
 * All rights Reserved, Designed By SPINFO
 * Copyright:    Copyright(C) 2010-2011
 */
public class JobDeployObj
{
	/**
	 * definition.xml字符串,此处不可以使用Document对象,因为JSON函数不能处理Document对象的属性
	 */
	private String jobDocStr;

	/**
	 * 策略id
	 */
	private String jobId;

	private List<String> taskIds;

	public String getJobDocStr()
	{
		return jobDocStr;
	}

	public void setJobDocStr(String jobDocStr)
	{
		this.jobDocStr = jobDocStr;
	}

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public List<String> getTaskIds()
	{
		return taskIds;
	}

	public void setTaskIds(List<String> taskIds)
	{
		this.taskIds = taskIds;
	}

	public String toString()
	{
		return "{jobId : " + jobId + ",jobDocStr : " + jobDocStr + "}";
	}

}
