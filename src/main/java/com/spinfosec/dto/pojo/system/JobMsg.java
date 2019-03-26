package com.spinfosec.dto.pojo.system;

/**
 * @title JobMsg.java
 * @Package com.spinfosec.dto.msg.task
 * @description TODO(用一句话描述该文件做什么)
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author sunaj
 * @version V1.0
 * @create 2014年7月15日 上午10:30:54
 */
public class JobMsg
{

	private String jpbId;

	private String code;

	public String getJpbId()
	{
		return jpbId;
	}

	public void setJpbId(String jpbId)
	{
		this.jpbId = jpbId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	@Override
	public String toString()
	{
		return "JobMsg [jpbId=" + jpbId + ", code=" + code + "]";
	}

}
