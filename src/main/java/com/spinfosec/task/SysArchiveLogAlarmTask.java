package com.spinfosec.task;

import com.spinfosec.dao.AdminDao;
import com.spinfosec.dao.SysOperateLogDao;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.utils.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SysArchiveLogAlarmTask
 * @Description: 〈系统日志容量告警监控〉
 * @date 2019/1/23
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Component
@EnableScheduling
public class SysArchiveLogAlarmTask
{
	private Logger logger = LoggerFactory.getLogger(SysArchiveLogAlarmTask.class);

	@Autowired
	private ApplicationProperty property;

	@Autowired
	private SysOperateLogDao sysOperateLogDao;

	@Autowired
	private AdminDao adminDao;

	public void checkCount()
	{
		logger.info("系统操作日志审计告警定时任务执行...");
		int archiveSyslogAlarmLimit = StringUtils.isNotEmpty(property.getArchiveSyslogAlarmLimit())
				? Integer.valueOf(property.getArchiveSyslogAlarmLimit())
				: 100000;
		Long count = sysOperateLogDao.countSystemOperateLog();
		if (count.intValue() > archiveSyslogAlarmLimit)
		{
			List<SpAdmins> admins = adminDao.findUserByName("adtadmin");
			if (null != admins && !admins.isEmpty())
			{
				SpAdmins adtadmin = admins.get(0);
				String email = adtadmin.getEmail();
				String fullName = adtadmin.getName();
				String subject = "加密文件检查系统审计告警";
				String content = "hi " + fullName + ",<br/>" + "加密文件检查系统审计告警：系统审计记录（系统操作日志）容量达到" + count.intValue()
						+ "条，上限值为：" + archiveSyslogAlarmLimit + "条，超出" + (count.intValue() - archiveSyslogAlarmLimit)
						+ "条，请及时清理！";
				logger.info("发送邮件内容：" + content);
				try
				{
					EmailUtil.sendHtmlMail(subject, content, null, email);
				}
				catch (Exception e)
				{
					logger.info("系统操作日志审计告警发送邮件时发生错误...", e);
					e.printStackTrace();
				}
			}
		}
	}
}
