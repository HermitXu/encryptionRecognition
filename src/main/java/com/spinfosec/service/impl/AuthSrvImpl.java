package com.spinfosec.service.impl;

import com.spinfosec.dao.AdminDao;
import com.spinfosec.dao.SysOperateLogDao;
import com.spinfosec.dao.entity.SpAdminHostSetting;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.Token;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.UserData;
import com.spinfosec.service.srv.IAuthSrv;
import com.spinfosec.dto.pojo.system.CustomSpAdminsBean;
import com.spinfosec.system.LoginFailedInfo;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.EmailUtil;
import com.spinfosec.utils.GenUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AuthSrvImpl
 * @Description: 〈鉴权服务接口实现类〉
 * @date 2018/10/9
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("authSrv")
public class AuthSrvImpl implements IAuthSrv
{
	private static final Logger log = LoggerFactory.getLogger(AuthSrvImpl.class);


	@Autowired
	private AdminDao adminDao;

	@Autowired
	private SysOperateLogDao sysOperateLogDao;

	/**
	 * 通过用户名查询用户
	 * @param userName 用户名
	 * @return
	 */
	@Override
	public List<SpAdmins> findUserByName(String userName)
	{
		return adminDao.findUserByName(userName);
	}

	/**
	 * 通过用户名查询用户信息和角色信息
	 * @param userName
	 * @return
	 */
	@Override
	public CustomSpAdminsBean findLogUserInfo(String userName)
	{
		return adminDao.findLogUserInfo(userName);
	}

	/**
	 * 通过用户ID获取信任主机IP
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> getHostIpByUserId(String userId)
	{
		List<String> hostIp = new ArrayList<>();
		List<SpAdminHostSetting> hostSettings = adminDao.findTrustHostByUserId(userId);
		for (SpAdminHostSetting hostSetting : hostSettings)
		{
			hostIp.add(hostSetting.getHostIp());
		}
		return hostIp;
	}

	/**
	 * 鉴权获取token
	 * @return
	 */
	@Override
	public Token getToken()
	{
		Token token = new Token();
		String tokenId = GenUtil.getUUID();
		token.setTokenId(tokenId);
		log.info("get a tocken success!");
		return token;
	}

	/**
	 * 初始化用户权限
	 * @param userId
	 */
	@Override
	public List<TreeData> initRoleFunctions(String userId) throws IOException
	{
		CustomSpAdminsBean customSpAdminsBean = adminDao.findLoginInfoByUserId(userId);
		if (null == customSpAdminsBean)
		{
			log.info("该用户没有对应的角色信息！");
			return null;
		}

		// 角色ID
		String roleId = customSpAdminsBean.getRoleId();
		List<SpCodeDecodes> spCodeDecodesList = adminDao.getRoleUrlFunctions(roleId);
		if (spCodeDecodesList.isEmpty())
		{
			log.info("用户获取权限失败！");
			return null;
		}

		List<TreeData> moduleTreeData = GenUtil.getModuleTree(spCodeDecodesList, true);

		return moduleTreeData;
	}

