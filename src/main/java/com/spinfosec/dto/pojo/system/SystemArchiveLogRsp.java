package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.dto.msg.system.SystemArchiveLogRsp
 * @description [一句话描述]
 * @create 2017/11/16 20:15
 * @copyright Copyright(C) 2017 SHIPING INFO Corporation. All rights reserved.
 */
public class SystemArchiveLogRsp
{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 存储路径
	 */
	private String localPath;

	/**
	 * 下载次数
	 */
	private int downLoadOptlock;

	/**
	 * 创建日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 文件大小
	 */
	private BigDecimal fileSize;

	/**
	 * 备份数据类型 - 业务数据 BUSI, 系统数据 SYSTEM, 安全数据 SAFE
	 */
	private String type;

	/**
	 * 恢复次数
	 */
	private int recoverOptlock;

	/**
	 * 状态  '状态：0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败'
	 */
	private String status;

	/**
	 * 创建者userId
	 */
	private String createdBy;

	private String createdUserName;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLocalPath()
	{
		return localPath;
	}

	public void setLocalPath(String localPath)
	{
		this.localPath = localPath;
	}

	public int getDownLoadOptlock()
	{
		return downLoadOptlock;
	}

	public void setDownLoadOptlock(int downLoadOptlock)
	{
		this.downLoadOptlock = downLoadOptlock;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public BigDecimal getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(BigDecimal fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getRecoverOptlock()
	{
		return recoverOptlock;
	}

	public void setRecoverOptlock(int recoverOptlock)
	{
		this.recoverOptlock = recoverOptlock;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getCreatedUserName()
	{
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName)
	{
		this.createdUserName = createdUserName;
	}
}
