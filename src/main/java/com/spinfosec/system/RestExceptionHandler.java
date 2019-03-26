package com.spinfosec.system;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName RestExceptionHandler
 * @Description: 〈Rest接口统一异常处理〉
 * @date 2018/12/25
 * All rights Reserved, Designed By SPINFO
 */
@Component
public class RestExceptionHandler implements HandlerExceptionResolver
{

	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse rsp, Object o, Exception ex)
	{
		if (TMCException.class.isInstance(ex))
		{
			try
			{
				TMCException e = (TMCException) ex;
				log.error(e.getMessage(), e);

				RspCode errCode = e.getErrCode();
				CodeRsp codeRsp = new CodeRsp(errCode);
				rsp.getWriter().write(JSONObject.toJSONString(new ResponseBean(codeRsp)));
			}
			catch (IOException e1)
			{
				log.error("print error msg json to HttpServletResponse failed", e1);
			}
		}
		else
		{
			// 其他Exception 异常处理
			log.error(RspCode.INNER_ERROR.getDescription(), ex);
			try
			{
				CodeRsp codeRsp = new CodeRsp(RspCode.INNER_ERROR);
				rsp.getWriter().write(JSONObject.toJSONString(new ResponseBean(codeRsp)));
			}
			catch (IOException e2)
			{
				log.error("print error msg json to HttpServletResponse failed", e2);
			}
		}
		return new ModelAndView();
	}
}
