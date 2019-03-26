package com.spinfosec.dto.pojo.system;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName Test
 * @Description: 〈用户信息和角色信息模型〉
 * @date 2018/10/9
 * All rights Reserved, Designed By SPINFO
 */
public class CustomSpAdminsBean implements Serializable
{

	/**
	 * 用户ID
	 */
	private String id;

	/**
	 * 用户名
	 */
	private String name;

	/**
	 * 姓名
	 */
	private String userName;

	/**
	 * 用户类型 1  本地用户  默认
	 */
	private String userType;

	/**
	 * 用户使用状态 0激活  1注销  2 休眠
	 */
	private BigDecimal accountIsDisabled;

	/**
	 * C_USER_DEFINE  自定义   C_PRE_DEFINE  预定义
	 */
	private String definitionType;

	/**
	 * 过期时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationDate;

	/**
	 * 注册日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registrationDate;

	/**
	 * 修改密码时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date passwordModifyDate;

	/**
	 * 账户修改标识
	 */
	private BigDecimal passwordChangeFlag;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色类型
	 */
	private String roleType;

	/**
	 * 所属部门ID
	 */
	private String orgId;

	/**
	 * 所属部门名称
	 */
	private String orgName;
	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * SM2加密公钥X
	 */
	private String sm2_pubKeyX;

	/**
	 * SM2加密公钥Y
	 */
	private String sm2_pubKeyY;

	public Date getExpirationDate()
	{
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate)
	{
		this.expirationDate = expirationDate;
	}

	public String getSm2_pubKeyX()
	{
		return sm2_pubKeyX;
	}

	public void setSm2_pubKeyX(String sm2_pubKeyX)
	{
		this.sm2_pubKeyX = sm2_pubKeyX;
	}

	public String getSm2_pubKeyY()
	{
		return sm2_pubKeyY;
	}

	public void setSm2_pubKeyY(String sm2_pubKeyY)
	{
		this.sm2_pubKeyY = sm2_pubKeyY;
	}

	public String getDefinitionType()
	{
		return definitionType;
	}

	public void setDefinitionType(String definitionType)
	{
		this.definitionType = definitionType;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public BigDecimal getPasswordChangeFlag()
	{
		return passwordChangeFlag;
	}

	public void setPasswordChangeFlag(BigDecimal passwordChangeFlag)
	{
		this.passwordChangeFlag = passwordChangeFlag;
	}

	public Date getPasswordModifyDate()
	{
		return passwordModifyDate;
	}

	public void setPasswordModifyDate(Date passwordModifyDate)
	{
		this.passwordModifyDate = passwordModifyDate;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRoleId()
	{
		return roleId;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRoleType()
	{
		return roleType;
	}

	public void setRoleType(String roleType)
	{
		this.roleType = roleType;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public BigDecimal getAccountIsDisabled()
	{
		return accountIsDisabled;
	}

	public void setAccountIsDisabled(BigDecimal accountIsDisabled)
	{
		this.accountIsDisabled = accountIsDisabled;
	}

	public Date getRegistrationDate()
	{
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate)
	{
		this.registrationDate = registrationDate;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
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
}
