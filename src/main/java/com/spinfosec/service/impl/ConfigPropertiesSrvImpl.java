package com.spinfosec.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dao.entity.SpServerStatus;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dao.tactic.BasicSettingDao;
import com.spinfosec.service.srv.IConfigPropertiesSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.MQUtil;
import com.spinfosec.utils.OperateLogUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName BasicSettingSrvImpl
 * @Description: 〈基本设置业务实现类〉
 * @date 2018/10/25
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("configProperSrv")
public class ConfigPropertiesSrvImpl implements IConfigPropertiesSrv
{
	private static final Logger log = LoggerFactory.getLogger(ConfigPropertiesSrvImpl.class);

	@Autowired
	private BasicSettingDao basicSettingDao;

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 根据基本设置类型名获取设置参数
	 * @param groupName
	 * @return
	 */
	@Override
	public List<SpConfigProperties> getSettingByGroupName(String groupName)
	{
		return basicSettingDao.getConfigByGroupName(groupName);
	}

	/**
	 * 保存配置参数
	 * @param configPropertiesList
	 */
	@Override
	public void saveConfigProperties(List<SpConfigProperties> configPropertiesList) throws Exception
	{
		for (SpConfigProperties configProperties : configPropertiesList)
		{
			basicSettingDao.updateConfigProperties(configProperties);
		}

		/**
		 * 邮箱将设置存贮到缓存中
		 */
		if (configPropertiesList.get(0).getGroupName().equalsIgnoreCase("EMAIL_SETTINGS"))
		{
			// 保存邮箱设置
			for (SpConfigProperties spConfigProperties : configPropertiesList)
			{
				MemInfo.getEmailInfo().put(spConfigProperties.getName(), spConfigProperties.getValue());
			}
		}

	}

	/**
	 * 修改系统时间
	 * @param data
	 * @return
	 */
	@Override
	public RspCode saveBaseTimeSetting(String data, SpSystemOperateLogInfo operateLogInfo)
	{

		List<SpConfigProperties> settingByGroupName = getSettingByGroupName(Contants.BASE_SETTINGS);
		JSONObject con = null;
		try
		{
			con = JSONObject.parseObject(data);
		}
		catch (Exception e)
		{
			return RspCode.PARAMERTER_ERROR;
		}
		try
		{
			if (null != con)
			{
				operateLogInfo.setOperation("基本设置-时间设置，设置信息：" + OperateLogUtil.getZhDes(data));
				systemSrv.saveOperateLog(operateLogInfo);
				// 手动设置时间
				if (StringUtils.isNotEmpty(con.getString("TIMECONTROL"))
						&& "CUSTOM".equalsIgnoreCase(con.getString("TIMECONTROL")))
				{
					// 设置系统时间
					String time = con.getString("time");
					String reply = MQUtil.sendMessage(connectionFactory, Contants.GET_SYSTEMTIME,
							"{\"content\": {\"tzname\": \"China Standard Time\", \"time\": " + "\"" + time
									+ "\"}, \"type\": \"4\"}",
							60000);
					JSONObject jsonForReply = JSONObject.parseObject(reply);
					// 设置成功更新数据库中的配置数据信息
					if (0 == jsonForReply.getInteger("retcode"))
					{
						String timecontrol = con.getString("TIMECONTROL");
						for (SpConfigProperties config : settingByGroupName)
						{
							if (config.getName().equalsIgnoreCase("TIMECONTROL"))
							{
								config.setValue(timecontrol);
								basicSettingDao.updateConfigProperties(config);
							}
						}
						return RspCode.SUCCESS;
					}
				}
				// 设置NTP服务器
				else if (StringUtils.isNotEmpty(con.getString("TIMECONTROL"))
						&& "NTP".equalsIgnoreCase(con.getString("TIMECONTROL")))
				{

					String reply = MQUtil.sendMessage(connectionFactory, Contants.GET_SYSTEMTIME,
							"{\"content\": {\"ip\": \"" + con.getString("NTP_SERVER") + "\", \"interval\": \""
									+ con.getString("INTERVAL") + "\"}, \"type\": \"6\"}",
							60000);
					if (StringUtils.isEmpty(reply))
					{
						log.debug("设置NTP服务器失败：后台返回信息为空！");
						return RspCode.FAILURE;
					}

					JSONObject jsonForReply = JSONObject.parseObject(reply);
					// 更新数据库
					try
					{
						if (0 == jsonForReply.getInteger("retcode"))
						{
							for (SpConfigProperties config : settingByGroupName)
							{
								if (config.getName().equalsIgnoreCase("NTP_SERVER"))
								{
									config.setValue(con.getString("NTP_SERVER"));
									basicSettingDao.updateConfigProperties(config);
								}
								else if (config.getName().equalsIgnoreCase("INTERVAL"))
								{
									config.setValue(con.getString("INTERVAL"));
									basicSettingDao.updateConfigProperties(config);
								}
								else if (config.getName().equalsIgnoreCase("TIMECONTROL"))
								{
									config.setValue(con.getString("TIMECONTROL"));
									basicSettingDao.updateConfigProperties(config);
								}
							}
							return RspCode.SUCCESS;
						}
						else
						{
							return RspCode.getRspCodeByCode(jsonForReply.getInteger("retcode").toString());
						}
					}
					catch (Exception e)
					{
						return RspCode.FAILURE;
					}
				}
			}
			else
			{
				return RspCode.PARAMERTER_ERROR;
			}
		}
		catch (Exception e)
		{
			return RspCode.FAILURE;
		}

		return null;
	}

	/**
	 * 获取服务器所在系统的CPU、内存、磁盘空间等信息
	 * @return
	 */
	@Override
	public SpServerStatus getServerStatus()
	{
		return basicSettingDao.getServerStatus();
	}
}
