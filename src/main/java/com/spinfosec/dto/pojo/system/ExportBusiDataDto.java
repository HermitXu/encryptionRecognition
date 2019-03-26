package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ExportBusiDataDto
 * @Description: 〈导出条件封装〉
 * @date 2018/10/31
 * All rights Reserved, Designed By SPINFO
 */
public class ExportBusiDataDto
{
	/**
	 * 策略名称（与任务名称相同）
	 */
	private String jobName;

	/**
	 * 策略ID
	 */
	private String jobId;

	/**
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 任务类型
	 */
	private String taskType;

	/**
	 * 任务状态
	 */
	private String status;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 文件类型
	 */
	private String fileExtension;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 检查时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date detectDateTs;

	/**
	 * 任务IP
	 */
	private String ip;

	/**
	 * 是否加密 0.否 1.是
	 */
	private String isEncrypt;

	/**
	 * 算法类型
	 */
	private String algorithmType;

	/**
	 * 创建者
	 */
	private String createdBy;

	/**
	 * 检查开始时间
	 */
	private String detectDateTs_beginTime;

	/**
	 * 检查结束时间
	 */
	private String detectDateTs_endTime;

	/**
	 * 以哪个字段排序
	 */
	private String sort;

	/**
	 * 升序 或 降序
	 */
	private String order;

	public String getTaskId()
	{
		return taskId;
	}

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getDetectDateTs_endTime()
	{
		return detectDateTs_endTime;
	}

	public void setDetectDateTs_endTime(String detectDateTs_endTime)
	{
		this.detectDateTs_endTime = detectDateTs_endTime;
	}

	public String getDetectDateTs_beginTime()
	{
		return detectDateTs_beginTime;
	}

	public void setDetectDateTs_beginTime(String detectDateTs_beginTime)
	{
		this.detectDateTs_beginTime = detectDateTs_beginTime;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public String getTaskType()
	{
		return taskType;
	}

	public void setTaskType(String taskType)
	{
		this.taskType = taskType;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(String fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public Date getDetectDateTs()
	{
		return detectDateTs;
	}

	public void setDetectDateTs(Date detectDateTs)
	{
		this.detectDateTs = detectDateTs;
	}

	public String getIsEncrypt()
	{
		return isEncrypt;
	}

	public void setIsEncrypt(String isEncrypt)
	{
		this.isEncrypt = isEncrypt;
	}

	public String getAlgorithmType()
	{
		return algorithmType;
	}

	public void setAlgorithmType(String algorithmType)
	{
		this.algorithmType = algorithmType;
	}

	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
	}

	public String getOrder()
	{
		return order;
	}

	public void setOrder(String order)
	{
		this.order = order;
	}
}
