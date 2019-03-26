package com.spinfosec.utils;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @title OperateLogUtil
 * @description TODO(转换操作日志详细描述)
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author ZHANGPF
 * @version V1.5.0.1
 * @create 2015年6月8日 下午3:31:10   
 */
public class OperateLogUtil
{

	/**
	 * 系统操作日志对象封装
	 * @param request
	 * @return
	 */
	public static SpSystemOperateLogInfo packageSysLog(HttpServletRequest request)
	{
		SpSystemOperateLogInfo operateLogInfo = new SpSystemOperateLogInfo();
		operateLogInfo.setDiscriminator("discriminator");
		operateLogInfo.setIsLeaderForTx(1);
		operateLogInfo.setTransactionId("transactionId");
		operateLogInfo.setGenerationTimeTs(new Date());
		operateLogInfo.setAdminId((null != request.getSession().getAttribute(SessionItem.userId.name()))
				? request.getSession().getAttribute(SessionItem.userId.name()).toString()
				: "");
		operateLogInfo.setAdminName((null != request.getSession().getAttribute(SessionItem.userName.name()))
				? request.getSession().getAttribute(SessionItem.userName.name()).toString()
				: "");
		operateLogInfo.setRoleId((null != request.getSession().getAttribute(SessionItem.roleId.name()))
				? request.getSession().getAttribute(SessionItem.roleId.name()).toString()
				: "");
		operateLogInfo.setRoleName((null != request.getSession().getAttribute(SessionItem.roleName.name()))
				? request.getSession().getAttribute(SessionItem.roleName.name()).toString()
				: "");
		return operateLogInfo;
	}

	private static Logger log = LoggerFactory.getLogger(OperateLogUtil.class);

	/**
	 * @throws ClassNotFoundException 
	 * @Title: getOpeLogInfo
	 * @Description: TODO(转换操作日志详细描述)
	 * @param: @param arguments
	 * @param: @param opDefinition
	 * @param: @return
	 * @param: @throws OgnlException   
	 * @return: String   
	 * @throws
	 */
	public static String getOpeLogInfo(Object[] arguments, String opDefinition)
			throws OgnlException, ClassNotFoundException
	{
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Pattern subPattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(opDefinition);
		List<String> params = new ArrayList<String>();
		int expIndex = 0;
		while (matcher.find())
		{
			String subExp = matcher.group(1);

			int index = -1;
			Object expr = null;
			int i = 0;
			Matcher subMatcher = subPattern.matcher(subExp);
			while (subMatcher.find())
			{
				if (i == 0)
				{
					index = Integer.valueOf(subMatcher.group(1));
				}
				else
				{
					expr = Ognl.parseExpression(subMatcher.group(1));
				}
				i++;
			}
			Object value = null;
			// 如果expr不为空则匹配
			if (null != expr)
			{
				// 如果参数为一个数组
				if (null != arguments[index] && arguments[index].getClass().isArray())
				{
					Object[] objs = (Object[]) arguments[index];

					for (Object object : objs)
					{
						if (null != value && StringUtils.isNotEmpty(value.toString()))
						{
							value = value.toString() + "，" + Ognl.getValue(expr, object);
						}
						else
						{
							value = Ognl.getValue(expr, object);
						}
					}
				}
				else
				{
					value = Ognl.getValue(expr, arguments[index]);
					if (null != value)
					{
						if (value.getClass().equals(Date.class) || value.getClass().equals(Timestamp.class))
						{
							value = DateUtil.dateToString((Date) value, DateUtil.DATETIME_FORMAT_PATTERN);
						}
					}
				}
			}
			else
			{
				value = arguments[index];
				if (null != value && value.getClass().equals(Date.class))
				{
					value = DateUtil.dateToString((Date) value, DateUtil.DATETIME_FORMAT_PATTERN);
				}
			}

			params.add(null != value ? value.toString() : "");

			opDefinition = opDefinition.replace(matcher.group(1), expIndex + "");
			expIndex++;
		}

		log.info("opDefinition : " + MessageFormat.format(opDefinition, params.toArray()));

		return MessageFormat.format(opDefinition, params.toArray());
	}

