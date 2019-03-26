package com.spinfosec.controller.system;

import com.spinfosec.dao.entity.SpSecPasswordComplexityItem;
import com.spinfosec.dao.entity.SpSecPasswordPolicy;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.PasswordValidData;
import com.spinfosec.dto.pojo.system.SecPasswordPolicyFormData;
import com.spinfosec.service.srv.ISecPasswordSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SecPasswordController
 * @Description: 〈密码安全策略控制层〉
 * @date 2018/10/12
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/secPassword")
public class SecPasswordController
{
	private static final Logger log = LoggerFactory.getLogger(SecPasswordController.class);

	@Autowired
	private ISecPasswordSrv secPasswordSrv;

	/**
	 * 获取密码安全策略
	 * @return
	 */
	@RequestMapping(value = { "/getSecPasswordPolicy" }, method = RequestMethod.GET)
	public @ResponseBody ResponseBean getComplexityItem(HttpServletRequest req, HttpServletResponse resp)
	{
		SpSecPasswordPolicy list = secPasswordSrv.getSpSecPasswordPolicy();
		return ResultUtil.getSuccessResult(list);
	}

	/**
	 * 获取密码复杂度选项
	 * @param req
	 * @return List<SpSecPasswordComplexityItem>
	 */
	@RequestMapping(value = "/getComplexityItem", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getComplexityItem(HttpServletRequest req)
	{
		List<SpSecPasswordComplexityItem> list = secPasswordSrv.getComplexityItem();
		return ResultUtil.getSuccessResult(list);
	}

	/**
	 * 保存密码安全策略
	 * @param req
	 * @param data
	 * @return CodeRsp
	 */
	@RequestMapping(value = "/saveSecPasswordPolicy", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveSecPasswordPolicy(HttpServletRequest req,
			@RequestBody SecPasswordPolicyFormData data)
	{
		secPasswordSrv.saveSecPasswordPolicy(req, data);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 密码后台校验
	 * @param req
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/passwordValid", method = RequestMethod.POST)
	public @ResponseBody ResponseBean passwordValid(HttpServletRequest req, @RequestBody PasswordValidData data)
			throws Exception
	{
		String password = data.getNewPwd();
		String name = data.getName();
		if (StringUtils.isEmpty(password))
		{
			password = data.getPassword();
		}
		String errors = secPasswordSrv.passwordValid(password, name);
		if (errors.length() == 0)
		{
			return ResultUtil.getSuccessResult();
		}
		else
		{
			CodeRsp codeRsp = new CodeRsp(RspCode.PASSWORD_COMPLEXITY_VALID_FAIL);
			codeRsp.setMsg(errors);
			return ResultUtil.getDefinedCodeResult(codeRsp);
		}
	}
}
