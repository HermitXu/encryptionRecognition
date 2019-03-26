package com.spinfosec.dto.pojo.system;

import com.spinfosec.dao.entity.SpSecPasswordPolicy;

import java.util.List;

/**
 * @author ank
 * @version V1.0
 * @title 密码安全策略设置表单数据
 * @Package com.spinfosec.dto.pojo.system
 * @description 密码安全策略设置表单数据
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @create 2015/5/25 11:59
 */
public class SecPasswordPolicyFormData
{
	/**
	 * 密码策略实体
	 */
	SpSecPasswordPolicy secPasswordPolicy;
	/**
	 * 密码策略复杂度
	 */
	List<String> secPasswordComplexityItem;

	/**
	 * 
	 * @Title: getSecPasswordPolicy
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param: @return   
	 * @return: SpSecPasswordPolicy   
	 * @throws
	 */
	public SpSecPasswordPolicy getSecPasswordPolicy()
	{
		return secPasswordPolicy;
	}

	/**
	 * 
	 * @Title: setSecPasswordPolicy
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param: @param secPasswordPolicy   
	 * @return: void   
	 * @throws
	 */
	public void setSecPasswordPolicy(SpSecPasswordPolicy secPasswordPolicy)
	{
		this.secPasswordPolicy = secPasswordPolicy;
	}

	/**
	 * 
	 * @Title: getSecPasswordComplexityItem
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param: @return   
	 * @return: List<String>   
	 * @throws
	 */
	public List<String> getSecPasswordComplexityItem()
	{
		return secPasswordComplexityItem;
	}

	/**
	 * 
	 * @Title: setSecPasswordComplexityItem
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param: @param secPasswordComplexityItem   
	 * @return: void   
	 * @throws
	 */
	public void setSecPasswordComplexityItem(List<String> secPasswordComplexityItem)
	{
		this.secPasswordComplexityItem = secPasswordComplexityItem;
	}
}