	/**
	 * @Title getSystemOpDes
	 * @Description TODO(系统设置相关操作，生成操作描述)
	 * @param propesArrs - 设置参数
	 * @return
	 * @return String - 操作描述
	 */
	public static String getSystemOpDes(SpConfigProperties[] propesArrs)
	{
		StringBuffer resultDes = new StringBuffer();
		if (null != propesArrs && propesArrs.length > 0)
		{
			if ("AUTHORSETTINGS".equalsIgnoreCase(propesArrs[0].getGroupName()))
			{
				resultDes.append("修改认证设置 ");
			}
			else if ("EMAIL_SETTINGS".equalsIgnoreCase(propesArrs[0].getGroupName()))
			{
				resultDes.append("修改基本设置-邮件服务器配置 ");
			}
			else if ("BASE_SETTINGS".equalsIgnoreCase(propesArrs[0].getGroupName()))
			{
				resultDes.append("修改基本设置-时间设置 ");
			}
			else if ("SYS_NET_SETTINGS".equalsIgnoreCase(propesArrs[0].getGroupName()))
			{
				resultDes.append("修改基本设置-系统网络设置 ");
			}
			else if ("Quarantine_Area".equalsIgnoreCase(propesArrs[0].getGroupName()))
			{
				resultDes.append("修改隔离区配置 ");
			}
			else if ("LOGSERVERSETTING".equalsIgnoreCase(propesArrs[0].getGroupName()))
			{
				resultDes.append("修改日志服务器配置 ");
			}

			for (SpConfigProperties spConfigProperties : propesArrs)
			{
				if (StringUtils.isNotEmpty(getCnName(spConfigProperties.getName()))
						&& StringUtils.isNotEmpty(spConfigProperties.getValue()))
				{
					// "1".equals(spConfigProperties.getValue()) ? "启用" : ("0".equals(spConfigProperties.getValue()) ?
					// "不启用" : spConfigProperties.getValue())
					resultDes.append(" ").append(getCnName(spConfigProperties.getName())).append(" : ")
							.append(spConfigProperties.getValue());
				}
			}
		}

		return resultDes.toString();
	}

	/**
	 * @Title getCnName
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @param enName -设置英文字段名称
	 * @return
	 * @return String  中文名称
	 */
	private static String getCnName(String enName)
	{
		String cnName = "";
		switch (enName)
		{
			case "SERVER":
				cnName = "认证名称";
				break;
			case "ADDRESS":
				cnName = "IP ";
				break;
			case "PORT":
				cnName = "端口 ";
				break;
			case "USERNAME":
				cnName = "用户名 ";
				break;
			case "CONTEXT":
				cnName = "搜索范围 ";
				break;
			case "SMTP":
				cnName = "服务器地址 ";
				break;
			case "EMAIL":
				cnName = "邮箱地址 ";
				break;
			case "ntp_server":
				cnName = "时间服务器 ";
				break;
			case "interval":
				cnName = "同步周期(秒) ";
				break;
			case "datebox":
				cnName = "日期 ";
				break;
			case "ethN":
				cnName = "网卡 ";
				break;
			case "IP":
				cnName = "IP ";
				break;
			case "NETMASK":
				cnName = "子网掩码 ";
				break;
			case "GW":
				cnName = "网关 ";
				break;
			case "DNS1":
				cnName = "DNS1 ";
				break;
			case "DNS2":
				cnName = "DNS2 ";
				break;
			case "quarantine_type":
				cnName = "路径类别 ";
				break;
			case "quarantine_path":
				cnName = "隔离区路径 ";
				break;
			case "quarantine_user":
				cnName = "用户名 ";
				break;
			case "quarantine_domin":
				cnName = "域 ";
				break;
			case "quarantine_max_size":
				cnName = "最大容量(MB) ";
				break;
			case "quarantine_cur_size":
				cnName = "当前容量(MB) ";
				break;
			case "quarantine_threshold":
				cnName = "阈值(%) ";
				break;
			case "sysLog_server_isEnable":
				cnName = "是否传输 ";
				break;
			case "sysLog_server_adress":
				cnName = "服务器地址 ";
				break;
			case "sysLog_server_port":
				cnName = "服务器端口 ";
				break;
			case "sysLog_server_priority":
				cnName = "优先级 ";
				break;
			case "sysLog_server_isOperateLog":
				cnName = "操作日志 ";
				break;
			case "sysLog_server_isEventLog":
				cnName = "安全事件 ";
				break;
			case "sysLog_server_isSysAlarm":
				cnName = "系统告警 ";
				break;
			case "sysLog_server_isBusiAlarm":
				cnName = "业务告警 ";
				break;
			default:
				cnName = "";
				break;
		}

		return cnName;
	}

	/**
	 * @Title getZhDes
	 * @Description TODO(根据英文描述，将其中英文部分转换为中文)
	 * @param enDes - 英文描述
	 * @return
	 * @return String - 中文描述
	 */
	public static String getZhDes(String enDes)
	{
		String zhDes = enDes.replaceAll("dns", "DNS").replaceAll("gw", "网关").replaceAll("index", "网卡下标")
				.replaceAll("nic", "网卡").replaceAll("status", "设置状态").replaceAll("mask", "子网掩码")
				.replaceAll("ip", "IP地址").replaceAll("time", "时间").replaceAll("NTP_SERVER", "时间服务器")
				.replaceAll("INTERVAL", "周期").replaceAll("TRUE", "是").replaceAll("FALSE", "否")
				.replaceAll("TIMECONTROL", "设置方式").replaceAll("CUSTOM", "手动设置时间").replaceAll("NTP", "NTP服务器同步时间");

		return zhDes;
	}

	public static void main(String[] args)
	{
		for (int i = 0; i < 2; i++)
		{
			System.out.println(GenUtil.getUUID());
		}
	}

}
