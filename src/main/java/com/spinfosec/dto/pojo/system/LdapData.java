package com.spinfosec.dto.pojo.system;

/**
 * Exchange连接信息获取
 */
public class LdapData
{

	private String id;

	private String cn;

	private String ou;

	private String sAMAccountName;

	private String name;

	private String email;

	private String password;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCn()
	{
		return cn;
	}

	public void setCn(String cn)
	{
		this.cn = cn;
	}

	public String getOu()
	{
		return ou;
	}

	public void setOu(String ou)
	{
		this.ou = ou;
	}

	public String getsAMAccountName()
	{
		return sAMAccountName;
	}

	public void setsAMAccountName(String sAMAccountName)
	{
		this.sAMAccountName = sAMAccountName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
