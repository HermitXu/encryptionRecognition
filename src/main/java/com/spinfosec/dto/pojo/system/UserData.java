package com.spinfosec.dto.pojo.system;

public class UserData
{
	private String id;

	private String name;

	private String oldPwd;

	private String newPwd;

	private String showPrompt;

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

	public String getOldPwd()
	{
		return oldPwd;
	}

	public void setOldPwd(String oldPwd)
	{
		this.oldPwd = oldPwd;
	}

	public String getNewPwd()
	{
		return newPwd;
	}

	public void setNewPwd(String newPwd)
	{
		this.newPwd = newPwd;
	}

	public String getShowPrompt()
	{
		return showPrompt;
	}

	public void setShowPrompt(String showPrompt)
	{
		this.showPrompt = showPrompt;
	}

}