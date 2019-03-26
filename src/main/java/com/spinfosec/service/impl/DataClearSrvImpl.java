package com.spinfosec.service.impl;

import com.spinfosec.dao.DataClearDao;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dao.entity.SpTask;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.service.srv.IDataClearSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DataClearSrvImpl
 * @Description: 〈数据清理〉
 * @date 2019/1/15
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("dataClearSrv")
public class DataClearSrvImpl implements IDataClearSrv
{

	private Logger logger = LoggerFactory.getLogger(DataClearSrvImpl.class);

	@Autowired
	private DataClearDao dataClearDao;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 业务数据
	 */
	private static final String DATA_TYPE_BUSINESS = "DATA_BUSINESS";

	/**
	 * 事件数据
	 */
	private static final String DATA_TYPE_EVENT = "DATA_EVENT";

	/**
	 * 操作日志
	 */
	private static final String DATA_TYPE_OPERATE_LOG = "DATA_OPERATE_LOG";

	/**
	 *系统设置数据
	 */
	private static final String DATA_TYPE_SETTING = "DATA_SETTING";

	/**
	 * 备份数据
	 */
	private static final String DATA_TYPE_BACKUP_RECOVER = "DATA_BACKUP_RECOVER";

	/**
	 * 用户和角色
	 */
	private static final String DATA_TYPE_USER_ROLE = "DATA_USER_ROLE";

	/**
	 * 其他
	 */
	private static final String DATA_TYPE_OTHER = "DATA_OTHER";

	/**
	 * 判断是否存在运行中的任务
	 * @return
	 */
	@Override
	public boolean isExistRunningTask()
	{

		boolean exist = false;
		List<SpTask> runningTasks = dataClearDao.getRunningTasks();
		if (null != runningTasks && runningTasks.size() > 0)
		{
			exist = true;
		}
		return exist;
	}

