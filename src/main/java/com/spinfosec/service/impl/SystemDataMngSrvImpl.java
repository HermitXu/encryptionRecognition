package com.spinfosec.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.DataBackupDao;
import com.spinfosec.dao.entity.SpDataBackups;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.SystemArchiveLogRsp;
import com.spinfosec.service.srv.ISystemDataMngSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.GenUtil;
import com.spinfosec.utils.Zip4JUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SystemDataMngSrvImpl
 * @Description: 〈系统备份〉
 * @date 2019/1/16
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("systemDataMngSrv")
public class SystemDataMngSrvImpl implements ISystemDataMngSrv
{

	private Logger log = LoggerFactory.getLogger(SystemDataMngSrvImpl.class);

	@Autowired
	private DataBackupDao systemAchiveLogDao;

	@Autowired
	private ISystemSrv systemSrv;

	@Autowired
	private ApplicationProperty property;

	/**
	 * 数据分类 -- 业务数据
	 */
	public final String BUSI = "BUSI";

	/**
	 * 数据分类 -- 安全数据
	 */
	public final String SAFE = "SAFE";

	/**
	 * 数据分类 -- 系统配置数据
	 */
	public final String SYSTEM = "SYSTEM";

	/**
	 * 分页查询系统备份信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SystemArchiveLogRsp> querySpAdminsByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SystemArchiveLogRsp> data = systemAchiveLogDao.queryData(queryMap);
		PageInfo<SystemArchiveLogRsp> pageList = new PageInfo<>(data);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 创建系统备份记录
	 * @param type
	 * @param auditInfo
	 * @param userId
	 * @return
	 */
	@Override
	public SpDataBackups createSystemData(String type, SpSystemOperateLogInfo auditInfo, String userId)
	{
		String childType = type;
		// 归档日志类别
		if (BUSI.equalsIgnoreCase(childType))
		{
			auditInfo.setOperation("数据备份恢复-归档系统业务数据");
		}
		else if (SAFE.equalsIgnoreCase(childType))
		{
			auditInfo.setOperation("数据备份恢复-归档系统安全数据");
		}
		else if (SYSTEM.equalsIgnoreCase(childType))
		{
			auditInfo.setOperation("数据备份恢复-归档系统配置数据");
		}

		// 保存日志
		systemSrv.saveOperateLog(auditInfo);

		String basic_fileName = childType + "_" + "systemData"
				+ DateUtil.dateToString(new Date(), DateUtil.CUSTOM_FORMAT_PATTERN);
		String folderPath = MemInfo.getServletContextPath() + "systemArchive" + File.separator + basic_fileName;

		SpDataBackups dataBackups = new SpDataBackups();
		dataBackups.setId(GenUtil.getUUID());
		dataBackups.setStatus(Contants.LOG_ARCHIVE_RUNNING);
		dataBackups.setCreateDate(new Date());
		dataBackups.setType(type);
		dataBackups.setLocalpath(folderPath);
		dataBackups.setCreatedBy(userId);
		systemAchiveLogDao.saveDataBackups(dataBackups);

		return dataBackups;
	}

	/**
	 * 更新系统备份记录状态
	 * @param dataBackups
	 * @param status
	 */
	@Override
	public void updateDataBackups(SpDataBackups dataBackups, String status)
	{
		dataBackups.setStatus(status);
		systemAchiveLogDao.updateDataBackups(dataBackups);
	}

	/**
	 * 更新系统备份信息
	 * @param dataBackups
	 */
	@Override
	public void updateDataBackups(SpDataBackups dataBackups)
	{
		systemAchiveLogDao.updateDataBackups(dataBackups);
	}

	/**
	 * 根据ID获取系统备份记录
	 * @param id
	 * @return
	 */
	@Override
	public SpDataBackups getystemAchiveLogById(String id)
	{
		return systemAchiveLogDao.getystemAchiveLogById(id);
	}

