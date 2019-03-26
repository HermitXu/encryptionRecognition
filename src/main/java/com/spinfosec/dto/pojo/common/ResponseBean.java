package com.spinfosec.dto.pojo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ResponseModel
 * @Description: 〈响应格式格式化〉
 * @date 2018/10/23
 * All rights Reserved, Designed By SPINFO
 */
@ApiModel(value = "返回前台对象")
public class ResponseBean<T> implements Serializable
{

	private static final long serialVersionUID = -8252893844968101434L;
	/**
	 * 响应状态码
	 */
	@ApiModelProperty(name = "响应状态码", required = true)
	private CodeRsp codeRsp;

	/**
	 * 响应数据
	 */
	@ApiModelProperty(name = "响应数据", required = true)
	private T data;

	public ResponseBean()
	{
	}

	public ResponseBean(CodeRsp codeRsp)
	{
		this.codeRsp = codeRsp;
	}

	public CodeRsp getCodeRsp()
	{
		return codeRsp;
	}

	public void setCodeRsp(CodeRsp codeRsp)
	{
		this.codeRsp = codeRsp;
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}
}