	/**
	 * 启动守护线程
	 */
	@Override
	public void startCrond()
	{
		try
		{
			String[] cmds = { "/bin/sh", "-c", "service crond start" };
			Process process = Runtime.getRuntime().exec(cmds);
			process.waitFor();
			BufferedReader normalReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = normalReader.readLine()) != null)
			{
				logger.info(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean startScanEngine()
	{
		boolean isStart = false;
		try
		{
			String[] cmds = { "/bin/sh", "-c", "service ScanEngine start" };
			Process process = Runtime.getRuntime().exec(cmds);
			process.waitFor();
			BufferedReader normalReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = normalReader.readLine()) != null)
			{
				logger.info(line);
				if (line.contains("ScanEngine is running"))
				{
					isStart = true;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return isStart;
	}

	@Override
	public void stopCrond()
	{
		try
		{
			String[] cmds = { "/bin/sh", "-c", "service crond stop" };
			Process process = Runtime.getRuntime().exec(cmds);
			process.waitFor();
			BufferedReader normalReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = normalReader.readLine()) != null)
			{
				logger.info(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 停止SE
	 * @return
	 */
	@Override
	public boolean stopScanEngine()
	{
		boolean isStop = false;
		try
		{
			String[] cmds = { "/bin/sh", "-c", "service ScanEngine stop" };
			Process process = Runtime.getRuntime().exec(cmds);
			process.waitFor();
			BufferedReader normalReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = normalReader.readLine()) != null)
			{
				logger.info(line);
				if (line.contains("ScanEngine is stoped"))
				{
					isStop = true;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return isStop;
	}

	/**
	 * 清理数据
	 * @param dataArr 需要清理的数据类型
	 */
	@Override
	public void clearData(String[] dataArr, SpSystemOperateLogInfo systemOperateLogInfo)
	{
		if (null != dataArr && dataArr.length > 0)
		{
			StringBuilder operateMsg = new StringBuilder("数据清理，清理内容如下：");
			for (String dataType : dataArr)
			{
				if (DATA_TYPE_BUSINESS.equalsIgnoreCase(dataType))
				{
					// 业务数据（包括策略，任务中心数据的清理）
					clearBusinessData();
					operateMsg.append("业务数据，");
				}
				else if (DATA_TYPE_EVENT.equalsIgnoreCase(dataType))
				{
					// 事件数据（包括事件，安全日志归档数据的清理）
					clearEventData();
					operateMsg.append("事件数据，");
				}
				else if (DATA_TYPE_OPERATE_LOG.equalsIgnoreCase(dataType))
				{
					// 操作日志（清理用户的操作日志，操作日志归档信息）
					clearOperateLogData();
					operateMsg.append("操作日志，");
				}
				else if (DATA_TYPE_SETTING.equalsIgnoreCase(dataType))
				{
					// 系统设置数据（系统模块下认证设置，基本设置，密码安全策略，Lotus配置的数据的清理和重置）
					clearSettingData();
					operateMsg.append("系统设置数据，");
				}
				else if (DATA_TYPE_BACKUP_RECOVER.equalsIgnoreCase(dataType))
				{
					// 备份数据（清理数据备份恢复的数据）
					clearDataBackuoRecoverData();
					operateMsg.append("备份数据，");
				}
				else if (DATA_TYPE_USER_ROLE.equalsIgnoreCase(dataType))
				{
					// 用户角色数据（包括个人中心的密码重置，用户管理，角色管理，信任主机管理的数据。）
					clearUserRoleData();
					operateMsg.append("用户和角色数据，");
				}
				else if (DATA_TYPE_OTHER.equalsIgnoreCase(dataType))
				{
					// 其他数据（包括升级管理的数据）
					clearOtherData();
					operateMsg.append("其他数据，");
				}
			}
			systemOperateLogInfo
					.setOperation(operateMsg.replace(operateMsg.length() - 1, operateMsg.length(), "。").toString());
			systemSrv.saveOperateLog(systemOperateLogInfo);
		}
	}

	/**
	 * 清理业务数据
	 */
	private void clearBusinessData()
	{
		// 1.清理数据库数据
		logger.info("开始清理任务中心数据");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_task");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_task_files");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_task_sipped");

		logger.info("开始清理策略数据");
		// 清空策略任务主表
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_discovery_tasks");
		// 清空扫描任务排除文件类型列表
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_dscvr_excld_file_types");
		// 清空扫描任务包含文件类型列表
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_dscvr_incld_file_types");
		// 清空加密算法类型匹配
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_algorithm_file_type");
		// 清空策略时间调度数据
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_scheduling_data");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_scheduling_day_tf");
		// 清空主机资源内容
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_target_res");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_target_res_detail");
		// 清空数据库高级追击内容
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_column_resinfo");

		// 2.清理文件
		logger.info("开始清理DiscoveryJobs数据");
		StringBuilder discoveryJobsPath = new StringBuilder();
		discoveryJobsPath.append(System.getenv("SP_HOME")).append(File.separator).append("DDMS").append(File.separator)
				.append("DiscoveryJobs");
		FileUtils.clearDirectory(discoveryJobsPath.toString());

		logger.info("开始清理密钥数据");
		StringBuilder keyPath = new StringBuilder();
		keyPath.append(MemInfo.getServletContext().getRealPath("/")).append("document").append(File.separator)
				.append("sftpKey").append(File.separator);
		File keyDir = new File(keyPath.toString());
		org.apache.commons.io.FileUtils.deleteQuietly(keyDir);

	}

	/**
	 * 清理事件数据
	 */
	private void clearEventData()
	{
		logger.info("开始清理事件数据");
		// 清空事件数据
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_dscvr_files");

		logger.info("开始清理安全日志归档中事件数据");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_event_archive_log");

		// 删除日志归档数据
		String logPath = MemInfo.getServletContextPath() + "logArchive" + File.separator;
		File logPathDir = new File(logPath.toString());
		File[] files = logPathDir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{

				if (name.startsWith("safe_event_log"))
				{
					return true;
				}
				return false;
			}
		});
		if (null != files)
		{
			for (File file : files)
			{
				org.apache.commons.io.FileUtils.deleteQuietly(file);
			}
		}

	}

	/**
	 * 清理操作日志数据
	 */
	private void clearOperateLogData()
	{
		logger.info("开始清理操作日志的数据");
		// 清理操作日志
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_system_operate_log_info");
		// 清理操作日志归档数据
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_system_operate_archive_log");

		// 清理系统日志归档数据
		String logPath = MemInfo.getServletContextPath() + "logArchive" + File.separator;
		File logPathDir = new File(logPath.toString());
		File[] files = logPathDir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				if (name.startsWith("system_operate_log"))
				{
					return true;
				}
				return false;
			}
		});
		if (null != files)
		{
			for (File file : files)
			{
				org.apache.commons.io.FileUtils.deleteQuietly(file);
			}
		}

	}

	/**
	 * 清理系统设置数据
	 */
	private void clearSettingData()
	{
		logger.info("清理设置数据！");
		// 重置系统设置数据（包括系统模块下认证设置,基本设置,密码安全策略，Lotus配置数据的清理和重置）
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_config_properties");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('54', 'USE_SSL', 'EMAIL_SETTINGS', 'false', NULL, '1', NULL, NULL, '18')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('55', 'SMTP', 'EMAIL_SETTINGS', '127.0.0.1', NULL, '1', NULL, NULL, '20')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('56', 'PORT', 'EMAIL_SETTINGS', '25', NULL, '1', NULL, NULL, '20')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('57', 'EMAIL', 'EMAIL_SETTINGS', 'test@spinfosec.com', NULL, '1', NULL, NULL, '20')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('58', 'PASSWORD', 'EMAIL_SETTINGS', 'Spinfo0', NULL, '1', NULL, NULL, '20')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('19', 'SERVER', 'AUTHORSETTINGS', 'server', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('20', 'ADDRESS', 'AUTHORSETTINGS', '127.0.0.1', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('21', 'PORT', 'AUTHORSETTINGS', '389', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('22', 'USERNAME', 'AUTHORSETTINGS', 'administrator', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('23', 'PASSWORD', 'AUTHORSETTINGS', 'admin@123', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('24', 'CONTEXT', 'AUTHORSETTINGS', 'context', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('25', 'NESTED_GROUP_SEARCH', 'AUTHORSETTINGS', 'true', NULL, '1', NULL, NULL, '15')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('26', 'USE_SSL', 'AUTHORSETTINGS', 'false', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('27', 'USE_DEFAULT', 'AUTHORSETTINGS', 'true', NULL, '1', NULL, NULL, '19')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('311', 'NTP_SERVER', 'BASE_SETTINGS', '127.0.0.1', NULL, '1', NULL, NULL, '4')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('312', 'INTERVAL', 'BASE_SETTINGS', '3600', NULL, '1', NULL, NULL, '4')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_config_properties` (`ID`, `NAME`, `GROUP_NAME`, `VALUE`, `EXTRA_DATA`, `GROUP_ORDER`, `BUNDLE_KEY`, `DEFAULT_VALUE`, `OPTLOCK`) VALUES ('324', 'TIMECONTROL', 'BASE_SETTINGS', 'CUSTOM', NULL, '1', NULL, NULL, '0')");

		// 邮箱设置存放到缓存中
		MemInfo.getEmailInfo().put("USE_SSL", "false");
		MemInfo.getEmailInfo().put("SMTP", "127.0.0.1");
		MemInfo.getEmailInfo().put("PORT", "25");
		MemInfo.getEmailInfo().put("EMAIL", "test@spinfosec.com");
		MemInfo.getEmailInfo().put("PASSWORD", "Spinfo0");

		// 重置密码安全策略
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_sec_password_policy");
		// 重置密码复杂度
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_sec_password_complexity_item");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_sec_password_policy` (`ID`, `PASSWORD_VALIDITY`, `PASSWORD_LENGTH_MIN`, `PASSWORD_LENGTH_MAX`, `MAX_LOGIN_TIMES`, `IS_MODIFY_PASSWORD_FIRST`, `UKEY_ENABLE`, `IS_REPEAT_LOGIN`, `SECHOST_ENABLE`) VALUES ('1', '3', '6', '16', '3', '1', '0', '1', '0')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_sec_password_complexity_item` (`ID`, `NAME`, `DISPLAY_NAME`, `VALUE`, `IS_ENABLE`, `DESCRIPTION`) VALUES ('1', 'number', '包含数字', '\\\\d', '1', '数字')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_sec_password_complexity_item` (`ID`, `NAME`, `DISPLAY_NAME`, `VALUE`, `IS_ENABLE`, `DESCRIPTION`) VALUES ('2', 'letter', '包含字母', '[a-zA-Z]', '1', '字母')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_sec_password_complexity_item` (`ID`, `NAME`, `DISPLAY_NAME`, `VALUE`, `IS_ENABLE`, `DESCRIPTION`) VALUES ('3', 'matchCase', '包含大小写', '([a-z].*[A-Z])|([A-Z].*[a-z])', '0', '大小写')");
		dataClearDao.execNativeSql(
				"INSERT INTO `sp_sec_password_complexity_item` (`ID`, `NAME`, `DISPLAY_NAME`, `VALUE`, `IS_ENABLE`, `DESCRIPTION`) VALUES ('4', 'specialChar', '包含特殊字符', '[!@#$%^&()\\\\+\\\\-\\\\*/|\\\\\\\\\\\\[\\\\]{}._=]', '0', '特殊字符!@#$%^&()+-*/|\\\\[]{}._=');");

		// 修改时将初始化信息保存到内存中
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordValidity.name(), "3");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordLengthMin.name(), "6");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordLengthMax.name(), "15");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.maxLoginTimes.name(), "3");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.isModifyPasswordFirst.name(), "1");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.ukeyEnable.name(), "0");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.isRepeatLogin.name(), "1");
		MemInfo.getSecPasswordPolicyInfo().put(SessionItem.sechostEnable.name(), "0");

		// Lotus配置文件还原
		StringBuilder databakPath = new StringBuilder();
		databakPath.append(MemInfo.getServletContextPath()).append(File.separator).append("WEB-INF")
				.append(File.separator).append("databak").append(File.separator).append("lotus.xml");
		StringBuilder dstPath = new StringBuilder();
		dstPath.append(System.getenv("SP_HOME")).append(File.separator).append("DDMS").append(File.separator)
				.append("config").append(File.separator).append("lotus.xml");
		FileUtils.copyFile(databakPath.toString(), dstPath.toString());

	}

	/**
	 * 清理系统备份数据
	 */
	private void clearDataBackuoRecoverData()
	{
		logger.info("开始清理数据备份恢复的数据");
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_data_backups");
		// 删除系统备份数据文件
		String path = MemInfo.getServletContextPath() + "systemArchive";
		File dir = new File(path.toString());
		for (File file : dir.listFiles())
		{
			org.apache.commons.io.FileUtils.deleteQuietly(file);
		}

	}

	/**
	 * 清理用户角色数据
	 */
	private void clearUserRoleData()
	{
		logger.info("清理用户角色信息");
		// TODO 组织资源是否清理
		// 用户角色数据（包括个人中心的密码重置，用户管理，角色管理，信任主机管理的数据。）
		// 清理非预置用户数据
		dataClearDao.execNativeSql("DELETE FROM sp_admins WHERE DEFINITION_TYPE <> 'C_PRE_DEFINE'");
		dataClearDao.execNativeSql(
				"UPDATE sp_admins SET PASSWORD = '047801CF090866BBAA4CF96A60D5F6A3EFA25FD6C184DCA29169BAAB94859984757252948DF95A035B08CD925CFEA82176AEF3C0FE7DC11E2749B3FB8BD6379DCDF323BEE51BCFCC7718BEA81E529813A08D61E83DA95E18C26261AF2C159465D3F0815A8077CA8B8EAD55',PASSWORD_CHANGE_FLAG = '0',PASSWORD_MODIFY_DATE='2037-01-01 00:00:00',LAST_LOGIN_TIME='2037-01-01 00:00:00'");

		// 清理非预置角色
		dataClearDao.execNativeSql("DELETE FROM sp_roles WHERE DEFINITION_TYPE <> 'C_PRE_DEFINE'");
		// 清理用户和角色关系
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_role_admin_relation");
		// 重置预置用户和角色关系
		dataClearDao.execNativeSql(
				"insert into `sp_role_admin_relation`(`ID`, `ROLE_ID`, `ADMIN_ID`) values ('9f0bcdbfacc9463c99bf15ed51b7966f', '0953325cca254aef89a3f28300ee85ad', 'eb8f71082a85469ea3868dfb5cd59500')");
		dataClearDao.execNativeSql(
				"insert into `sp_role_admin_relation`(`ID`, `ROLE_ID`, `ADMIN_ID`) values ('b168dcd88c874fdf9ec1e9dad0a128ce', '1789012e85d240bbb64d4dfc38f4e2ae', 'd7769e6aae5c4ec89507d79d5e461480')");
		dataClearDao.execNativeSql(
				"insert into `sp_role_admin_relation`(`ID`, `ROLE_ID`, `ADMIN_ID`) values ('e3ed7824b6f34823945045052433b4bf', 'efb969e8326c497896c83722e9aae4e8', '541b53677d52477daf186546a8e5491d')");

		// 清空非预置角色和模块关系
		dataClearDao.execNativeSql(
				"DELETE FROM sp_role_module_permissions WHERE ROLE_ID NOT IN('0953325cca254aef89a3f28300ee85ad','1789012e85d240bbb64d4dfc38f4e2ae','efb969e8326c497896c83722e9aae4e8')");

		logger.info("清理信任主机信息");
		// 清理信任主机信息
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_admin_host_setting");

		// 清理信任主机和用户关系
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_admin_trust_host");
	}

	/**
	 * 清理其他数据
	 */
	private void clearOtherData()
	{
		logger.info("清理其他数据，升级管理数据");
		// 服务端升级清理
		dataClearDao.execNativeSql("TRUNCATE TABLE sp_update_server_package");
		// 清除上传的升级数据
		// 清除上传的升级数据
		StringBuilder path = new StringBuilder();
		path.append(MemInfo.getServletContextPath()).append(File.separator).append("document").append(File.separator)
				.append("update").append(File.separator).append("server").append(File.separator);
		FileUtils.clearDirectory(path.toString());
	}

}
