package com.spinfosec.system;

import java.util.Date;

/**
 * @ClassName: LoginFailedInfo
 * @Description:登录失败信息
 * @author: zhangpf
 * @date: 2014-9-17 下午7:09:26 All rights Reserved, Designed By SPINFO Copyright:
 *        Copyright(C) 2010-2011
 */
public class LoginFailedInfo
{
	/**
	 * 登录失败次数
	 */
	private int failedTime;

	/**
	 * 锁定IP起始时间
	 */
	private Date lockStartDate;

	/**
	 * 最后一次输入错误的时间
	 */
	private Date lastFailedTime;

	/**
	 * 失败后验证码
	 */
	private String authCode;

	public LoginFailedInfo()
	{
		super();
	}

	public int getFailedTime()
	{
		return failedTime;
	}

	public void setFailedTime(int failedTime)
	{
		this.failedTime = failedTime;
	}

	public Date getLockStartDate()
	{
		return lockStartDate;
	}

	public void setLockStartDate(Date lockStartDate)
	{
		this.lockStartDate = lockStartDate;
	}

	public String getAuthCode()
	{
		return authCode;
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}

	public Date getLastFailedTime()
	{
		return lastFailedTime;
	}

	public void setLastFailedTime(Date lastFailedTime)
	{
		this.lastFailedTime = lastFailedTime;
	}
}
