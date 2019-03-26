package com.spinfosec.dto.pojo.system;

/**
 * @ClassName:     PasswordValidData
 * @Description:修改密码时验证的参数对象
 * @author:    ank
 * @date:        2015-7-29
 * All rights Reserved, Designed By SPINFO
 * Copyright:    Copyright(C) 2010-2011
 */
public class PasswordValidData
{

	/**
	 * 禁止用户口令与用户名相同或包含用户名；
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 确认密码
	 */
	private String newPwd;
	private String temp;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getNewPwd()
	{
		return newPwd;
	}

	public void setNewPwd(String newPwd)
	{
		this.newPwd = newPwd;
	}

	public String getTemp()
	{
		return temp;
	}

	public void setTemp(String temp)
	{
		this.temp = temp;
	}
}
