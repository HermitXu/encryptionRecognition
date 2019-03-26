package com.spinfosec.dto.pojo.system.tatic;

import com.spinfosec.dao.entity.SpColumnResInfo;
import com.spinfosec.dao.entity.SpTargetRes;
import com.spinfosec.dao.entity.SpTargetResDetail;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TargetResFormData
 * @Description: 〈目标资源表单信息封装〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
public class TargetResFormData
{
	/**
	 * 主机资源
	 */
	private SpTargetRes targetRes;

	/**
	 * 主机资源详情
	 */
	private List<SpTargetResDetail> targetResDetailList;

	/**
	 * 列设置的相关信息
	 */
	private List<SpColumnResInfo> columnResInfos;

	public List<SpColumnResInfo> getColumnResInfos()
	{
		return columnResInfos;
	}

	public void setColumnResInfos(List<SpColumnResInfo> columnResInfos)
	{
		this.columnResInfos = columnResInfos;
	}

	public SpTargetRes getTargetRes()
	{
		return targetRes;
	}

	public void setTargetRes(SpTargetRes targetRes)
	{
		this.targetRes = targetRes;
	}

	public List<SpTargetResDetail> getTargetResDetailList()
	{
		return targetResDetailList;
	}

	public void setTargetResDetailList(List<SpTargetResDetail> targetResDetailList)
	{
		this.targetResDetailList = targetResDetailList;
	}
}
