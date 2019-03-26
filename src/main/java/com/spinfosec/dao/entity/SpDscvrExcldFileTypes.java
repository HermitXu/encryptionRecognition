package com.spinfosec.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 扫描任务排除文件类型列表
 *
 * @title [扫描任务排除文件类型列表]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-7-6 下午8:47:44
 */
public class SpDscvrExcldFileTypes implements Serializable
{

	private static final long serialVersionUID = -8639489017301838849L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 外键，参考扫描任务表
	 */
	private String discoveryTaskId;
	/**
	 * 文件类型
	 */
	private String fileType;
	/**
	 * 同一个任务的顺序字段，自动生成
	 */
	private BigDecimal elementIndex;

	/**
	 * 引用的文档特征主键
	 */
	private String filePropertyId;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDiscoveryTaskId()
	{
		return this.discoveryTaskId;
	}

	public void setDiscoveryTaskId(String discoveryTaskId)
	{
		this.discoveryTaskId = discoveryTaskId;
	}

	public String getFileType()
	{
		return this.fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public BigDecimal getElementIndex()
	{
		return elementIndex;
	}

	public void setElementIndex(BigDecimal elementIndex)
	{
		this.elementIndex = elementIndex;
	}

	public String getFilePropertyId()
	{
		return filePropertyId;
	}

	public void setFilePropertyId(String filePropertyId)
	{
		this.filePropertyId = filePropertyId;
	}
}
