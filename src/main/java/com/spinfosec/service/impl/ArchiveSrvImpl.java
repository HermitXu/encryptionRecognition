package com.spinfosec.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.DscvrFilesDao;
import com.spinfosec.dao.ArchiveLogDao;
import com.spinfosec.dao.SysOperateLogDao;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpEventArchiveLog;
import com.spinfosec.dao.entity.SpSystemOperateArchiveLog;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.EventArchiveLogRsp;
import com.spinfosec.dto.pojo.system.SysOperateArchiveLogRsp;
import com.spinfosec.service.srv.IArchiveSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.GenUtil;
import com.spinfosec.utils.Zip4JUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ArchiveSrvImpl
 * @Description: 〈日志归档实现类〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("archiveSrv")
public class ArchiveSrvImpl implements IArchiveSrv
{

	private static final Logger log = LoggerFactory.getLogger(ArchiveSrvImpl.class);

	@Autowired
	private ArchiveLogDao archiveLogDao;

	@Autowired
	private DscvrFilesDao dscvrFilesDao;

	@Autowired
	private SysOperateLogDao sysOperateLogDao;

	@Autowired
	private ISystemSrv systemSrv;

	@Autowired
	private ApplicationProperty property;

	/**
	 * 分页查询安全日志归档信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<EventArchiveLogRsp> queryEventByPage(Map<String, Object> queryMap)
	{

		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<EventArchiveLogRsp> eventArchiveLogRspArrayList = archiveLogDao.queryEventByPage(queryMap);
		PageInfo<EventArchiveLogRsp> pageList = new PageInfo<>(eventArchiveLogRspArrayList);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 分页查询系统操作日志归档记录
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SysOperateArchiveLogRsp> querySysOperateByPage(Map<String, Object> queryMap)
	{

		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SysOperateArchiveLogRsp> list = archiveLogDao.querySysByPage(queryMap);
		PageInfo<SysOperateArchiveLogRsp> pageInfo = new PageInfo<>(list);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageInfo;
	}

	/**
	 *创建安全日志归档
	 * @param archive
	 * @throws TMCException
	 */
	@Override
	public SpEventArchiveLog createEventLogArchive(SpEventArchiveLog archive, String userId, String folderPath,
			SpSystemOperateLogInfo operateLogInfo)
			throws TMCException
	{
		// 日志归档的开始时间和结束时间
		Date startDate = archive.getStartDate();
		Date endDate = archive.getEndDate();
		String startString = DateUtil.dateToString(startDate, DateUtil.DATETIME_FORMAT_PATTERN);
		String endString = DateUtil.dateToString(endDate, DateUtil.DATETIME_FORMAT_PATTERN);
		if (startDate.getTime() > endDate.getTime())
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_DATE_INVALID);
		}
		//查询条件封装
		Map<String, Object> conditionMap = new HashMap<>();
		// 根据用户查询(预置用户查询所有事件)
		SpAdmins user = systemSrv.getAdminById(userId);
		if(!user.getDefinitionType().equals(Contants.C_PRE_DEFINE)){
			conditionMap.put("createdBy",user.getId());
		}


		conditionMap.put("start", startString);
		conditionMap.put("end", endString);

		// 查询归档时间范围内是否符合条件的事件
		Long checkEventCount = dscvrFilesDao.countEventByCondition(conditionMap);
		if (checkEventCount.longValue() == 0)
		{
			throw new TMCException(RspCode.NO_MACTH_CONDITION_DATA);
		}
		else
		{
			String fileName_basic = "safe_event_log." + DateUtil.dateToString(startDate, DateUtil.CUSTOM_FORMAT_PATTERN)
					+ "--" + DateUtil.dateToString(endDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "."
					+ System.currentTimeMillis();
			folderPath = folderPath + "logArchive" + File.separator + fileName_basic;
			// 保存归档数据库记录
			archive.setId(GenUtil.getUUID());
			archive.setStatus(Contants.LOG_ARCHIVE_RUNNING);
			archive.setStartDate(startDate);
			archive.setEndDate(endDate);
			archive.setIsdel(archive.getIsdel());
			archive.setCreateDate(new Date());
			archive.setCreatedBy(userId);
			archive.setPath(folderPath);
			archiveLogDao.saveEventArchiveLog(archive);
		}

		// 保存日志
		systemSrv.saveOperateLog(operateLogInfo);

		return archive;
	}

	/**
	 * 根据ID获取安全日志归档记录
	 * @param id
	 * @return
	 */
	@Override
	public SpEventArchiveLog getAcrhiveLogById(String id)
	{
		return archiveLogDao.getEventAcrhiveLogById(id);
	}

	/**
	 * 根据ID获取系统操作日志归档记录
	 * @param id
	 * @return
	 */
	@Override
	public SpSystemOperateArchiveLog getOperateArchiveLogById(String id)
	{
		return archiveLogDao.getOperateArchiveLogById(id);
	}

	/**
	 * 开始安全日志归档进程
	 * @param archive
	 * @param userId
	 */
	@Override
	public void startEventAcrhive(SpEventArchiveLog archive, String userId)
	{
		try
		{
			Date startDate = archive.getStartDate();
			Date endDate = archive.getEndDate();
			String startString = DateUtil.dateToString(startDate, DateUtil.DATETIME_FORMAT_PATTERN);
			String endString = DateUtil.dateToString(endDate, DateUtil.DATETIME_FORMAT_PATTERN);
			//查询条件封装
			Map<String, Object> conditionMap = new HashMap<>();

			// 根据用户查询(预置用户查询所有事件)
			SpAdmins user = systemSrv.getAdminById(userId);
			if(!user.getDefinitionType().equals(Contants.C_PRE_DEFINE)){
				conditionMap.put("createdBy",user.getId());
			}
			conditionMap.put("start", startString);
			conditionMap.put("end", endString);

			// 查询归档时间范围内是否符合条件的事件
			Long checkEventCount = dscvrFilesDao.countEventByCondition(conditionMap);

			// 归档文件存储文件名
			String fileName_basic_files = "sp_dscvr_files."
					+ DateUtil.dateToString(startDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "--"
					+ DateUtil.dateToString(endDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "." + System.currentTimeMillis();

			// 备份Sql文件名
			String fileName_files = fileName_basic_files + ".sql";

			// 创建文件夹(否则如果部署的时候无backup文件夹则会报错导致归档失败)
			String folderPath = archive.getPath();

			File bacupFolder = new File(folderPath);
			if (!bacupFolder.exists())
			{
				bacupFolder.mkdirs();
			}
			else
			{
				FileUtils.forceDelete(bacupFolder);
				bacupFolder.mkdirs();
			}

			// 备份Sql文件完成目录
			String filePath_files = folderPath + File.separator + fileName_files;

			// 保存文件路径
			archive.setPath(folderPath);

			String tableSql_files = " sp_dscvr_files ";
			StringBuilder whereCondition = new StringBuilder();
			whereCondition.append(" DETECT_DATE_TS >='").append(startString).append("' AND DETECT_DATE_TS <='")
					.append(endString).append("'");

			// 根据用户查询(预置用户查询所有事件)
			if(!user.getDefinitionType().equals(Contants.C_PRE_DEFINE)){
				whereCondition.append(" AND CREATED_BY ='").append(user.getId()).append("'");
			}
			// 备份
			mysqlDump(tableSql_files, whereCondition.toString(), filePath_files);

			// 保存记录个数
			archive.setIncidentNum("检查事件数：" + checkEventCount + "条");
			// 保存归档数据库记录
			archive.setStatus(Contants.LOG_ARCHIVE_SUCCESS);
			archiveLogDao.updateEventArchiveLog(archive);

			// 判断是否删除原记录
			if (StringUtils.isNotEmpty(archive.getIsdel()))
			{
				if (archive.getIsdel().equalsIgnoreCase("1"))
				{
					dscvrFilesDao.deleteEventByCondition(conditionMap);
				}
			}

			String descFilePath = archive.getPath() + ".zip";
			log.info("安全日志归档文件开始压缩：" + descFilePath);
			Zip4JUtil.zip(archive.getPath(), descFilePath, Zip4JUtil.DATA_ZIP_PWD);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			archive.setStatus(Contants.LOG_ARCHIVE_FAIL);
			archiveLogDao.updateEventArchiveLog(archive);
			log.error("备份安全日志失败！失败原因 : ", e);
		}

	}

	/**
	 * 开始系统操作日志归档进程
	 * @param operateArchiveLog
	 */
	@Override
	public void startSysOperaRecover(SpSystemOperateArchiveLog operateArchiveLog, String userId)
	{
		try
		{
			// 根据用户查询
			SpAdmins adminById = systemSrv.getAdminById(userId);

			Date startDate = operateArchiveLog.getStartDate();
			Date endDate = operateArchiveLog.getEndDate();
			String startString = DateUtil.dateToString(startDate, DateUtil.DATETIME_FORMAT_PATTERN);
			String endString = DateUtil.dateToString(endDate, DateUtil.DATETIME_FORMAT_PATTERN);

			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("start", startString);
			conditionMap.put("end", endString);

			// 查询归档时间范围内是否符合条件的系统操作日志
			Long sysOperateLogCount = sysOperateLogDao.countSystemOperateLogByCondition(conditionMap);

			String fileName_basic_audit = "sp_system_operate_archive_log."
					+ DateUtil.dateToString(startDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "--"
					+ DateUtil.dateToString(endDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "." + System.currentTimeMillis();

			// 备份Sql文件名
			String fileName_files = fileName_basic_audit + ".sql";

			// 创建文件夹(否则如果部署的时候无backup文件夹则会报错导致归档失败)
			String folderPath = operateArchiveLog.getPath();

			File bacupFolder = new File(folderPath);
			if (!bacupFolder.exists())
			{
				bacupFolder.mkdirs();
			}
			else
			{
				FileUtils.forceDelete(bacupFolder);
				bacupFolder.mkdirs();
			}

			// 备份Sql文件完成目录
			String filePath_files = folderPath + File.separator + fileName_files;

			// 保存文件路径
			operateArchiveLog.setPath(folderPath);

			String tableSql_files = " sp_system_operate_log_info ";
			StringBuilder whereCondition = new StringBuilder();
			whereCondition.append(" GENERATION_TIME_TS >='").append(startString).append("' AND GENERATION_TIME_TS <='")
					.append(endString).append("'");

			// 备份
			mysqlDump(tableSql_files, whereCondition.toString(), filePath_files);

			// 保存记录个数
			operateArchiveLog.setIncidentNum("总数：" + sysOperateLogCount + "条");
			// 保存归档数据库记录
			operateArchiveLog.setStatus(Contants.LOG_ARCHIVE_SUCCESS);

			// 更新系统操作日志归档记录
			archiveLogDao.updateOperateArchiveLog(operateArchiveLog);

			// 判断是否删除原记录
			if (StringUtils.isNotEmpty(operateArchiveLog.getIsdel()))
			{
				if (operateArchiveLog.getIsdel().equalsIgnoreCase("1"))
				{
					sysOperateLogDao.deleteOperateLogByQueryMap(conditionMap);
				}
			}

			String descFilePath = operateArchiveLog.getPath() + ".zip";
			log.info("系统操作日志归档文件开始压缩：" + descFilePath);
			Zip4JUtil.zip(operateArchiveLog.getPath(), descFilePath, Zip4JUtil.DATA_ZIP_PWD);
		}
		catch (Exception e)
		{
			log.error("备份系统操作日志失败！失败原因 : ", e);
		}

	}

	/**
	 * mysqlDump备份
	 * @param tables
	 * @param where
	 * @param outPath
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void mysqlDump(String tables, String where, String outPath) throws IOException, InterruptedException
	{
		BufferedReader normalReader_files = null;
		BufferedReader errorReader_files = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append("mysqldump  --user=")
					.append (property.getDataUserName())
					.append(" --password=")
					.append(property.getDataPassWord())
					.append(" -t --replace ").append(property.getDataName())
					.append(" ").append(tables)
					.append(" --where \"").append(where).append("\"").append(" > ").append(outPath);
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
	 * 更新安全日志归档记录状态为恢复中
	 * @param id 事件日志id
	 * @throws Exception
	 */
	@Override
	public SpEventArchiveLog startEventRecover(String id) throws TMCException
	{
		SpEventArchiveLog spEventArchiveLog = archiveLogDao.getEventAcrhiveLogById(id);
		if (null == spEventArchiveLog)
		{
			throw new TMCException(RspCode.NO_MACTH_CONDITION_DATA);
		}
		spEventArchiveLog.setStatus(Contants.LOG_RECOVER_RUNNING);
		archiveLogDao.updateEventArchiveLog(spEventArchiveLog);
		return spEventArchiveLog;
	}

	/**
	 * 更新操作日志归档记录状态为恢复中
	 * @param id
	 * @return
	 */
	@Override
	public SpSystemOperateArchiveLog startSysOperaRecover(String id)
	{
		SpSystemOperateArchiveLog operateArchiveLog = archiveLogDao.getOperateArchiveLogById(id);
		if (null == operateArchiveLog)
		{
			throw new TMCException(RspCode.OBJECE_NOT_EXIST);
		}
		operateArchiveLog.setStatus(Contants.LOG_RECOVER_RUNNING);
		archiveLogDao.updateOperateArchiveLog(operateArchiveLog);
		return operateArchiveLog;
	}

	/**
	 * 恢复事件日志归档记录
	 * @param spEventArchiveLog
	 * @throws Exception
	 */
	@Override
	public void recoverEventLogArchive(SpEventArchiveLog spEventArchiveLog, SpSystemOperateLogInfo operateLogInfo)
	{
		try
		{
			String filePath = spEventArchiveLog.getPath();
			File backupFile = new File(filePath);
			if (!backupFile.exists())
			{
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
			// MySQL数据恢复
			recoverEventLogArchive(backupFile);
			spEventArchiveLog.setStatus(Contants.LOG_RECOVER_SUCCESS);
			spEventArchiveLog.setRecoverOptLock(spEventArchiveLog.getRecoverOptLock() + 1);
			archiveLogDao.updateEventArchiveLog(spEventArchiveLog);

			// 保存日志
			systemSrv.saveOperateLog(operateLogInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("安全日志归档恢复失败！失败原因：", e);
			spEventArchiveLog.setStatus(Contants.LOG_RECOVER_FAIL);
			archiveLogDao.updateEventArchiveLog(spEventArchiveLog);
		}

	}

	/**
	 * 恢复系统操作日志归档记录
	 * @param operateArchiveLog
	 */
	@Override
	public void recoverSysOperaAcrhive(SpSystemOperateArchiveLog operateArchiveLog,
			SpSystemOperateLogInfo operateLogInfo)
	{
		try
		{
			String filePath = operateArchiveLog.getPath();
			File backupFile = new File(filePath);
			if (!backupFile.exists())
			{
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
			// MySQL数据恢复
			recoverEventLogArchive(backupFile);
			operateArchiveLog.setStatus(Contants.LOG_RECOVER_SUCCESS);
			operateArchiveLog.setRecoverOptLock(operateArchiveLog.getRecoverOptLock() + 1);
			archiveLogDao.updateOperateArchiveLog(operateArchiveLog);

			// 保存日志
			systemSrv.saveOperateLog(operateLogInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("系统操作日志归档恢复失败！失败原因：" + e.toString());
			operateArchiveLog.setStatus(Contants.LOG_RECOVER_FAIL);
			archiveLogDao.updateOperateArchiveLog(operateArchiveLog);
		}
	}

	/**
	 * 执行恢复命令
	 * @param backFilePath
	 * @return
	 */
	private CodeRsp recoverEventLogArchive(File backFilePath)
	{
		CodeRsp rsp = new CodeRsp(RspCode.SUCCESS);
		BufferedReader errorReader_files = null;
		BufferedReader normalReader_files = null;
		try
		{
			// 生成的四个文件进行过滤.sql
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
				// 文件解密操作
				// FileEncryptDecryptUtil.decrypt(files[i].toString(), AES.SYS_PRIVATE_KEY);
				// 调用mysql命令 备份数据库中所有数据--不包括事件
				StringBuffer mysqlDump = new StringBuffer();
				// Linux
				if (StringUtils.isNotEmpty(System.getProperty("os.name"))
						&& System.getProperty("os.name").toLowerCase().indexOf("linux") != -1)
				{
					mysqlDump.append("mysql --user=")
							.append(property.getDataUserName())
							.append(" --password=")
							.append(property.getDataPassWord())
							.append(" -f ").append(property.getDataName())
							.append(" < ").append(files[i]);
					log.info(mysqlDump.toString());
					Process process = Runtime.getRuntime().exec(new String[] { "sh", "-c", mysqlDump.toString() });

					errorReader_files = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					normalReader_files = new BufferedReader(new InputStreamReader(process.getInputStream()));

					String line_files = null;
					while ((line_files = errorReader_files.readLine()) != null)
					{
						log.error("sql导入异常信息：" + line_files);
					}
					while ((line_files = normalReader_files.readLine()) != null)
					{
						log.debug("sql导入结束信息：" + line_files);
					}

					process.waitFor();
					rsp = new CodeRsp(RspCode.SUCCESS);
				}
				else if (StringUtils.isNotEmpty(System.getProperty("os.name"))
						&& System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
				{
					// Windows
					mysqlDump.append("mysql  -u")
							.append(property.getDataUserName())
							.append(" -p")
							.append(property.getDataPassWord())
							.append(" -f ").append(property.getDataName())
							.append(" < ").append(files[i]);
					log.info(mysqlDump.toString());
					Process process = Runtime.getRuntime().exec("cmd  /c " + mysqlDump.toString());

					errorReader_files = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					normalReader_files = new BufferedReader(new InputStreamReader(process.getInputStream()));

					String line_files = null;
					while ((line_files = errorReader_files.readLine()) != null)
					{
						log.error("sql导入异常信息：" + line_files);
					}
					while ((line_files = normalReader_files.readLine()) != null)
					{
						log.debug("sql导入结束信息：" + line_files);
					}

					process.waitFor();
					rsp = new CodeRsp(RspCode.SUCCESS);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.RECOVER_BACKUP_ERROR, e);
		}
		finally
		{
			IOUtils.closeQuietly(errorReader_files);
			IOUtils.closeQuietly(normalReader_files);
		}

		return rsp;
	}

	/**
	 * 更新安全日志进程状态
	 * @param eventArchiveLog
	 */
	@Override
	public void updateEventArchiveLog(SpEventArchiveLog eventArchiveLog, String status)
	{
		eventArchiveLog.setStatus(status);
		archiveLogDao.updateEventArchiveLog(eventArchiveLog);
	}

	/**
	 * 更新系统操作日志归档进程
	 * @param operateArchiveLog
	 */
	@Override
	public void updateOperateArchiveLog(SpSystemOperateArchiveLog operateArchiveLog, String status)
	{
		operateArchiveLog.setStatus(status);
		archiveLogDao.updateOperateArchiveLog(operateArchiveLog);
	}

	/**
	 * 创建系统操作日志归档记录
	 * @param operateArchiveLog
	 * @param userId
	 * @param folderPath
	 * @return
	 */
	@Override
	public SpSystemOperateArchiveLog createSysOperateLogArchive(SpSystemOperateArchiveLog operateArchiveLog,
			String userId, String folderPath, SpSystemOperateLogInfo operateLogInfo)
	{
		// 日志归档的开始时间和结束时间
		Date startDate = operateArchiveLog.getStartDate();
		Date endDate = operateArchiveLog.getEndDate();
		String startString = DateUtil.dateToString(startDate, DateUtil.DATETIME_FORMAT_PATTERN);
		String endString = DateUtil.dateToString(endDate, DateUtil.DATETIME_FORMAT_PATTERN);
		if (startDate.getTime() > endDate.getTime())
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_DATE_INVALID);
		}
		// 根据用户查询
		SpAdmins adminById = systemSrv.getAdminById(userId);
		Map<String, Object> conditionMap = new HashMap<>();
		conditionMap.put("start", startString);
		conditionMap.put("end", endString);

		// 三个月以内的日志无法进行归档
		if (DateUtil.dateDiff(new Date(), endDate) <= 90)
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_LESS_THREE_MONTH);
		}
		// 查询归档时间范围内是否符合条件的系统操作日志
		Long sysOperateLogCount = sysOperateLogDao.countSystemOperateLogByCondition(conditionMap);
		if (sysOperateLogCount == 0)
		{
			throw new TMCException(RspCode.NO_MACTH_CONDITION_DATA);
		}
		else
		{
			String fileName_basic = "system_operate_log."
					+ DateUtil.dateToString(startDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "--"
					+ DateUtil.dateToString(endDate, DateUtil.CUSTOM_FORMAT_PATTERN) + "." + System.currentTimeMillis();

			folderPath = folderPath + "logArchive" + File.separator + fileName_basic;

			// 保存归档数据库记录
			operateArchiveLog.setId(GenUtil.getUUID());
			operateArchiveLog.setStartDate(startDate);
			operateArchiveLog.setEndDate(endDate);
			operateArchiveLog.setStatus(Contants.LOG_ARCHIVE_RUNNING);
			operateArchiveLog.setIsdel(operateArchiveLog.getIsdel());
			operateArchiveLog.setCreateDate(new Date());
			operateArchiveLog.setCreatedBy(userId);
			operateArchiveLog.setPath(folderPath);
			archiveLogDao.saveSystemOperateArchiveLog(operateArchiveLog);
		}

		// 保存日志
		systemSrv.saveOperateLog(operateLogInfo);
		return operateArchiveLog;
	}

	/**
	 * 删除安全日志归档记录
	 * @param ids
	 */
	@Override
	public void deleteEventArchiveLog(List<String> ids)
	{
		for (String id : ids)
		{
			SpEventArchiveLog acrhiveLogById = archiveLogDao.getEventAcrhiveLogById(id);
			if (null == acrhiveLogById)
			{
				throw new TMCException(RspCode.NO_MACTH_CONDITION_DATA);
			}
			String filePath = acrhiveLogById.getPath();
			File backupFile = new File(filePath);
			try
			{
				FileUtils.deleteDirectory(backupFile);
				FileUtils.deleteQuietly(new File(filePath + ".zip"));
			}
			catch (Exception e)
			{
				StringWriter sw = new StringWriter();
				PrintWriter epw = new PrintWriter(sw);
				e.printStackTrace(epw);
			}
			archiveLogDao.deleteEventArchiveLogById(id);
		}
	}

	/**
	 * 删除系统操作日志归档记录
	 * @param ids
	 */
	@Override
	public void deleteOperateArchiveLog(List<String> ids)
	{
		for (String id : ids)
		{
			// 查询归档记录
			SpSystemOperateArchiveLog operateArchiveLogById = archiveLogDao.getOperateArchiveLogById(id);
			if (null == operateArchiveLogById)
			{
				throw new TMCException(RspCode.NO_MACTH_CONDITION_DATA);
			}
			String filePath = operateArchiveLogById.getPath();
			File backupFile = new File(filePath);
			try
			{
				FileUtils.deleteDirectory(backupFile);
				FileUtils.deleteQuietly(new File(filePath + ".zip"));
			}
			catch (Exception e)
			{
				StringWriter sw = new StringWriter();
				PrintWriter epw = new PrintWriter(sw);
				e.printStackTrace(epw);
			}
			archiveLogDao.deleteOperateArchiveLog(id);
		}
	}

	/**
	 * 是否有正在归档或恢复的任务在执行中
	 * @param archiveStatus
	 * @param recoverStatus
	 * @return
	 */
	@Override
	public RspCode isArchiveOrRecoverRunning(String archiveStatus, String recoverStatus)
	{
		RspCode rspCode = RspCode.SUCCESS;
		Long eventRuning = archiveLogDao.isArchiveOrRecoverRunning(Contants.sp_event_archive_log, archiveStatus,
				recoverStatus);
		Long sysLogRuning = archiveLogDao.isArchiveOrRecoverRunning(Contants.sp_system_operate_log_info, archiveStatus,
				recoverStatus);
		Long dataBackRuning = archiveLogDao.isArchiveOrRecoverRunning(Contants.sp_data_backups, archiveStatus,
				recoverStatus);
		if (eventRuning > 0 || sysLogRuning > 0 || dataBackRuning > 0)
		{
			rspCode = RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING;
		}
		return rspCode;
	}

	/**
	 * 创建上传zip文件进行恢复记录
	 * @param logPath
	 * @param userId
	 * @return
	 */
	@Override
	public SpEventArchiveLog processArchiveLogRecord(String logPath, String userId)
	{
		// 判断该数据压缩包的记录是否已经存在 存在则更新 不存在则保存
		// 直接判断logArchive文件以后的路径 有的路径的\和/是相反的
		String file = "";
		file = logPath.substring(logPath.indexOf("logArchive") + 11);
		SpEventArchiveLog eventArchiveLog = archiveLogDao.getEventArchiveByPath(file);
		// 判断是保存还是更新
		boolean update = true;
		if (null == eventArchiveLog)
		{
			update = false;
			eventArchiveLog = new SpEventArchiveLog();
			eventArchiveLog.setId(GenUtil.getUUID());
			eventArchiveLog.setPath(logPath);
			eventArchiveLog.setCreateDate(new Date());
		}
		eventArchiveLog.setCreatedBy(userId);
		eventArchiveLog.setStatus(Contants.LOG_RECOVER_RUNNING);
		if (update)
		{
			archiveLogDao.updateEventArchiveLog(eventArchiveLog);
		}
		else
		{
			archiveLogDao.saveEventArchiveLog(eventArchiveLog);
		}

		return eventArchiveLog;
	}
}
