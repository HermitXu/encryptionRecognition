package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DscvrFilesRsp
 * @Description: 〈检查事件信息模型〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
public class DscvrFilesRsp
{
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 事件id
	 */
	private String incExternalId;

	/**
	 * 状态（任务）
	 */
	private String status;
	/**
	 * 策略id
	 */
	private String jobId;
	/**
	 * 策略名称
	 */
	private String jobName;
	/**
	 * 入库时间
	 */
	private Date insertDate;
	/**
	 * 是否审核
	 */
	private double isAuthorized;
	/**
	 * 检查时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date detectDateTs;
	/**
	 * 检查时区
	 */
	private String detectDateTz;
	/**
	 * 本地检查时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date localDetectTs;
	/**
	 * 本地检查时区
	 */
	private String localDetectTz;
	/**
	 * 路径
	 */
	private String filePath;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 后缀
	 */
	private String fileExtension;
	/**
	 * 主机名
	 */
	private String hostName;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * MAC
	 */
	private String mac;
	/**
	 * 文件大小
	 */
	private double fileSize;
	/**
	 * 访问时间
	 */
	private Date fdateAccessedTs;
	/**
	 * 访问时区
	 */
	private String fdateAccessedTz;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date fdateCreatedTs;
	/**
	 * 创建时区
	 */
	private String fdateCreatedTz;
	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date fdateModifiedTs;
	/**
	 * 修改时区
	 */
	private String fdateModifiedTz;
	/**
	 * 组织资源ID
	 */
	private String orgId;
	/**
	 * 组织资源名称
	 */
	private String orgName;
	/**
	 * 数据库名称
	 */
	private String databaseName;
	/**
	 * 数据库类型
	 */
	private String databaseType;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 发件人
	 */
	private String forwardedFrom;
	/**
	 * 发件部门
	 */
	private String department;
	/**
	 * 收件人
	 */
	private String inetSendTo;
	/**
	 * 抄送
	 */
	private String inetCopyTo;
	/**
	 * 邮件主题
	 */
	private String emailSubject;
	/**
	 * 发送时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date sendTime;
	/**
	 * 源文件md5值
	 */
	private String md5Value;
	/**
	 * 是否加密
	 */
	private double isEncrypt;
	/**
	 * 算法类型
	 */
	private String algorithmType;

	/**
	 * 事件类型
	 */
	private String taskType;

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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getIncExternalId()
	{
		return incExternalId;
	}

	public void setIncExternalId(String incExternalId)
	{
		this.incExternalId = incExternalId;
	}

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public Date getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(Date insertDate)
	{
		this.insertDate = insertDate;
	}

	public double getIsAuthorized()
	{
		return isAuthorized;
	}

	public void setIsAuthorized(double isAuthorized)
	{
		this.isAuthorized = isAuthorized;
	}

	public Date getDetectDateTs()
	{
		return detectDateTs;
	}

	public void setDetectDateTs(Date detectDateTs)
	{
		this.detectDateTs = detectDateTs;
	}

	public String getDetectDateTz()
	{
		return detectDateTz;
	}

	public void setDetectDateTz(String detectDateTz)
	{
		this.detectDateTz = detectDateTz;
	}

	public Date getLocalDetectTs()
	{
		return localDetectTs;
	}

	public void setLocalDetectTs(Date localDetectTs)
	{
		this.localDetectTs = localDetectTs;
	}

	public String getLocalDetectTz()
	{
		return localDetectTz;
	}

	public void setLocalDetectTz(String localDetectTz)
	{
		this.localDetectTz = localDetectTz;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
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

	public String getHostName()
	{
		return hostName;
	}

	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public double getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(double fileSize)
	{
		this.fileSize = fileSize;
	}

	public Date getFdateAccessedTs()
	{
		return fdateAccessedTs;
	}

	public void setFdateAccessedTs(Date fdateAccessedTs)
	{
		this.fdateAccessedTs = fdateAccessedTs;
	}

	public String getFdateAccessedTz()
	{
		return fdateAccessedTz;
	}

	public void setFdateAccessedTz(String fdateAccessedTz)
	{
		this.fdateAccessedTz = fdateAccessedTz;
	}

	public Date getFdateCreatedTs()
	{
		return fdateCreatedTs;
	}

	public void setFdateCreatedTs(Date fdateCreatedTs)
	{
		this.fdateCreatedTs = fdateCreatedTs;
	}

	public String getFdateCreatedTz()
	{
		return fdateCreatedTz;
	}

	public void setFdateCreatedTz(String fdateCreatedTz)
	{
		this.fdateCreatedTz = fdateCreatedTz;
	}

	public Date getFdateModifiedTs()
	{
		return fdateModifiedTs;
	}

	public void setFdateModifiedTs(Date fdateModifiedTs)
	{
		this.fdateModifiedTs = fdateModifiedTs;
	}

	public String getFdateModifiedTz()
	{
		return fdateModifiedTz;
	}

	public void setFdateModifiedTz(String fdateModifiedTz)
	{
		this.fdateModifiedTz = fdateModifiedTz;
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

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getDatabaseType()
	{
		return databaseType;
	}

	public void setDatabaseType(String databaseType)
	{
		this.databaseType = databaseType;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getForwardedFrom()
	{
		return forwardedFrom;
	}

	public void setForwardedFrom(String forwardedFrom)
	{
		this.forwardedFrom = forwardedFrom;
	}

	public String getDepartment()
	{
		return department;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public String getInetSendTo()
	{
		return inetSendTo;
	}

	public void setInetSendTo(String inetSendTo)
	{
		this.inetSendTo = inetSendTo;
	}

	public String getInetCopyTo()
	{
		return inetCopyTo;
	}

	public void setInetCopyTo(String inetCopyTo)
	{
		this.inetCopyTo = inetCopyTo;
	}

	public String getEmailSubject()
	{
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	public Date getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getMd5Value()
	{
		return md5Value;
	}

	public void setMd5Value(String md5Value)
	{
		this.md5Value = md5Value;
	}

	public double getIsEncrypt()
	{
		return isEncrypt;
	}

	public void setIsEncrypt(double isEncrypt)
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

}
