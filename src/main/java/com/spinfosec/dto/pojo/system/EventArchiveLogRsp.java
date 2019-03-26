package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.dto.msg.event.EventArchiveLogRsp
 * @description [一句话描述]
 * @create 2017/11/16 19:19
 * @copyright Copyright(C) 2017 SHIPING INFO Corporation. All rights reserved.
 */
public class EventArchiveLogRsp implements Serializable
{

	private static final long serialVersionUID = 2948819869571628616L;
	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 状态  '状态：0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败 '
	 */
	private String status;

	/**
	 * 是否删除原记录
	 */
	private String isDel;
	/**
	 * 开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startDate;
	/**
	 * 结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endDate;
	/**
	 * 时间编号
	 */
	private String incidentNum;
	/**
	 * 路径
	 */
	private String path;
	/**
	 * 注释
	 */
	private String description;
	/**
	 * 创建日期
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
	 * 创建者
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

	public String getIsDel()
	{
		return isDel;
	}

	public void setIsDel(String isDel)
	{
		this.isDel = isDel;
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
