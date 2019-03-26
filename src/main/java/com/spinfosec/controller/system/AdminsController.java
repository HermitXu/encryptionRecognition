package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.AdminReq;
import com.spinfosec.service.srv.IAuthSrv;
import com.spinfosec.service.srv.ISecPasswordSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.dto.pojo.system.CustomSpAdminsBean;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.OperateLogUtil;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AdminsController
 * @Description: 〈用户管理控制层〉
 * @date 2018/10/11
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/admin")
public class AdminsController
{
	private static final Logger log = LoggerFactory.getLogger(AdminsController.class);

	@Autowired
	private ISystemSrv systemSrv;

	@Autowired
	private ISecPasswordSrv secPasswordSrv;

	@Autowired
	private IAuthSrv authSrv;

	/**
	 * 用户分页查询
	 * @param req
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = { "/page" }, method = RequestMethod.GET)
	public @ResponseBody ResponseBean pageUser(HttpServletRequest req, HttpServletResponse rsp)
	{
		Map<String, Object> queryMap = new HashMap<>();

		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");


		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 三权管理员用户只能查询自己创建的用户
		String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		queryMap.put("createdBy", mngUserId);

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 姓名
			String name = querJson.getString("name");
			if (StringUtils.isNotEmpty(name))
			{
				queryMap.put("name", name);
			}

			// 用户名
			String userName = querJson.getString("userName");
			if (StringUtils.isNotEmpty(userName))
			{
				queryMap.put("userName", userName);
			}

			// 角色名
			String roleName = querJson.getString("roleName");
			if (StringUtils.isNotEmpty(roleName))
			{
				queryMap.put("roleName", roleName);
			}

			// 用户类型
			String userType = querJson.getString("userType");
			if (StringUtils.isNotEmpty(userType))
			{
				queryMap.put("userType", userType);
			}

			// 描述
			String description = querJson.getString("DESCRIPTION");
			if (StringUtils.isNotEmpty(description))
			{
				queryMap.put("description", description);
			}

			// 用户状态
			String accountIsDisabled = querJson.getString("accountIsDisabled");
			if (StringUtils.isNotEmpty(accountIsDisabled))
			{
				queryMap.put("accountIsDisabled", accountIsDisabled);
			}

			// 预置或自定义
			String definitionType = querJson.getString("definitionType");
			if (StringUtils.isNotEmpty(definitionType))
			{
				queryMap.put("definitionType", definitionType);
			}

		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("userName"))
			{
				sort = "a.USERNAME";
			}
			else if (sort.equalsIgnoreCase("name"))
			{
				sort = "a.NAME";
			}
			else if (sort.equalsIgnoreCase("roleName"))
			{
				sort = "r.NAME";
			}
			else if (sort.equalsIgnoreCase("userType"))
			{
				sort = "a.USER_TYPE";
			}
			else if (sort.equalsIgnoreCase("registrationDate"))
			{
				sort = "a.REGISTRATION_DATE";
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<CustomSpAdminsBean> pageList = systemSrv.querySpAdminsByPage(queryMap);

		// 对已过期用户状态进行处理
		for (CustomSpAdminsBean admin : pageList.getList())
		{
			Date expirationDate = admin.getExpirationDate();
			if (expirationDate != null)
			{
				long accountLeft = expirationDate.getTime() - System.currentTimeMillis();
				if (accountLeft < 0)
				{
					// 已过期用户 如果为激活状态则修改为已过期状态
					if (!admin.getAccountIsDisabled().toString().equals("3")
							&& admin.getAccountIsDisabled().toString().equals("0"))
					{
						systemSrv.updateAdminState(admin.getId(), "3");
						admin.setAccountIsDisabled(BigDecimal.valueOf(3.0));
					}

				}
				else
				{
					// 未过期用户 如果还处在已过期状态 则修改为激活
					if (admin.getAccountIsDisabled().toString().equals("3"))
					{
						systemSrv.updateAdminState(admin.getId(), "0");
						admin.setAccountIsDisabled(BigDecimal.valueOf(0));
					}
				}
			}
			else
			{
				throw new TMCException(RspCode.PARAMERTER_ERROR);
			}
		}

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 根据ID查询用户信息
	 * @Title: getSpAdminsById
	 * @Description: 根据ID查询用户信息
	 * @param: @param req
	 * @param: @param id
	 * @param: @return
	 * @return: UserRsp
	 * @throws
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getSpAdminsById(HttpServletRequest req, @PathVariable String id)
	{
		return ResultUtil.getSuccessResult(systemSrv.getAdminReqById(id));
	}

	/**
	 * 保存用户信息
	 * @param req
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveSpAdmins(HttpServletRequest req, @RequestBody AdminReq data) throws Exception
	{
		CodeRsp codeRsp = secPasswordSrv.dateValid(
				DateUtil.dateToString(data.getSpAdmins().getExpirationDate(), DateUtil.DATETIME_FORMAT_PATTERN), "1");

		if (!codeRsp.getCode().equals(RspCode.SUCCESS.getCode()))
		{
			return ResultUtil.getFailResult(codeRsp);
		}

		String userId = req.getSession().getAttribute(SessionItem.userId.name()).toString();
		String decPwd = data.getSpAdmins().getPassword();
		String errors = secPasswordSrv.passwordValid(decPwd, data.getSpAdmins().getUsername());
		if (errors.length() > 0)
		{
			codeRsp = new CodeRsp(RspCode.PASSWORD_COMPLEXITY_VALID_FAIL);
			codeRsp.setMsg(errors);
			return ResultUtil.getFailResult(codeRsp);
		}

		systemSrv.saveSpAdmin(data, userId);

		return ResultUtil.getSuccessResult();
	}

	/**
	 * 更新用户信息
	 * @param req
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateSpAdmins(HttpServletRequest req, @RequestBody AdminReq data)
			throws Exception
	{
		// CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		// // 更新时不传递密码从数据库中查询
		// SpAdmins admins = systemSrv.getAdminById(data.getSpAdmins().getId());
		// String decPwd = admins.getPassword();
		// String errors = secPasswordSrv.passwordValid(decPwd, data.getSpAdmins().getUsername());
		// if (errors.length() > 0)
		// {
		// codeRsp = new CodeRsp(RspCode.PASSWORD_COMPLEXITY_VALID_FAIL);
		// codeRsp.setMsg(errors);
		// return ResultUtil.getFailResult(codeRsp);
		// }

		CodeRsp codeRsp = secPasswordSrv.dateValid(
				DateUtil.dateToString(data.getSpAdmins().getExpirationDate(), DateUtil.DATETIME_FORMAT_PATTERN), "1");

		if (!codeRsp.getCode().equals(RspCode.SUCCESS.getCode()))
		{
			return ResultUtil.getFailResult(codeRsp);
		}
		systemSrv.updateSpAdmin(data);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 通过id删除用户信息
	 * @param req
	 * @param parms 放在body中的参数集合
	 * @return
	 */
	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteSpAdmins(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody Map<String, Object> parms)
	{
		List<String> idList = (List<String>) parms.get("ids");
		if (!idList.isEmpty())
		{
			RspCode rspCode = systemSrv.deleteSpAdmins(idList);
			return ResultUtil.getFailResult(new CodeRsp(rspCode));
		}
		else
		{
			log.error("delete by ids,ids is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
	}

	/**
	 * 忘记密码
	 * @param req
	 * @param name
	 * @param email
	 * @return
	 * @throws EmailException
	 */
	@RequestMapping(value = "/findUserPwd/{name}/{email}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean findUserPwd(HttpServletRequest req, @PathVariable String name,
			@PathVariable String email)
	{
		try
		{
			systemSrv.forgetPassword(name, email);
			return ResultUtil.getSuccessResult();
		}
		catch (EmailException e)
		{
			e.printStackTrace();
			return ResultUtil.getFailResult(new CodeRsp(RspCode.CONNECT_EMAIL_AUTH_ERROR));
		}
		catch (IllegalArgumentException e)
		{
			return ResultUtil.getFailResult(new CodeRsp(RspCode.CONNECT_EMAIL_AUTH_ERROR));
		}
		catch (TMCException e)
		{
			log.error("找回密码失败：用户名或电子邮箱不存在。", e);
			CodeRsp codeRsp = new CodeRsp(RspCode.FAILURE);
			codeRsp.setMsg("找回密码失败：用户名或电子邮箱不存在。");
			return ResultUtil.getFailResult(codeRsp);
		}
		catch (Exception e)
		{
			log.error("找回密码失败：失败原因：", e);
			return ResultUtil.getFailResult(new CodeRsp(RspCode.FAILURE));
		}

	}

	/**
	 * 首次登陆修改密码
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updatePassword(HttpServletRequest req, @RequestBody JSONObject jsonObject)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		String oldPwdEnc = jsonObject.getString("oldPwd");
		String newPwdEnc = jsonObject.getString("newPwd");
		String userName = jsonObject.getString("userName");

		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		operateLogInfo.setOperation("首次登陆/密码过期修改密码操作");
		CustomSpAdminsBean userInfo = authSrv.findLogUserInfo(userName);
		String currentUserId = null;
		if (null != userInfo)
		{
			currentUserId = userInfo.getId();
			operateLogInfo.setAdminId(userInfo.getId());
			operateLogInfo.setRoleId(userInfo.getRoleId());
			operateLogInfo.setAdminName(userInfo.getUserName());
			operateLogInfo.setRoleName(userInfo.getRoleName());
		}
		codeRsp = systemSrv.updatePassword(oldPwdEnc, newPwdEnc, currentUserId);

		// 记录操作日志
		systemSrv.saveOperateLog(operateLogInfo);
		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

	/**
	 * 修改用户状态
	 * @param request
	 * @param id
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAdminState/{id}/{state}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateAdminState(HttpServletRequest request, @PathVariable String id,
			@PathVariable String state) throws Exception
	{
		systemSrv.updateAdminState(id, state);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 用户管理中管理员重置密码
	 * @param req
	 * @param parms 放在body中的参数集合
	 * @return
	 */
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public @ResponseBody ResponseBean resetPwd(HttpServletRequest req, @RequestBody Map<String, Object> parms)
	{
		String id = (String) parms.get("id");
		String newPwd = (String) parms.get("newPwd");
		String isSendEmail = (String) parms.get("isSendEmail");
		CodeRsp rsp = null;
		try
		{
			rsp = systemSrv.resetPwd(id, newPwd, isSendEmail, req);
		}
		catch (Exception e)
		{
			rsp = new CodeRsp(RspCode.RESET_PASSWORD_ERROR);
			log.error("重置用户密码失败：" + e.getMessage(), e);
		}
		return ResultUtil.getDefinedCodeResult(rsp);
	}

}
