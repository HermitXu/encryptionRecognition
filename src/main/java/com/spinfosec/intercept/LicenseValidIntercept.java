package com.spinfosec.intercept;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.ILicenseSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ank
 * @version v 1.0
 * @title [License鉴权拦截器]
 * @ClassName: com.spinfosec.intercept.LicenseValidIntercept
 * @description [License鉴权拦截器]
 * @create 2018/12/3 15:26
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class LicenseValidIntercept implements HandlerInterceptor
{
    @Autowired
    private ILicenseSrv licenseSrv;

    private Logger logger = LoggerFactory.getLogger(LicenseValidIntercept.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
		String uri = request.getRequestURI();
		// 不做鉴权的有：
		// 登陆 是否显示验证码 创建验证码 登出 许可管理 密码校验 忘记密码 首次登陆修改密码 下载PDF 任务中心导出报告 安全日志下载 Ukey驱动下载
		// TODO 异常抛出 会出现/error请求
		if (uri.contains("/auth/login") || uri.contains("/auth/isShowAuthCode") || uri.contains("/auth/createAuthCode")
				|| uri.contains("/common/logout") || uri.contains("/error") || uri.contains("/license")
				|| uri.contains("/system/secPassword/passwordValid") || uri.contains("/system/admin/findUserPwd")
				|| uri.contains("/system/admin/updatePassword") || uri.contains("/export") || uri.contains("/download")
				|| uri.contains("/common/getMenu") || uri.contains("swagger"))
		{
			return true;
		}
		CodeRsp failResp = new CodeRsp(RspCode.LICENSE_VERIFY_FAIL_NEND_TO_RELOGIN);
        try
        {
            logger.info("开始检查license");
            if (licenseSrv.checkLicense())
            {
                logger.info("检查license  OK");
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("检查license 异常：" + e.getMessage(), e);
			failResp = new CodeRsp(RspCode.LICENSE_VERIFY_FAIL_NEND_TO_RELOGIN);
        }
        // 验证失败
        ResponseBean failResult = ResultUtil.getFailResult(failResp);
        ResultUtil.response(response, JSONObject.toJSONString(failResult));
        return false;
    }
}
