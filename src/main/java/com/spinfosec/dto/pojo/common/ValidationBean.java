package com.spinfosec.dto.pojo.common;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ValidationBean
 * @Description: 〈校验数据模型〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
public class ValidationBean implements Serializable
{

	private static final long serialVersionUID = 4663666622404988444L;
	/**
	 * 主键的属性名（ID，排除本身）
	 */
	private String pkName;
	/**
	 * 主键值
	 */
	private String pkValue;

	/**
	 * 模型名称（表名）
	 */
	private String entityName;

	/**
	 * 检查字段名
	 */
	private String checkName;

	/**
	 * 检查字段值
	 */
	private String checkValue;

	/**
	 * 附加验证字段
	 */
	private Map<String, String> conditions;


	public ValidationBean()
	{
	}

	public ValidationBean(String pkName, String pkValue, String entityName, String checkName, String checkValue)
	{
		this.pkName = pkName;
		this.pkValue = pkValue;
		this.entityName = entityName;
		this.checkName = checkName;
		this.checkValue = checkValue;
	}

	public String getPkName()
	{
		return pkName;
	}

	public void setPkName(String pkName)
	{
		this.pkName = pkName;
	}

	public String getPkValue()
	{
		return pkValue;
	}

	public void setPkValue(String pkValue)
	{
		this.pkValue = pkValue;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public String getCheckName()
	{
		return checkName;
	}

	public void setCheckName(String checkName)
	{
		this.checkName = checkName;
	}

	public String getCheckValue()
	{
		return checkValue;
	}

	public void setCheckValue(String checkValue)
	{
		this.checkValue = checkValue;
	}

	public Map<String, String> getConditions()
	{
		return conditions;
	}

	public void setConditions(Map<String, String> conditions)
	{
		this.conditions = conditions;
	}

	@Override
	public String toString()
	{
		return "ValidationBean [pkName=" + pkName + ", pkValue=" + pkValue + ", entityName=" + entityName
				+ ", checkName=" + checkName + ", checkValue=" + checkValue + "]";
	}

}
