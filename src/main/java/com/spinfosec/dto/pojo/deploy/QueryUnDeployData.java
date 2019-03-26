package com.spinfosec.dto.pojo.deploy;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName QueryUnDeployData
 * @Description: 〈策略部署信息模型〉
 * @date 2018/11/12
 * All rights Reserved, Designed By SPINFO
 */
public class QueryUnDeployData
{
	/**
	 * 策略ID
	 */
	private String id;
	/**
	 * 策略名称
	 */
	private String name;
	/**
	 * 任务类型
	 */
	private String taskType;
	/**
	 * 部署状态
	 */
	private String elementStatus;

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

	public String getTaskType()
	{
		return taskType;
	}

	public void setTaskType(String taskType)
	{
		this.taskType = taskType;
	}

	public String getElementStatus()
	{
		return elementStatus;
	}

	public void setElementStatus(String elementStatus)
	{
		this.elementStatus = elementStatus;
	}
}
