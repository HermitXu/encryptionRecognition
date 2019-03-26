package com.spinfosec.dto.pojo.system;

import java.util.List;

/**
 * @title OperateMsg.java
 * @Package com.spinfosec.dto.msg.task
 * @description TODO(用一句话描述该文件做什么)
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author sunaj
 * @version V1.0
 * @create 2014年7月15日 上午10:28:59
 */
public class OperateMsg
{

	private String type;

	private String msgFormat;

	private List<JobMsg> content;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getMsgFormat()
	{
		return msgFormat;
	}

	public void setMsgFormat(String msgFormat)
	{
		this.msgFormat = msgFormat;
	}

	public List<JobMsg> getContent()
	{
		return content;
	}

	public void setContent(List<JobMsg> content)
	{
		this.content = content;
	}

}
