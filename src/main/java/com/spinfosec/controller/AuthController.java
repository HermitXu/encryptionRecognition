package com.spinfosec.controller;

import com.alibaba.fastjson.JSONObject;

import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.common.Token;
import com.spinfosec.dto.pojo.system.CustomSpAdminsBean;
import com.spinfosec.dto.pojo.system.UserData;
import com.spinfosec.listener.SessionUserListener;
import com.spinfosec.security.ukey.SM2SM3;
import com.spinfosec.service.srv.IAuthSrv;
import com.spinfosec.service.srv.ILicenseSrv;
import com.spinfosec.service.srv.ISecPasswordSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.*;
import com.spinfosec.utils.*;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AuthController
 * @Description: 〈用户登录〉
 * @date 2018/10/9
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/auth")
public class AuthController
{

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	public static final int DAY = 24 * 60 * 60 * 1000;

	public static final int MINUTE = 60 * 1000;

	@Autowired
	private IAuthSrv authSrv;

	@Autowired
	private ISecPasswordSrv secPasswordSrv;

	@Autowired
	private ILicenseSrv licenseSrv;

	@Autowired
	private ISystemSrv systemSrv;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody JSONObject login(HttpServletRequest req, HttpServletResponse rsp,
			@RequestBody Map<String, Object> parms) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);

		String logUserName = (String) parms.get("username");
		String logPassword = (String) parms.get("password");
		String authCode = (String) parms.get("authCode");
		String signData = (String) parms.get("signData");


		// 解密后密码
		logPassword = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, logPassword));

		if (StringUtils.isNotEmpty(authCode))
		{
			try
			{
				authCode = URLEncoder.encode(authCode, "utf-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		String loginIp = IpUtils.getIpAddr(req);
		Integer maxLoginTimes = Integer
				.valueOf(MemInfo.getSecPasswordPolicyInfo().get(SessionItem.maxLoginTimes.name()));
		LoginFailedInfo failedInfo = MemInfo.getLoginFailedMap().get(loginIp);
		// 登录失败次数小于限制次数时，此计数24小时有效
		if (null != failedInfo)
		{
			if (System.currentTimeMillis() - failedInfo.getLastFailedTime().getTime() > DAY)
			{
				MemInfo.getLoginFailedMap().remove(loginIp);
			}
			else
			{
				int failedTime = failedInfo.getFailedTime();

				if (failedTime >= maxLoginTimes)
				{
					if (System.currentTimeMillis() - failedInfo.getLockStartDate().getTime() < MINUTE * 10)
					{
						String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
						RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
						throw new TMCException(RspCode.LOGIN_FAILED);
					}
					else
					{
						MemInfo.getLoginFailedMap().remove(loginIp);
					}
				}
			}
		}

		// 检验账户的有效期
		List<SpAdmins> admins = authSrv.findUserByName(logUserName);
		if (null != admins && admins.size() > 0 && null != admins.get(0))
		{
			SpAdmins admin = admins.get(0);
			Date expirationDate = admin.getExpirationDate();
			if (expirationDate != null)
			{
				long accountLeft = expirationDate.getTime() - System.currentTimeMillis();
				if (accountLeft < 0)
				{
					throw new TMCException(RspCode.ACCOUNT_OUT_OF_DATE);
				}
			}
			else
			{
				throw new TMCException(RspCode.PARAMERTER_ERROR);
			}

		}

		if (null != failedInfo && failedInfo.getFailedTime() > 0)
		{
			String serverCode = failedInfo.getAuthCode();
			if (StringUtils.isEmpty(serverCode) || StringUtils.isEmpty(authCode))
			{
				log.error("auth code is null.");
				String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
				failedDes += "失败原因 : 验证码错误!";
				RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
				throw new TMCException(RspCode.LOGIN_FAILED);
			}
			if (!authCode.equalsIgnoreCase(serverCode))
			{
				String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
				failedDes += "失败原因 : 验证码错误!";
				RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
				throw new TMCException(RspCode.LOGIN_FAILED);
			}
		}

		// 通过用户名先查询用户 因为SM2加密密文每次都不一样 没办法通过密文去对比
		CustomSpAdminsBean logUserInfo = authSrv.findLogUserInfo(logUserName);
		if (null == logUserInfo)
		{
			String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
			failedDes += "失败原因 : 用户名或密码错误!";
			RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
			throw new TMCException(RspCode.LOGIN_FAILED);
		}
		else
		{
			String findPassword = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, logUserInfo.getPassword()));
			if (!findPassword.equals(logPassword))
			{
				String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
				failedDes += "失败原因 : 用户名或密码错误!";
				RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
				throw new TMCException(RspCode.LOGIN_FAILED);
			}
		}

		// 是否启动Ukey
		if (null != logUserInfo)
		{
			String isUkey = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.ukeyEnable.name());
			if (isUkey.equalsIgnoreCase(Contants.YES))
			{
				try
				{
					String data = MemInfo.getLoginRandom(loginIp).toString();
					if (!SM2SM3.YtVerfiy(logUserName, data, logUserInfo.getSm2_pubKeyX(), logUserInfo.getSm2_pubKeyY(),
							signData.trim()))
					{
						String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
						failedDes += "失败原因 : 该用户不是合法用户!";
						RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
						throw new TMCException(RspCode.LOGIN_FAILED);
					}
				}
				catch (TMCException e)
				{
					throw e;
				}
				catch (Exception e)
				{
					String failedDes = authSrv.setLoginInfo(loginIp, logUserName, maxLoginTimes);
					failedDes += "失败原因 : 该用户不是合法用户!";
					RspCode.LOGIN_FAILED.setDescription(RspCode.LOGIN_FAILED, failedDes);
					throw new TMCException(RspCode.LOGIN_FAILED);
				}
			}
		}

		// 先判断用户状态（可能密码在有效期内但被管理员休眠或注销）处于休眠状态
		if ("2".equals(logUserInfo.getAccountIsDisabled().toString()))
		{
			throw new TMCException(RspCode.ACCOUNT_IS_SLEEP);
			// 用户处于注销状态
		}
		else if ("1".equals(logUserInfo.getAccountIsDisabled().toString()))
		{
			throw new TMCException(RspCode.ACCOUNT_IS_CANCEL);
		}

		// 判断是否启动了信任主机
		String secHostEnable = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.sechostEnable.name());
		// 启动了信任主机则判断是否为该用户添加了信任主机IP
		if ("1".equalsIgnoreCase(secHostEnable))
		{
			String userId = logUserInfo.getId();
			List<String> hostIps = authSrv.getHostIpByUserId(userId);
			// 如果未给该用户添加信任主机IP 则正常登录
			if (null != hostIps && !hostIps.isEmpty())
			{
				if (!hostIps.contains(loginIp))
				{
					log.error("非法ip登录，登录ip地址为： " + loginIp);
					throw new TMCException(RspCode.INVALID_IP);
				}
			}

		}

		// 判断首次登陆是否需要修改密码
		String isModifyPasswordFirst = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.isModifyPasswordFirst.name());
		if (Contants.YES.equals(isModifyPasswordFirst))
		{
			// 判断用户密码是否已经修改
			BigDecimal passwordChangeFlag = logUserInfo.getPasswordChangeFlag();
			if (Contants.NO.equals(passwordChangeFlag.toString()))
			{
				codeRsp = new CodeRsp(RspCode.MODIFY_PASSWORD_FIRST);
				jsonObject.put("codeRsp", codeRsp);
				return jsonObject;
			}
		}

		// 获取密码有效期
		String passwordValidity = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.passwordValidity.name());
		// 密码修改日期为null可能从来没改过密码 从注册日期开始
		Date passwordModifyDate = logUserInfo.getPasswordModifyDate() != null ? logUserInfo.getPasswordModifyDate()
				: logUserInfo.getRegistrationDate();

		long validTime = 0l;
		if (passwordValidity.equals("1"))
		{
			// 一周
			validTime = 7 * Contants.DAY;
		}
		else if (passwordValidity.equals("2"))
		{
			// 一月
			validTime = 30 * Contants.DAY;
		}
		else if (passwordValidity.equals("3"))
		{
			// 一季度
			validTime = 90 * Contants.DAY;
		}
		long modifyTime = passwordModifyDate.getTime();
		// 过期
		if (System.currentTimeMillis() - modifyTime >= validTime)
		{
			codeRsp = new CodeRsp(RspCode.PASSWORD_OUT_OF_DATE);
			jsonObject.put("codeRsp", codeRsp);
			return jsonObject;
		}
		// 即将过期
		if (System.currentTimeMillis() - modifyTime <= validTime
				&& modifyTime + validTime - System.currentTimeMillis() <= 3 * Contants.DAY)
		{
			codeRsp = new CodeRsp(RspCode.PASSWORD_WILL_OUT_OF_DATE);
		}

		log.info("登录ip地址为： " + loginIp);


		// 判断是否可以重复登录
		String isRepeatLogin = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.isRepeatLogin.name());
		String requestInfo = SessionUserListener.userIds.get(logUserInfo.getId());
		String userAgent = req.getHeader("User-Agent");
		if (Contants.NO.equals(isRepeatLogin))
		{
			// 限制只能一个用户登录
			if (StringUtils.isNotEmpty(requestInfo))
			{
				if (!requestInfo.equalsIgnoreCase(loginIp + userAgent))
				{
					throw new TMCException(RspCode.USER_LOGIN_AGING);
				}
			}
		}

		SessionUserListener.userIds.put(logUserInfo.getId(), loginIp + userAgent);

		log.info("当前会话人数：" + SessionUserListener.userIds.size() + "人");

		// 记录用户最后登录时间
		SpAdmins user = systemSrv.getAdminById(logUserInfo.getId());
		user.setLastLoginTime(new Date());
		systemSrv.updateSpAdmin(user);

		// 登录成功,去除登录失败信息
		MemInfo.getLoginFailedMap().remove(loginIp);

		// 获取鉴权token
		Token token = authSrv.getToken();

		// 保存tokenId到session
		req.getSession().setAttribute(SessionItem.tokenId.name(), token.getTokenId());
		// 用户ID
		req.getSession().setAttribute(SessionItem.userId.name(), logUserInfo.getId());
		// 用户名称
		req.getSession().setAttribute(SessionItem.userName.name(), logUserInfo.getUserName());
		// 姓名
		req.getSession().setAttribute(SessionItem.name.name(), logUserInfo.getName());
		// 用户邮箱
		req.getSession().setAttribute(SessionItem.email.name(), logUserInfo.getEmail());
		// 角色ID
		req.getSession().setAttribute(SessionItem.roleId.name(), logUserInfo.getRoleId());
		// 角色名称
		req.getSession().setAttribute(SessionItem.roleName.name(), logUserInfo.getRoleName());

		// 初始化用户的权限
		// String userId = logUserInfo.getId();
		// List<TreeData> moduleTreeDataList = authSrv.initRoleFunctions(userId);
		// if (null == moduleTreeDataList || moduleTreeDataList.isEmpty())
		// {
		// log.info("用户获取权限失败！");
		// throw new TMCException(RspCode.LOGIN_FAILED);
		// }
		// 将用户权限初始化到session
		// req.getSession().setAttribute(SessionItem.userFunctions.name(), moduleTreeDataList);

		JSONObject data = new JSONObject();
		data.put("tokenId", token.getTokenId());
		data.put("userInfo", logUserInfo);
		// data.put("menu", moduleTreeDataList);
		jsonObject.put("data", data);
		jsonObject.put("codeRsp", codeRsp);

		// 针对登录操作，需要记录登录ip 所以此处操作日志单独记录
		SpSystemOperateLogInfo systemOperateLogInfo = OperateLogUtil.packageSysLog(req);
		systemOperateLogInfo.setOperation("用户登录，登录IP：" + loginIp);
		systemOperateLogInfo.setResult(0);
		systemSrv.saveOperateLog(systemOperateLogInfo);

		return jsonObject;
	}

	// 判断是否要显示验证码
	@RequestMapping(value = "/isShowAuthCode", method = RequestMethod.POST)
	public @ResponseBody ResponseBean isShowAuthCode(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		JSONObject data = new JSONObject();
		String remoteIp = IpUtils.getIpAddr(req);

		// 查看当前IP是否存在登陆失败信息
		LoginFailedInfo failedInfo = MemInfo.getLoginFailedMap().get(remoteIp);

		// 判断是否使用ukey
		String isUkey = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.ukeyEnable.name());
		data.put("isUkey", isUkey);

		// ukey签名使用的随机数和IP绑定
		int rnd = 0;
		if (null == MemInfo.getLoginRandom(remoteIp))
		{
			rnd = (int) (Math.random() * 65535) + 10;
			MemInfo.addLoginRandom(remoteIp, rnd);
		}
		else
		{
			rnd = MemInfo.getLoginRandom(remoteIp);
		}
		data.put("rnd", rnd);

		//更新防重放时间戳（与服务器时间同步）
		resp.setHeader("Date", DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN));

		if (null != failedInfo && failedInfo.getFailedTime() > 0)
		{
			data.put("isShowCode", true);
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.SHOW_AUTH_CODE), data);
		}
		else
		{
			data.put("isShowCode", false);
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.HIDE_AUTH_CODE), data);
		}
	}

	/**
	 * 输出验证码图片
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	@RequestMapping(value = "/createAuthCode", method = RequestMethod.GET)
	public @ResponseBody void createAuthCode(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		// 设置不缓存图片
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "No-cache");
		resp.setDateHeader("Expires", 0);
		// 指定生成的相应图片
		resp.setContentType("image/jpeg");

		String authCode = AuthCodeUtil.generateVerifyCode(4);
		LoginFailedInfo failedInfo = MemInfo.getLoginFailedMap().get(req.getRemoteAddr());
		if (null != failedInfo)
		{
			failedInfo.setAuthCode(authCode);
		}
		int w = 90, h = 34;
		AuthCodeUtil.outputImage(w, h, resp.getOutputStream(), authCode);
	}

	/**
	 * 查询当前用户信息
	 *
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/curruser", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getCurrUser(HttpServletRequest req, HttpServletResponse rsp) throws Exception
	{
		Object userId = req.getSession().getAttribute(SessionItem.userId.name());
		if (null == userId)
		{
			throw new TMCException(RspCode.INVALID_SESSION);
		}
		CustomSpAdminsBean curruserUser = authSrv.getCurruserUserById(userId.toString());
		if (null == curruserUser)
		{
			throw new TMCException(RspCode.OBJECE_NOT_EXIST);
		}
		return ResultUtil.getSuccessResult(curruserUser);
	}

	/**
	 * 修改当前用户个人配置/密码
	 * @param request
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateCurrentUser", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateCurrentUser(HttpServletRequest request, @RequestBody UserData data)
			throws Exception
	{
		if (null != data.getNewPwd() && StringUtils.isNotEmpty(data.getNewPwd()))
		{
			String errors = secPasswordSrv.passwordValid(data.getNewPwd(), data.getName());
			if (errors.length() > 0)
			{
				CodeRsp codeRsp = new CodeRsp(RspCode.PASSWORD_COMPLEXITY_VALID_FAIL);
				codeRsp.setMsg(errors);
				return ResultUtil.getDefinedCodeResult(codeRsp);
			}
		}
		authSrv.updateCurrentUser(data, request);
		return ResultUtil.getSuccessResult();
	}

}
