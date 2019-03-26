package com.spinfosec.intercept;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ank
 * @version v 1.0
 * @title [鉴权拦截器]
 * @ClassName: com.spinfosec.intercept.AuthIntercept
 * @description [鉴权拦截器]
 * @create 2018/10/31 16:22
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class AuthIntercept implements HandlerInterceptor
{

    private Logger logger = LoggerFactory.getLogger(AuthIntercept.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
		String uri = request.getRequestURI();
		// 不做鉴权的有：
		// 登陆 是否显示验证码 创建验证码 登出 许可管理 密码校验（未登录时修改密码） 忘记密码 首次登陆修改密码 下载PDF 任务中心导出报告 安全日志下载 Ukey驱动下载
		// TODO 异常抛出 会出现/error请求
		if (uri.contains("/auth/login") || uri.contains("/auth/isShowAuthCode") || uri.contains("/auth/createAuthCode")
				|| uri.contains("/common/logout") || uri.contains("/error") || uri.contains("/license")
				|| uri.contains("/system/secPassword/passwordValid") || uri.contains("/system/admin/findUserPwd")
				|| uri.contains("/system/admin/updatePassword") || uri.contains("/export") || uri.contains("/download")
				|| uri.contains("/upload") || uri.contains("swagger"))
		{
			return true;
		}

        CodeRsp failResp;
        // 判断用户是否session超时
        String userId = (String) request.getSession().getAttribute(SessionItem.userId.name());
        logger.debug("currentUserId = " + userId);
        if (StringUtils.isNotEmpty(userId))
        {
            // 用户未失效  判断token
            String authorization = request.getHeader("authorization");
            if (StringUtils.isNotEmpty(authorization))
            {
                String tokenId = (String) request.getSession().getAttribute(SessionItem.tokenId.name());
                if (StringUtils.isNotEmpty(tokenId))
                {
                    if (tokenId.equalsIgnoreCase(authorization))
                    {
                        return true;
                    }
                    else
                    {
                        logger.info("请求中token和session中token不一致，token失效！");
                        failResp = new CodeRsp(RspCode.INVALID_TOKENID);
                    }
                }
                else
                {
                    logger.info("session中token失效");
                    failResp = new CodeRsp(RspCode.INVALID_TOKENID);
                }
            }
            else
            {
                logger.info("请求中缺失token！");
                failResp = new CodeRsp(RspCode.INVALID_TOKENID);
            }
        }
        else
        {
            logger.info("用户session失效！");
            failResp = new CodeRsp(RspCode.INVALID_SESSION);
        }
        // 验证失败
        ResponseBean failResult = ResultUtil.getFailResult(failResp);
        ResultUtil.response(response, JSONObject.toJSONString(failResult));
        return false;
    }
}
