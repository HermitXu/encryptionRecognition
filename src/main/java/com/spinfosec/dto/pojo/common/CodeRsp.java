package com.spinfosec.dto.pojo.common;

import com.spinfosec.system.RspCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 状态码说明集合
 */
@ApiModel(value = "状态码对象")
public class CodeRsp
{
	@ApiModelProperty(name = "状态码", required = true)
	private String code;

	@ApiModelProperty(name = "提示信息", required = true)
	private String msg;

	public CodeRsp(RspCode e)
	{
		this.code = e.getCode();
		this.msg = e.getDescription();
	}

	public CodeRsp()
	{
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