	/**
	 * 进行系统数据备份
	 * @param achiveLog
	 */
	@Override
	public void achiveAllData(SpDataBackups achiveLog)
	{
		PrintWriter pw = null;
		BufferedReader normalReader = null;
		BufferedReader errorReader = null;
		try
		{
			String childType = achiveLog.getType();
			String basic_fileName = childType + "_" + "systemData"
					+ DateUtil.dateToString(new Date(), DateUtil.CUSTOM_FORMAT_PATTERN);

			String fileName = basic_fileName + ".sql";
			String folderPath = achiveLog.getLocalpath();

			// 根据记录的路径创建文件
			File achiveFolder = new File(folderPath);
			if (!achiveFolder.exists())
			{
				achiveFolder.mkdirs();
			}

			// sql文件整体路径
			String filePath = folderPath + File.separator + fileName;

			// 要备份的表名
			String tableSql = getTableByType(childType);
			mysqlDump(tableSql, filePath);

			// 压缩包路径
			String descFilePath = folderPath + ".zip";
			Zip4JUtil.zip(folderPath, descFilePath, Zip4JUtil.DATA_ZIP_PWD);
			log.info("系统备份zip 压缩ok...");
			achiveLog.setStatus(Contants.LOG_ARCHIVE_SUCCESS);
			achiveLog.setLocalpath(folderPath);
			File file = new File(descFilePath);
			// 文件大小 KB
			achiveLog.setFilesize((double) file.length() / (double) 1024);

			systemAchiveLogDao.updateDataBackups(achiveLog);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			achiveLog.setStatus(Contants.LOG_ARCHIVE_FAIL);
			systemAchiveLogDao.updateDataBackups(achiveLog);
			log.error("备份系统数据失败！失败原因 : " + e);
		}
		finally
		{
			IOUtils.closeQuietly(pw);
			IOUtils.closeQuietly(normalReader);
			IOUtils.closeQuietly(errorReader);
		}
	}

	/**
	 * mysql 备份命令
	 * @param tables
	 * @param outPath
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void mysqlDump(String tables, String outPath) throws IOException, InterruptedException
	{
		BufferedReader normalReader_files = null;
		BufferedReader errorReader_files = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append("mysqldump  --user=").append(property.getDataUserName()).append(" --password=")
					.append(property.getDataPassWord()).append(" -t --replace ").append(property.getDataName())
					.append(" ").append(tables).append(" > ").append(outPath);
			log.info("MySql备份命令:" + sb.toString());
			Process process_files = null;
			if (StringUtils.isNotEmpty(System.getProperty("os.name"))
					&& System.getProperty("os.name").indexOf("Linux") != -1)
			{
				process_files = Runtime.getRuntime().exec(new String[] { "sh", "-c", sb.toString() });
			}
			else if (StringUtils.isNotEmpty(System.getProperty("os.name"))
					&& System.getProperty("os.name").indexOf("Windows") != -1)
			{
				process_files = Runtime.getRuntime().exec("cmd  /c " + sb.toString());
			}
			if (null != process_files)
			{
				errorReader_files = new BufferedReader(new InputStreamReader(process_files.getErrorStream()));
				normalReader_files = new BufferedReader(new InputStreamReader(process_files.getInputStream()));

				String line_files = null;
				while ((line_files = errorReader_files.readLine()) != null)
				{
					log.error("数据归档结束异常：" + line_files);
				}
				while ((line_files = normalReader_files.readLine()) != null)
				{
					log.debug("数据归档结束信息：" + line_files);
				}
				process_files.waitFor();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			IOUtils.closeQuietly(normalReader_files);
			IOUtils.closeQuietly(errorReader_files);
		}
	}

	/**
	 * 系统备份恢复
	 * @param dataBackups
	 * @param auditInfo
	 */
	@Override
	public void recoverDataById(SpDataBackups dataBackups, SpSystemOperateLogInfo auditInfo)
	{
		try
		{
			if (null == dataBackups)
			{
				throw new TMCException(RspCode.OBJECE_NOT_EXIST);
			}
			if (BUSI.equalsIgnoreCase(dataBackups.getType()))
			{
				auditInfo.setOperation("数据备份恢复-恢复系统业务数据");
			}
			else if (SAFE.equalsIgnoreCase(dataBackups.getType()))
			{
				auditInfo.setOperation("数据备份恢复-恢复系统安全数据");
			}
			else if (SYSTEM.equalsIgnoreCase(dataBackups.getType()))
			{
				auditInfo.setOperation("数据备份恢复-恢复系统配置数据");
			}

			// 记录恢复日志
			systemSrv.saveOperateLog(auditInfo);

			String filePath = dataBackups.getLocalpath();
			File backupFile = new File(filePath);
			if (!backupFile.exists())
			{
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}

			recoverDataByFile(backupFile);
			dataBackups.setRecoverOptlock(dataBackups.getRecoverOptlock() + 1);
			dataBackups.setStatus(Contants.LOG_RECOVER_SUCCESS);

			systemAchiveLogDao.updateDataBackups(dataBackups);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dataBackups.setStatus(Contants.LOG_RECOVER_FAIL);
			systemAchiveLogDao.updateDataBackups(dataBackups);
		}
	}

