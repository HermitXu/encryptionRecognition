package com.spinfosec.service.impl;

import com.spinfosec.dao.AdminDao;
import com.spinfosec.dao.SecPasswordPolicyDao;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpSecPasswordComplexityItem;
import com.spinfosec.dao.entity.SpSecPasswordPolicy;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.SecPasswordPolicyFormData;
import com.spinfosec.service.srv.ISecPasswordSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.OperateLogUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SecPasswordSrvImpl
 * @Description: 〈密码安全策略实现类〉
 * @date 2018/10/12
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("secPasswordSrv")
public class SecPasswordSrvImpl implements ISecPasswordSrv
{
	private static final long DAY = 1 * 24 * 60 * 60 * 1000l;

	@Autowired
	private SecPasswordPolicyDao secPasswordPolicyDao;

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 获取密码安全策略
	 * @return
	 */
	@Override
	public SpSecPasswordPolicy getSpSecPasswordPolicy()
	{
		return secPasswordPolicyDao.getSpSecPasswordPolicy();
	}

	/**
	 * 获取密码复杂度选项
	 * @return List<SpSecPasswordComplexityItem>
	 */
	@Override
	public List<SpSecPasswordComplexityItem> getComplexityItem()
	{
		return secPasswordPolicyDao.getComplexityItem();
	}