	@Override
	public String setLoginInfo(String loginIp, String logUserName, Integer maxLoginTimes)
	{
		String failedDes = "";
		LoginFailedInfo failedInfoNew = MemInfo.getLoginFailedMap().get(loginIp);
		// 第一次登录失败 创建登录失败对象并存储在缓存中
		if (null == failedInfoNew)
		{
			failedInfoNew = new LoginFailedInfo();
			failedInfoNew.setFailedTime(1);
			failedDes = "登录失败 " + failedInfoNew.getFailedTime() + " 次,剩余 "
					+ (maxLoginTimes - failedInfoNew.getFailedTime()) + "次机会! ";
			MemInfo.getLoginFailedMap().put(loginIp, failedInfoNew);
		}
		else
		{
			// 判断是否达到最大限制次数
			if (failedInfoNew.getFailedTime() >= maxLoginTimes)
			{
				failedInfoNew.setFailedTime(failedInfoNew.getFailedTime() + 1);
				MemInfo.getLoginFailedMap().put(loginIp, failedInfoNew);
				long leave = System.currentTimeMillis() - failedInfoNew.getLockStartDate().getTime();
				int leaveMinute = 10 - (int) (leave / (60 * 1000));
				failedDes = "登录失败次数过多,请于" + leaveMinute + "分钟后重新登录系统! ";

				// 生成操作日志
				CustomSpAdminsBean logUserInfo = findLogUserInfo(logUserName);
				String roleName = "";
				String roleId = "";
				String userId = "";
				if (logUserInfo != null)
				{
					userId = logUserInfo.getId();
					roleId = logUserInfo.getRoleId();
					roleName = logUserInfo.getRoleName();
				}

				SpSystemOperateLogInfo operateLogInfo = new SpSystemOperateLogInfo();
				operateLogInfo.setId(GenUtil.getUUID());
				operateLogInfo.setDiscriminator("discriminator");
				operateLogInfo.setIsLeaderForTx(1);
				operateLogInfo.setTransactionId("transactionId");
				operateLogInfo.setGenerationTimeTs(new Date());
				operateLogInfo.setAdminId(userId);
				operateLogInfo.setAdminName(logUserName);
				operateLogInfo.setRoleId(roleId);
				operateLogInfo.setRoleName(roleName);
				operateLogInfo.setOperation("用户 : " + logUserName + " 在 " + loginIp + " 尝试登录失败次数为 : " + maxLoginTimes
						+ "次，登录ip被锁定，如果本次登录非本人操作，请及时修改密码！");
				sysOperateLogDao.saveOperateLog(operateLogInfo);

				// 系统管理员发送告警邮件
				SpAdmins sysadmin = adminDao.findSysadminInfo();
				String email = sysadmin.getEmail();
				String userName = sysadmin.getName();
				String subject = "加密文件检查系统登录异常告警";
				String content = "hi " + userName + ",<br/>" + "加密文件检查系统登录异常告警：" + " 用户 : " + logUserName + " 在 "
						+ loginIp + "尝试登录失败次数为 : " + maxLoginTimes + "次，登录ip被锁定，如果本次登录非本人操作，请及时修改密码！";
				log.info("发送邮件内容：" + content);
				try
				{
					EmailUtil.sendHtmlMail(subject, content, null, email);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			} // 判断还有几次登录机会
			else
			{
				failedInfoNew.setFailedTime(failedInfoNew.getFailedTime() + 1);
				MemInfo.getLoginFailedMap().put(loginIp, failedInfoNew);
				failedDes = "登录失败 " + failedInfoNew.getFailedTime() + " 次,剩余 "
						+ (maxLoginTimes - failedInfoNew.getFailedTime()) + "次机会! ";
				if ((maxLoginTimes - failedInfoNew.getFailedTime()) == 0)
				{
					failedInfoNew.setLockStartDate(new Date());
				}
			}
		}
		// 存储登录失败时间
		failedInfoNew.setLastFailedTime(new Date());
		return failedDes;
	}

	/**
	 * 获取当前用户的具体信息
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public CustomSpAdminsBean getCurruserUserById(String userId) throws Exception
	{

		return adminDao.getCurruserUserById(userId);
	}

	/**
	 * 修改当前用户密码
	 * @param data
	 * @param req
	 */
	@Override
	public void updateCurrentUser(UserData data, HttpServletRequest req)
	{
		if (null != data)
		{
			SpAdmins user = adminDao.findSpAdminById(data.getId());
			if (null == user)
			{
				throw new TMCException(RspCode.OBJECE_NOT_EXIST);
			}
			// 更新密码
			if (StringUtils.isNotEmpty(data.getOldPwd()) && StringUtils.isNotEmpty(data.getNewPwd()))
			{
				// 旧密码解密
				String decryptOldPass = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, data.getOldPwd()));
				// 数据库密码解密
				String decryptUsePass = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, user.getPassword()));
				if (decryptOldPass.equals(decryptUsePass))
				{
					user.setPassword(data.getNewPwd());
					user.setPasswordChangeFlag(new Double("1"));
					user.setPasswordModifyDate(new Date());
					adminDao.updateSpAdmin(user);
				}
				else
				{
					throw new TMCException(RspCode.OLD_PASSWORD_ERROR);
				}
			}
		}
	}
}