	private void recoverDataByFile(File backFilePath) throws IOException, InterruptedException
	{
		BufferedReader normalReader_files = null;
		BufferedReader errorReader_files = null;
		try
		{
			// 生成的文件进行过滤.sql
			FileFilter filter = new FileFilter()
			{
				@Override
				public boolean accept(File pathname)
				{
					return pathname.getName().toLowerCase().endsWith(".sql");
				}
			};
			File[] files = backFilePath.listFiles(filter);
			for (int i = 0; i < files.length; i++)
			{
				StringBuffer mysqlDump = new StringBuffer();
				// Linux
				if (StringUtils.isNotEmpty(System.getProperty("os.name"))
						&& System.getProperty("os.name").indexOf("Linux") != -1)
				{

					mysqlDump.append("mysql --user=").append(property.getDataUserName()).append(" --password=")
							.append(property.getDataPassWord()).append(" -f ").append(property.getDataName())
							.append(" < ").append(files[i]);
					log.info("==================================" + mysqlDump.toString());
					Process process = java.lang.Runtime.getRuntime()
							.exec(new String[] { "sh", "-c", mysqlDump.toString() });

					errorReader_files = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					normalReader_files = new BufferedReader(new InputStreamReader(process.getInputStream()));

					String line_files = null;
					while ((line_files = errorReader_files.readLine()) != null)
					{
						log.error("Linux数据归档结束异常：" + line_files);
					}
					while ((line_files = normalReader_files.readLine()) != null)
					{
						log.debug("Linux数据归档结束信息：" + line_files);
					}

					process.waitFor();
				}
				else if (StringUtils.isNotEmpty(System.getProperty("os.name"))
						&& System.getProperty("os.name").indexOf("Windows") != -1)
				{
					// Windows
					mysqlDump.append("mysql  -u ").append(property.getDataUserName()).append(" -p --password=")
							.append(property.getDataPassWord()).append(" -f ").append(property.getDataName())
							.append(" < ").append(files[i]);
					log.info("==================================" + mysqlDump.toString());
					Process process = Runtime.getRuntime().exec("cmd  /c " + mysqlDump.toString());

					errorReader_files = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					normalReader_files = new BufferedReader(new InputStreamReader(process.getInputStream()));

					String line_files = null;
					while ((line_files = errorReader_files.readLine()) != null)
					{
						log.error("Linux数据归档结束异常：" + line_files);
					}
					while ((line_files = normalReader_files.readLine()) != null)
					{
						log.debug("Linux数据归档结束信息：" + line_files);
					}

					process.waitFor();
				}
			}
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
			log.error("恢复系统数据出错,错误原因:" + e);
			throw e;
		}
		finally
		{
			IOUtils.closeQuietly(normalReader_files);
			IOUtils.closeQuietly(errorReader_files);
		}
	}

