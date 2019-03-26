package com.spinfosec.controller.common;

import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.ADConfig;
import com.spinfosec.dto.pojo.system.EmailConnectData;
import com.spinfosec.service.srv.ITestConnectSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.AESPython;
import com.spinfosec.utils.ResultUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.DirContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TestConnectController
 * @Description: 〈测试连接接口〉
 * @date 2018/10/30
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/testConnect")
public class TestConnectController
{
	private static final Logger log = LoggerFactory.getLogger(TestConnectController.class);

	@Autowired
	private ITestConnectSrv testConnectSrv;

	@RequestMapping(value = "/email", method = RequestMethod.POST)
	public @ResponseBody ResponseBean testConnectEmail(HttpServletRequest req,
			@RequestBody EmailConnectData connectData)
	{
		if (null == connectData || StringUtils.isEmpty(connectData.getIp())
				|| StringUtils.isEmpty(connectData.getPort()) || StringUtils.isEmpty(connectData.getEmail()))
		{
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PARAMERTER_ERROR));
		}
		return ResultUtil.getDefinedCodeResult(testConnectSrv.testEmail(connectData));
	}

	/**
	 * 测试连接Exchange
	 * @param req
	 * @param connectData
	 * @return
	 */
	@RequestMapping(value = "/exchange", method = RequestMethod.POST)
	public @ResponseBody ResponseBean testConnectExchange(HttpServletRequest req,
			@RequestBody(required = false) ADConfig connectData)
			throws Exception
	{
		try{
			if (connectData != null)
			{
				if (StringUtils.isNotEmpty(connectData.getAdminPwd()))
				{
					connectData.setAdminPwd(new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, connectData.getAdminPwd())));
				}
				else
				{
					connectData.setAdminPwd(testConnectSrv.getLdapConfig().getAdminPwd());
				}
			}

			DirContext dirContext = testConnectSrv.connectExchange(connectData);
			if (dirContext == null)
			{
				return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.CONNECT_ERROR));
			}
			else
			{
				log.info("-----------Exchange认证成功！----------");
				return ResultUtil.getSuccessResult();
			}
		}
		catch (Exception e)
		{
			log.error("-----------Exchange认证失败！失败原因：",e);
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.DOMAIN_CONNECT_FAIL));
		}


	}

}
