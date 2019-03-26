package com.spinfosec.dto.pojo.system.tatic;

import com.spinfosec.dao.entity.SpAlgorithmFileType;
import com.spinfosec.dao.entity.SpDiscoveryTasks;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DiscoveryTaskFormData
 * @Description: 〈扫描策略表单数据封装〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
public class DiscoveryTaskFormData
{
	/**
	 * 扫描任务主表
	 */
	private SpDiscoveryTasks discoveryTasks;

	/**
	 * 主机资源
	 */
	private TargetResFormData targetResFormData;

	/**
	 * 调度信息时间
	 */
	private SchedulingData schedulingData;

	/**
	 * 扫描任务包含文件类型列表
	 */
	private List<String> incldFileTypes;

	/**
	 * 扫描任务排除文件类型列表
	 */
	private List<String> excldFileTypes;

	/**
	 * 加密算法文件类型关系集合
	 */
	private List<SpAlgorithmFileType> algorithmFileTypeList;

	public List<SpAlgorithmFileType> getAlgorithmFileTypeList()
	{
		return algorithmFileTypeList;
	}

	public void setAlgorithmFileTypeList(List<SpAlgorithmFileType> algorithmFileTypeList)
	{
		this.algorithmFileTypeList = algorithmFileTypeList;
	}

	public SpDiscoveryTasks getDiscoveryTasks()
	{
		return discoveryTasks;
	}

	public void setDiscoveryTasks(SpDiscoveryTasks discoveryTasks)
	{
		this.discoveryTasks = discoveryTasks;
	}

	public TargetResFormData getTargetResFormData()
	{
		return targetResFormData;
	}

	public void setTargetResFormData(TargetResFormData targetResFormData)
	{
		this.targetResFormData = targetResFormData;
	}

	public SchedulingData getSchedulingData()
	{
		return schedulingData;
	}

	public void setSchedulingData(SchedulingData schedulingData)
	{
		this.schedulingData = schedulingData;
	}

	public List<String> getIncldFileTypes()
	{
		return incldFileTypes;
	}

	public void setIncldFileTypes(List<String> incldFileTypes)
	{
		this.incldFileTypes = incldFileTypes;
	}

	public List<String> getExcldFileTypes()
	{
		return excldFileTypes;
	}

	public void setExcldFileTypes(List<String> excldFileTypes)
	{
		this.excldFileTypes = excldFileTypes;
	}
}
