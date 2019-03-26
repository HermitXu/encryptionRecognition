package com.spinfosec.dto.pojo.system;

/**
 * @title [标题]
 * @description [Exchange认证设置连接模型封装]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013年11月12日 下午9:33:23
 */
public class ADConfig
{

	String host = "";
	int port = 0;
	String rootDN = "";
	String adminUser = "";
	String adminPwd = "";
	String useSsl = "";

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getRootDN()
	{
		return rootDN;
	}

	public void setRootDN(String rootDN)
	{
		this.rootDN = rootDN;
	}

	public String getAdminUser()
	{
		return adminUser;
	}

	public void setAdminUser(String adminUser)
	{
		this.adminUser = adminUser;
	}

	public String getAdminPwd()
	{
		return adminPwd;
	}

	public void setAdminPwd(String adminPwd)
	{
		this.adminPwd = adminPwd;
	}

	public String getUseSsl()
	{
		return useSsl;
	}

	public void setUseSsl(String useSsl)
	{
		this.useSsl = useSsl;
	}

	@Override
	public String toString()
	{
		return "ADConfig [host=" + host + ", port=" + port + ", rootDN=" + rootDN + ", adminUser=" + adminUser
				+ ", adminPwd=" + adminPwd + ", useSsl=" + useSsl + "]";
	}

}
