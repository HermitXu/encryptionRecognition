package com.spinfosec.utils;

import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.system.RspCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ResultUtil
 * @Description: 〈控制层统一响应格式工具类〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
public class ResultUtil
{
    private static Logger logger = LoggerFactory.getLogger(ResultUtil.class);

	public ResultUtil()
	{
	}

	/**
	 * 成功带数据响应结果
	 * @param data
	 * @return
	 */
	public static ResponseBean getSuccessResult(Object data)
	{
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCodeRsp(new CodeRsp(RspCode.SUCCESS));
		responseBean.setData(data);
		return responseBean;
	}

	/**
	 * 成功响应结果
	 * @return
	 */
	public static ResponseBean getSuccessResult()
	{
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCodeRsp(new CodeRsp(RspCode.SUCCESS));
		responseBean.setData("");
		return responseBean;
	}

	/**
	 * 失败响应结果
	 * @param codeRsp
	 * @return
	 */
	public static ResponseBean getFailResult(CodeRsp codeRsp)
	{
		return getFailResult(codeRsp, (Object) "");
	}

	/**
	 * 失败带数据响应结果
	 * @param codeRsp
	 * @param data
	 * @return
	 */
	public static ResponseBean getFailResult(CodeRsp codeRsp, Object data)
	{
		ResponseBean resultInfo = new ResponseBean();
		resultInfo.setCodeRsp(codeRsp);
		resultInfo.setData(data);
		return resultInfo;
	}

	/**
	 * 自定义响应结果
	 * @param codeRsp
	 * @return
	 */
	public static ResponseBean getDefinedCodeResult(CodeRsp codeRsp)
	{
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCodeRsp(codeRsp);
		responseBean.setData("");
		return responseBean;
	}

	/**
	 * 自定义响应结果带数据
	 * @param codeRsp
	 * @param data
	 * @return
	 */
	public static ResponseBean getDefinedCodeResult(CodeRsp codeRsp, Object data)
	{
		ResponseBean responseBean = new ResponseBean();
		responseBean.setCodeRsp(codeRsp);
		responseBean.setData(data);
		return responseBean;
	}

    /**
     * 写入响应对象
     * @param response
     * @param json
     * @throws Exception
     */
    public static void response(HttpServletResponse response, String json) throws Exception
    {
        PrintWriter out = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try
        {
            out = response.getWriter();
            out.print(json);
        }
        catch (Exception e)
        {
            logger.error("write response error", e);
        }
        finally
        {
            if (null != out)
            {
                out.close();
            }
        }
    }

}
