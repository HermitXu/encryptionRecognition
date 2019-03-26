package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SysOperateArchiveLogRsp
 * @Description: 〈系统操作日志归档信息封装〉
 * @date 2018/10/25
 * All rights Reserved, Designed By SPINFO
 */
public class SysOperateArchiveLogRsp implements Serializable
{

	private static final long serialVersionUID = -9097289488348821082L;
	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 状态  '0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败'
	 */
	private String status;

	/**
	 * 起始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startDate;

	/**
	 * 终止时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endDate;

	/**
	 * 事件编号
	 */
	private String incidentNum;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

	/**
	 * 下载次数
	 */
	private int downOptLock;

	/**
	 * 恢复次数
	 */
	private int recoverOptLock;

	/**
	 * 是否删除原记录
	 */
	private String isDel;

	/**
	 * 创建者ID
	 */
	private String createdBy;

	/**
	 * 创建者名称
	 */
	private String createdUserName;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getIncidentNum()
	{
		return incidentNum;
	}

	public void setIncidentNum(String incidentNum)
	{
		this.incidentNum = incidentNum;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public int getDownOptLock()
	{
		return downOptLock;
	}

	public void setDownOptLock(int downOptLock)
	{
		this.downOptLock = downOptLock;
	}

	public int getRecoverOptLock()
	{
		return recoverOptLock;
	}

	public void setRecoverOptLock(int recoverOptLock)
	{
		this.recoverOptLock = recoverOptLock;
	}

	public String getIsDel()
	{
		return isDel;
	}

	public void setIsDel(String isDel)
	{
		this.isDel = isDel;
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