	public String getTableByType(String type)
	{
		StringBuffer mysql = new StringBuffer();
		if (BUSI.equalsIgnoreCase(type))
		{
			mysql.append(
					" sp_admin_host_setting sp_admin_trust_host sp_admins sp_algorithm_file_type sp_code_decodes sp_column_resinfo sp_data_backups sp_day_timeframe");
			mysql.append(
					" sp_day_timeframe_hours sp_discovery_tasks sp_dscvr_excld_file_types sp_dscvr_incld_file_types sp_encryption_algorithm sp_org_admin_relation sp_org_unit_dict ");
			mysql.append(
					" sp_plc_file_types sp_plc_file_types_category sp_role_admin_relation sp_role_module_permissions sp_roles sp_scheduling_data sp_scheduling_day_tf ");
			mysql.append(
					" sp_sec_password_complexity_item sp_sec_password_policy sp_server_status sp_target_res sp_target_res_detail  sp_theme_info_setting sp_update_server_package");
		} // 安全数据包括 系统操作日志 系统日志归档 事件 任务中心 任务处理成功文件 任务处理失败文件 系统升级 安全日志归档
		else if (SAFE.equalsIgnoreCase(type))
		{
			mysql.append(
					" sp_system_operate_archive_log sp_system_operate_log_info sp_dscvr_files sp_task sp_task_files sp_task_sipped sp_update_server_package sp_event_archive_log");
		} // 系统数据包括 系统配置
		else if (SYSTEM.equalsIgnoreCase(type))
		{
			mysql.append(" sp_config_properties ");
		}
		return mysql.toString();
	}

	/**
	 * 删除系统备份数据压缩包
	 * @param id
	 * @param auditInfo
	 * @return
	 */
	@Override
	public CodeRsp deleteLog(String id, SpSystemOperateLogInfo auditInfo)
	{
		if (StringUtils.isEmpty(id))
		{
			log.error("删除系统数据，id为空");
			return new CodeRsp(RspCode.RECOVER_BACKUP_ERROR);
		}

		SpDataBackups dataBackups = getystemAchiveLogById(id);

		if (BUSI.equalsIgnoreCase(dataBackups.getType()))
		{
			auditInfo.setOperation("数据备份恢复-删除系统业务数据记录及备份文件");
		}
		else if (SAFE.equalsIgnoreCase(dataBackups.getType()))
		{
			auditInfo.setOperation("数据备份恢复-删除系统安全数据记录及备份文件");
		}
		else if (SYSTEM.equalsIgnoreCase(dataBackups.getType()))
		{
			auditInfo.setOperation("数据备份恢复-删除系统配置数据记录及备份文件");
		}

		// 保存操作日志
		systemSrv.saveOperateLog(auditInfo);

		String filePath = dataBackups.getLocalpath();
		File backupFile = new File(filePath);

		try
		{
			FileUtils.deleteDirectory(backupFile);
			FileUtils.deleteQuietly(new File(filePath + ".zip"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// 删除数据库记录
		systemAchiveLogDao.deleteDataBackupsById(id);

		return new CodeRsp(RspCode.SUCCESS);
	}

	/**
	 * 创建系统备份恢复记录
	 * @param logPath
	 * @param tabType
	 * @param userId
	 * @return
	 * @throws TMCException
	 */
	@Override
	public SpDataBackups createDataBackups(String logPath, String tabType, String userId) throws TMCException
	{
		// 判断该数据压缩包的记录是否已经存在 存在则更新 不存在则保存
		// 直接判断systemArchive文件以后的路径 有的路径的\和/是相反的
		String file = "";
		file = logPath.substring(logPath.indexOf("systemArchive") + 14);
		SpDataBackups dataBackups = systemAchiveLogDao.getDataBackupsByPath(file);
		// 判断是保存还是更新
		boolean update = true;
		if (null == dataBackups)
		{
			update = false;
			dataBackups = new SpDataBackups();
			dataBackups.setId(GenUtil.getUUID());
			dataBackups.setLocalpath(logPath);
			dataBackups.setCreateDate(new Date());
		}
		dataBackups.setCreatedBy(userId);
		dataBackups.setStatus(Contants.LOG_RECOVER_RUNNING);
		dataBackups.setType(tabType);

		if (update)
		{
			systemAchiveLogDao.updateDataBackups(dataBackups);
		}
		else
		{
			systemAchiveLogDao.saveDataBackups(dataBackups);
		}
		return dataBackups;
	}
}