	/**
	 * 保存密码安全策略
	 * @param req
	 * @param data
	 */
	@Override
	public void saveSecPasswordPolicy(HttpServletRequest req, SecPasswordPolicyFormData data)
	{
		List<String> complexityItem = data.getSecPasswordComplexityItem();
		if (null != complexityItem && !complexityItem.isEmpty())
		{
			// 先重置密码复杂度
			secPasswordPolicyDao.resetSpSecPasswordComplexityItem();
			for (String name : complexityItem)
			{
				// 再根据设置更新密码复杂度
				secPasswordPolicyDao.updateSpSecPasswordComplexityItem(name);
			}
		}
		// 用系统初始化的ID去更新而不是新增
		SpSecPasswordPolicy querySpSecPasswordPolicy = secPasswordPolicyDao.getSpSecPasswordPolicy();
		data.getSecPasswordPolicy().setId(querySpSecPasswordPolicy.getId());
		secPasswordPolicyDao.saveSecPasswordPolicy(data.getSecPasswordPolicy());

		// 将密码安全策略内容存储在服务器内存中
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordValidity.name(),
				data.getSecPasswordPolicy().getPasswordValidity());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordLengthMin.name(),
				data.getSecPasswordPolicy().getPasswordLengthMin());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordLengthMax.name(),
				data.getSecPasswordPolicy().getPasswordLengthMax());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.maxLoginTimes.name(),
				data.getSecPasswordPolicy().getMaxLoginTimes());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.isModifyPasswordFirst.name(),
				data.getSecPasswordPolicy().getIsModifyPasswordFirst());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.ukeyEnable.name(),
				data.getSecPasswordPolicy().getUkeyEnable());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.isRepeatLogin.name(),
				data.getSecPasswordPolicy().getIsRepeatLogin());
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.sechostEnable.name(),
				data.getSecPasswordPolicy().getSechostEnable());

		// 操作日志描述
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		SpSecPasswordPolicy passwordPolicy = data.getSecPasswordPolicy();
		StringBuffer operaTion = new StringBuffer("更新密码安全策略，密码有效期:");
		switch (passwordPolicy.getPasswordValidity())
		{
			case "1":
				operaTion.append("一周");
				break;
			case "2":
				operaTion.append("一月");
				break;
			case "3":
				operaTion.append("一季度");
				break;
			default:
				operaTion.append("");
		}
		operaTion.append("，密码最小长度：").append(passwordPolicy.getPasswordLengthMin()).append("，密码最大长度：")
				.append(passwordPolicy.getPasswordLengthMax()).append("，最大尝试登录次数：")
				.append(passwordPolicy.getMaxLoginTimes()).append("，首次登陆是否修改密码：")
				.append(passwordPolicy.getIsModifyPasswordFirst().equals(Contants.YES) ? "是" : "否")
				.append("，启用UKey登陆 ：").append(passwordPolicy.getUkeyEnable().equals(Contants.YES) ? "是" : "否")
				.append("，是否可以重复登录：").append(passwordPolicy.getIsRepeatLogin().equals(Contants.YES) ? "是" : "否")
				.append("，是否可以启动信任主机：").append(passwordPolicy.getSechostEnable().equals(Contants.YES) ? "是" : "否");
		operateLogInfo.setOperation(operaTion.toString());
		systemSrv.saveOperateLog(operateLogInfo);

	}

	/**
	 * 验证密码有效性
	 * @param password
	 * @param userName 禁止用户口令与用户名相同或包含用户名
	 * @return String
	 */
	@Override
	public String passwordValid(String password, String userName)
	{
		StringBuilder errors = new StringBuilder();
		if (StringUtils.isEmpty(password))
		{
			throw new TMCException(RspCode.OBJECE_NOT_EXIST);
		}

		// 解密后密码
		password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, password));
		// 查看该用户的密码是否和姓名相同
		List<SpAdmins> userByName = adminDao.getUserByName(userName);
		// 获取密码策略和复杂度
		SpSecPasswordPolicy secPasswordPolicy = getSpSecPasswordPolicy();
		// 获取密码复杂度
		List<SpSecPasswordComplexityItem> secPasswordComplexityItem = getComplexityItem();
		// 允许密码最小位数
		String passwordLengthMin = secPasswordPolicy.getPasswordLengthMin();
		// 允许密码最大位数
		String passwordLengthMax = secPasswordPolicy.getPasswordLengthMax();
		if (StringUtils.isEmpty(userName))
		{
			// 新建用户时可能用户先输入密码未输入用户名而获取不到name无法校验
			errors.append("请先输入用户名");
		}
		else if (password.length() < Integer.parseInt(passwordLengthMin)
				|| password.length() > Integer.parseInt(passwordLengthMax))
		{
			errors.append("密码长度为" + passwordLengthMin + "-" + passwordLengthMax + "之间");
		}
		else if (password.equals(userName) || password.contains(userName))
		{
			// 禁止用户口令与用户名相同或包含用户名；
			errors.append("禁止用户口令与用户名相同或包含用户名");
		}
		else if (StringUtils.isNotEmpty(password) && userByName != null && userByName.size() != 0
				&& password.equals(new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, userByName.get(0).getPassword()))))
		{
			// 禁止用户口令与用户名相同或包含用户名；
			errors.append("修改后的密码不能与上次密码相同！");
		}
		else
		{
			for (SpSecPasswordComplexityItem spSecPasswordComplexityItem : secPasswordComplexityItem)
			{
				if ("1".equals(spSecPasswordComplexityItem.getIsEnable()))
				{
					Pattern pattern = Pattern.compile(spSecPasswordComplexityItem.getValue());
					Matcher matcher = pattern.matcher(password);
					if (!matcher.find())
					{
						if (errors.length() == 0)
						{
							errors.append(spSecPasswordComplexityItem.getDescription());
						}
						else
						{
							errors.append("、").append(spSecPasswordComplexityItem.getDescription());
						}
					}
				}
			}
			if (errors.length() > 0)
			{
				errors.insert(0, "密码必须包含");
			}
		}
		return errors.toString();
	}

	/**
	 * 验证用户失效日期
	 * @param time 日期
	 * @param expirationDateOffset 日期间隔
	 * @return
	 */
	@Override
	public CodeRsp dateValid(String time, String expirationDateOffset)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		if (StringUtils.isNotEmpty(expirationDateOffset))
		{
			long timeValue = DateUtil.strToDateTime(time).getTime();
			String currentDateStr = DateUtil.dateToString(DateUtil.getNowTime(), DateUtil.DATE_FORMAT_PATTERN);
			Date currentDate = DateUtil.strToDate(currentDateStr);
			long currentTimeMillis = currentDate.getTime();
			int offsetIntValue = Integer.parseInt(expirationDateOffset);

			if (timeValue - currentTimeMillis < offsetIntValue * DAY)
			{
				codeRsp = new CodeRsp(RspCode.USER_EXPIRATION_DATE_VALID);
				String msg = String.format(codeRsp.getMsg(), expirationDateOffset);
				codeRsp.setMsg(msg);
			}
		}
		return codeRsp;
	}
}
