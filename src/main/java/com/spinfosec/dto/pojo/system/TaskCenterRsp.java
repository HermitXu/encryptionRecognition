package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TaskCenterRsp
 * @Description: 〈任务中心信息封装模型〉
 * @date 2018/10/26
 * All rights Reserved, Designed By SPINFO
 */
public class TaskCenterRsp implements Serializable
{

	private static final long serialVersionUID = 5487493802598286445L;
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 部署状态  SYNCHRONIZED（已部署）, UNSYNCHRONIZED_NEW(新建未部署), UNSYNCHRONIZED_EDIT（编辑未部署）
	 */
	private String elementStatus;

	/**
	 * 检查目标
	 */
	private String ip;
	/**
	 * 策略ID
	 */
	private String jobId;
	/**
	 * 任务名称(和策略名称一致)
	 */
	private String name;

	/**
	 * 和策略类型一致
	 */
	private String type;
	/**
	 * 任务开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;
	/**
	 * 任务结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	/**
	 * 失败原因
	 */
	private String failReason;

	/**
	 * 运行结果
	 */
	private String result;
	/**
	 * 状态进度
	 */
	private String status;

	/**
	 * 是否是最新任务
	 */
	private String isLast;

	/**
	 * 数据库时，为总表数，非数据库时为总记录数
	 */
	private String totalCount;
	/**
	 * 数据库时为总记录数
	 */
	private String totalRecordCount;
	/**
	 * 数据库时，为成功表数，非数据库时为成功记录数
	 */
	private String successCount;
	/**
	 * 数据库时为成功记录数
	 */
	private String successRecordCount;
	/**
	 * 数据库时，为失败表数，非数据库时为失败记录数
	 */
	private String failCount;
	/**
	 * 数据库时为失败记录数
	 */
	private String failRecordCount;
	/**
	 * 数据大小
	 */
	private String size;

	/**
	 * 关联组织ID
	 */
	private String orgId;
	/**
	 * 关联组织名称
	 */
	private String orgName;

	/**
	 * 创建者
	 */
	private String createdName;

	/**
	 * 子任务
	 */
	private List<TaskCenterRsp> childTask;

	public TaskCenterRsp()
	{
	}

	public TaskCenterRsp(String id, String jobId, String name, String type, Date startTime, Date endTime,
			String failReason, String result, String status, String isLast, String totalCount, String totalRecordCount,
			String successCount, String successRecordCount, String failCount, String failRecordCount, String size,
			String orgId, String orgName)
	{
		this.id = id;
		this.jobId = jobId;
		this.name = name;
		this.type = type;
		this.startTime = startTime;
		this.endTime = endTime;
		this.failReason = failReason;
		this.result = result;
		this.status = status;
		this.isLast = isLast;
		this.totalCount = totalCount;
		this.totalRecordCount = totalRecordCount;
		this.successCount = successCount;
		this.successRecordCount = successRecordCount;
		this.failCount = failCount;
		this.failRecordCount = failRecordCount;
		this.size = size;
		this.orgId = orgId;
		this.orgName = orgName;
	}

	public TaskCenterRsp(String id, String taskId, String jobId, String name, String type, Date startTime, Date endTime,
			String failReason, String result, String status, String isLast, String totalCount, String totalRecordCount,
			String successCount, String successRecordCount, String failCount, String failRecordCount, String size,
			String orgId, String orgName)
	{
		this.id = id;
		this.taskId = taskId;
		this.jobId = jobId;
		this.name = name;
		this.type = type;
		this.startTime = startTime;
		this.endTime = endTime;
		this.failReason = failReason;
		this.result = result;
		this.status = status;
		this.isLast = isLast;
		this.totalCount = totalCount;
		this.totalRecordCount = totalRecordCount;
		this.successCount = successCount;
		this.successRecordCount = successRecordCount;
		this.failCount = failCount;
		this.failRecordCount = failRecordCount;
		this.size = size;
		this.orgId = orgId;
		this.orgName = orgName;
	}

	public String getCreatedName()
	{
		return createdName;
	}

	public void setCreatedName(String createdName)
	{
		this.createdName = createdName;
	}

	public List<TaskCenterRsp> getChildTask()
	{
		return childTask;
	}

	public void setChildTask(List<TaskCenterRsp> childTask)
	{
		this.childTask = childTask;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getElementStatus()
	{
		return elementStatus;
	}

	public void setElementStatus(String elementStatus)
	{
		this.elementStatus = elementStatus;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public String getFailReason()
	{
		return failReason;
	}

	public void setFailReason(String failReason)
	{
		this.failReason = failReason;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getIsLast()
	{
		return isLast;
	}

	public void setIsLast(String isLast)
	{
		this.isLast = isLast;
	}

	public String getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(String totalCount)
	{
		this.totalCount = totalCount;
	}

	public String getTotalRecordCount()
	{
		return totalRecordCount;
	}

	public void setTotalRecordCount(String totalRecordCount)
	{
		this.totalRecordCount = totalRecordCount;
	}

	public String getSuccessCount()
	{
		return successCount;
	}

	public void setSuccessCount(String successCount)
	{
		this.successCount = successCount;
	}

	public String getSuccessRecordCount()
	{
		return successRecordCount;
	}

	public void setSuccessRecordCount(String successRecordCount)
	{
		this.successRecordCount = successRecordCount;
	}

	public String getFailCount()
	{
		return failCount;
	}

	public void setFailCount(String failCount)
	{
		this.failCount = failCount;
	}

	public String getFailRecordCount()
	{
		return failRecordCount;
	}

	public void setFailRecordCount(String failRecordCount)
	{
		this.failRecordCount = failRecordCount;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}
}
