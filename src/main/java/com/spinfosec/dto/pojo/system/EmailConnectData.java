package com.spinfosec.dto.pojo.system;

/**
 * @title ConnectEmailData
 * @description TODO(用一句话描述该文件做什么)
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author zhangpf
 * @version V1.5.0.1
 * @create 2015年8月4日 上午9:59:42   
 */
public class EmailConnectData
{

	/**
	 * 邮件服务器所在ip
	 */
	private String ip;

	/**
	 * 邮件端口
	 */
	private String port;

	/**
	 * 邮件服务器发送邮件地址
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	public EmailConnectData()
	{
		super();
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
