package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.IConfigPropertiesSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName BasicSettingsController
 * @Description: 〈系统基本设置控制层〉
 * @date 2018/10/25
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/basicSetting")
public class BasicSettingController
{
	private static final Logger log = LoggerFactory.getLogger(BasicSettingController.class);

	@Autowired
	private IConfigPropertiesSrv configProperSrv;

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 根据名称查询系统配置信息
	 * @param req
	 * @param groupName  配置名称 BASE_SETTINGS 基本配置 EMAIL_SETTINGS 邮箱配置 AUTHORSETTINGS Exchage认证信息
	 * @return
	 */
	@RequestMapping(value = "/getSettingByGroupName/{groupName}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getSettingByGroupName(HttpServletRequest req, @PathVariable String groupName)
	{
		List<SpConfigProperties> settingByGroupName = new ArrayList<>();
		if (groupName.equalsIgnoreCase(Contants.BASE_SETTINGS))
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONObject time = new JSONObject();
			time.put("time", format.format(new Date()));
			settingByGroupName = configProperSrv.getSettingByGroupName(groupName);
			for (SpConfigProperties spConfigProperties : settingByGroupName)
			{
				if (spConfigProperties.getName().equals("NTP_SERVER"))
				{
					time.put("NTP_SERVER", spConfigProperties.getValue());
				}
				else if (spConfigProperties.getName().equals("INTERVAL"))
				{
					time.put("INTERVAL", spConfigProperties.getValue());
				}
				else if (spConfigProperties.getName().equals("TIMECONTROL"))
				{
					time.put("TIMECONTROL", spConfigProperties.getValue());
				}
			}
			return ResultUtil.getSuccessResult(time);

		}
		else
		{
			// 查询不再返回密码
			settingByGroupName = configProperSrv.getSettingByGroupName(groupName);
			for (SpConfigProperties configProperties : settingByGroupName)
			{
				if (configProperties.getName().equalsIgnoreCase("PASSWORD"))
				{
					settingByGroupName.remove(configProperties);
					return ResultUtil.getSuccessResult(settingByGroupName);
				}
			}
		}
		return ResultUtil.getSuccessResult(settingByGroupName);
	}

	/**
	 * 保存系统配置信息
	 * @param req
	 * @param propesArrs
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveSettingByGroupName", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveSettingByGroupName(HttpServletRequest req,
			@RequestBody List<SpConfigProperties> propesArrs) throws Exception
	{
		// 保存Exchange认证信息操作日志
		if (propesArrs.get(0).getGroupName().equalsIgnoreCase("AUTHORSETTINGS"))
		{
			SpSystemOperateLogInfo sysLog = OperateLogUtil.packageSysLog(req);
			StringBuffer operaTion = new StringBuffer("修改认证设置 认证名称:");
			for (SpConfigProperties properties : propesArrs)
			{
				if (properties.getName().equalsIgnoreCase("SERVER"))
				{
					operaTion.append(properties.getValue() + "，");
				}
				else if (properties.getName().equalsIgnoreCase("ADDRESS"))
				{
					operaTion.append("IP:" + properties.getValue() + "，");
				}
				else if (properties.getName().equalsIgnoreCase("PORT"))
				{
					operaTion.append("端口:" + properties.getValue() + "，");
				}
				else if (properties.getName().equalsIgnoreCase("USERNAME"))
				{
					operaTion.append("用户名:" + properties.getValue() + "，");
				}
				else if (properties.getName().equalsIgnoreCase("CONTEXT"))
				{
					operaTion.append("搜索范围:" + properties.getValue());
				}
			}

			sysLog.setOperation(operaTion.toString());
			systemSrv.saveOperateLog(sysLog);

		}
		configProperSrv.saveConfigProperties(propesArrs);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 基本设置-修改系统时间
	 * @param req
	 * @param data 修改时间数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateSysTime", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateSysTime(HttpServletRequest req, @RequestBody String data) throws Exception
	{
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		RspCode rspCode = configProperSrv.saveBaseTimeSetting(data, operateLogInfo);
		return ResultUtil.getDefinedCodeResult(new CodeRsp(rspCode));
	}

	/**
	 * 获取系统网络设置信息
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSysNetSetting", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getSysNetSetting(HttpServletRequest req)
	{
		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();
		json.put("type", "1");
		json.put("content", json2);
		String reply = MQUtil.sendMessage(connectionFactory, Contants.GET_SYSTEMTIME, json.toJSONString());
		JSONObject jsonForReply = JSONObject.parseObject(reply);
		JSONArray content = JSONArray.parseArray(jsonForReply.get("content").toString());
		return ResultUtil.getSuccessResult(content);
	}

	/**
	 * 更新系统网络设置信息
	 * @param req
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateSysNetSetting", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateSysNetSetting(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody String data)
			throws Exception
	{
		// 记录日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		operateLogInfo.setOperation("基本设置-系统网络设置，设置信息："
				+ OperateLogUtil.getZhDes(JSONArray.parseArray(data).getJSONObject(0).toJSONString()));
		systemSrv.saveOperateLog(operateLogInfo);
		try
		{
			JSONObject json = new JSONObject();
			json.put("type", "2");
			JSONArray objects = JSONArray.parseArray(data);
			String mask = objects.getJSONObject(0).getString("mask");
			mask = convertToMask(mask);
			objects.getJSONObject(0).put("mask", mask);
			json.put("content", objects);
			String reply = MQUtil.sendMessage(connectionFactory, Contants.GET_SYSTEMTIME, json.toJSONString());
			JSONObject jsonForReply = JSONObject.parseObject(reply);
			if (0 == jsonForReply.getInteger("retcode"))
			{
				req.getSession().invalidate();
				return ResultUtil.getSuccessResult();
			}
			else
			{
				log.info("更新系统网络设置信息失败：");
				CodeRsp codeRsp = new CodeRsp(RspCode.getRspCodeByCode(jsonForReply.getString("retcode")));
				return ResultUtil.getFailResult(codeRsp);
			}
		}
		catch (Exception e)
		{
			throw new TMCException(RspCode.MQ_CONNECTION_ERROR);
		}
	}

	/**
	 * 掩码位数转换为子网掩码
	 * @param part3 子网掩码或掩码位数
	 * @return 子网掩码
	 */
	private String convertToMask(String part3)
	{
		String part2 = "";
		if (part3.length() < 3)
		{
			int tmpMask[] = { 0, 0, 0, 0 };
			int times = Integer.parseInt(part3) / 8;
			int i = 0;
			if (4 == times)
			{
				return "255.255.255.255";
			}
			for (; i < times; i++)
			{
				tmpMask[i] = 255;
			}

			for (int j = 1; j <= 8; j++)
			{
				if (j <= Integer.parseInt(part3) - times * 8)
				{
					tmpMask[i] = 2 * tmpMask[i] + 1;
				}
				else
				{
					tmpMask[i] = 2 * tmpMask[i];
				}
			}
			part2 = Integer.toString(tmpMask[0]) + "." + Integer.toString(tmpMask[1]) + "."
					+ Integer.toString(tmpMask[2]) + "." + Integer.toString(tmpMask[3]);
		}
		else
		{
			part2 = part3;
		}
		return part2;
	}

}
