package com.spinfosec.service.impl;

import com.spinfosec.dao.entity.SpColumnResInfo;
import com.spinfosec.dao.tactic.DiscoveryTasksDao;
import com.spinfosec.dao.tactic.JobScheduleDao;
import com.spinfosec.dto.pojo.deploy.JobDeployObj;
import com.spinfosec.service.srv.IJobScheduleXml;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.GenUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName JobScheduleXmlImpl
 * @Description: 〈任务部署xml处理接口实现类〉
 * @date 2018/11/12
 * All rights Reserved, Designed By SPINFO
 */
@Service("jobScheduleXml")
public class JobScheduleXmlImpl implements IJobScheduleXml
{

	private static final Logger log = LoggerFactory.getLogger(JobScheduleXmlImpl.class);

	@Autowired
	private JobScheduleDao jobScheduleDao;

	@Autowired
	private DiscoveryTasksDao discoveryTasksDao;

	@Override
	public List<JobDeployObj> jobScheduleCreateXml(List<String> ids) throws Exception
	{
		List<JobDeployObj> jobDocumentList = new ArrayList<JobDeployObj>();
		// 数据库类型策略
		generateDBScanXML(jobDocumentList, ids);
		// 文件共享类型策略
		generateFileSystemScanXML(jobDocumentList, ids);
		// Ftp类型策略
		generateFtpScanXml(jobDocumentList, ids);
		// Linux类型策略
		generateSftpScanXml(jobDocumentList, ids);
		// SharePoint类型策略
		generateSharePointScanXML(jobDocumentList, ids);
		// Lotus类型策略
		generateLotusXML(jobDocumentList, ids);
		// Exchange类型策略
		generateExchangeScanXML(jobDocumentList, ids);

		return jobDocumentList;
	}

	/**
	 * 生成数据库扫描任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 * @throws ParseException
	 */
	private void generateDBScanXML(List<JobDeployObj> jobDocumentList, List<String> ids) throws ParseException
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getDbDiscoveryTaskInfo(ids));
		for (Object[] jobelement : joblist)
		{
			String jobId = jobelement[1].toString();
			List<Object[]> targetList = GenUtil.mapToListObj(jobScheduleDao.dbLoutsTargetInfo(jobId));

			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");

			String type = "discovery";
			String childType = Contants.DB_SCAN_TASK_DISCOVER;

			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[17].toString()))
			{
				timerAge = jobelement[18].toString().equals("") ? "1" : jobelement[18].toString();
			}

			Element jobelementresult = jobele.addAttribute("type", type).addAttribute("id", jobId)
					.addAttribute("name", jobelement[0].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", childType).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[19] ? jobelement[19].toString() : "")
					.addAttribute("orgName", null != jobelement[20] ? jobelement[20].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[2].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");

			Element jobworker = workerNode.addElement("crawler");
			// crawler 节点下的settings节点
			Element jobSetting = jobworker.addElement("settings");

			// 闲时带宽（默认不启动）
			Element bandSettings = jobSetting.addElement("bandSettings");
			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			// 忙时带宽
			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[11].toString().equals("") ? "1" : jobelement[11].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[14].toString().equals("") ? "1" : jobelement[14].toString());
			settingBusy.addAttribute("start", null != jobelement[12] ? jobelement[12].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[13] ? jobelement[13].toString() : "");

			// credentials标签
			Element jobCredentials = jobSetting.addElement("credentials");
			jobCredentials.addElement("domain").addText("");
			jobCredentials.addElement("username").addText("");
			jobCredentials.addElement("password").addText("");

			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			Element spdatabase = jobworker.addElement("spdatabase");

			for (Object[] objects : targetList)
			{
				String databaseType = objects[0].toString();
				String databaseName = "";
				if (databaseType.equalsIgnoreCase("DB2") || databaseType.equalsIgnoreCase("Informix")
						|| databaseType.equalsIgnoreCase("DM6") || databaseType.equalsIgnoreCase("DM7")
						|| databaseType.equalsIgnoreCase("Kingbase"))
				{
					spdatabase.setName("javadatabase");
				}
				else if (databaseType.equalsIgnoreCase("MongoDB"))
				{
					spdatabase.setName("spmongodb");
				}
				else if (databaseType.equalsIgnoreCase("Hbase"))
				{
					spdatabase.setName("sphbase");
				}
				else if (databaseType.equalsIgnoreCase("Redis"))
				{
					spdatabase.setName("rdsdatabase");
				}
				if (databaseType.equalsIgnoreCase("Oracle"))
				{
					databaseName = null != objects[4] ? objects[4].toString() : "";
				}
				else
				{
					databaseName = null != objects[3] ? objects[3].toString() : "";
				}
				Element database = spdatabase.addElement("database");
				database.addAttribute("type", "ASSIGN");
				database.addElement("database_type").addText(databaseType);
				database.addElement("ip").addText(null != objects[1] ? objects[1].toString() : "");
				database.addElement("port").addText(null != objects[2] ? objects[2].toString() : "");
				database.addElement("domain").addText(null != objects[6] ? objects[6].toString() : "");
				database.addElement("username").addText(null != objects[4] ? objects[4].toString() : "");
				database.addElement("password").addText(null != objects[5] ? objects[5].toString() : "");
				database.addElement("share_username").addText(null != objects[17] ? objects[17].toString() : "");
				database.addElement("share_password").addText(null != objects[18] ? objects[18].toString() : "");
				database.addElement("database_name").addText(databaseName);
				database.addElement("oraclesid").addText(null != objects[9] ? objects[9].toString() : "");

				// 此处代表数据库分块扫描,每块最大扫描记录数,参考XML中配置固定为1000
				database.addElement("maxChunkSize").addText("1000");

				// 数据库百分比扫描，默认为100%
				String percentValue = "100";
				if ("1".equalsIgnoreCase(jobelement[15].toString()))
				{
					percentValue = (null != jobelement[16]) ? (jobelement[16].toString()) : "0";
				}
				database.addElement("percentage").addText(percentValue);

				// 主机资源ID
				String targetResId = objects[7].toString();

				// 查询资源下的所有模式或数据库
				List<Object[]> databaseOrSchemaNames = GenUtil
						.mapToListObj(jobScheduleDao.getDatabaseNameOrSchemaName(targetResId));
				if ("SqlServer".equalsIgnoreCase(databaseType) || "Oracle".equalsIgnoreCase(databaseType)
						|| "DB2".equalsIgnoreCase(databaseType) || "Informix".equalsIgnoreCase(databaseType)
						|| "DM6".equalsIgnoreCase(databaseType) || "Kingbase".equalsIgnoreCase(databaseType))
				{
					if (null != databaseOrSchemaNames && !databaseOrSchemaNames.isEmpty())
					{
						for (Object[] databaseOrSchemaName : databaseOrSchemaNames)
						{
							Element schemaNameEle = database.addElement("schemaName");
							schemaNameEle.addAttribute("name", databaseOrSchemaName[0].toString());
							schemaNameEle.addAttribute("scanAll", databaseOrSchemaName[1].toString());
							Element filter = schemaNameEle.addElement("filter");

								getDatabaseOrSchemaNameNode(jobId, targetResId, schemaNameEle, filter, databaseType,
										databaseOrSchemaName[0].toString());
							// 文件修改时间
							if (null != jobelement[5] && "1".equals(jobelement[5].toString()))
							{
								if ("BETWEEN".equalsIgnoreCase(jobelement[6].toString()))
								{
									Element modifyTime = filter.addElement("modifyTime");
									modifyTime.addElement("min").addText(jobelement[7].toString());
									modifyTime.addElement("max").addText(jobelement[8].toString());
								}
								else if ("MORE_THAN".equalsIgnoreCase(jobelement[6].toString()))
								{
									filter.addElement("minAge").addText(jobelement[9].toString());
								}
								else if ("WITHIN".equalsIgnoreCase(jobelement[6].toString()))
								{
									filter.addElement("maxAge").addText(jobelement[10].toString());
								}
							}
						}
					}
				}
				else if ("MySql".equalsIgnoreCase(databaseType) || "DM7".equalsIgnoreCase(databaseType)
						|| "MongoDB".equalsIgnoreCase(databaseType))
				{
					if (null != databaseOrSchemaNames && !databaseOrSchemaNames.isEmpty())
					{
						for (Object[] databaseOrSchemaName : databaseOrSchemaNames)
						{
							Element databaseNameEle = database.addElement("databaseName");
							databaseNameEle.addAttribute("name", databaseOrSchemaName[0].toString());
							databaseNameEle.addAttribute("scanAll", databaseOrSchemaName[1].toString());
							Element filter = databaseNameEle.addElement("filter");

							getDatabaseOrSchemaNameNode(jobId, targetResId, databaseNameEle, filter, databaseType,
									databaseOrSchemaName[0].toString());

							// 文件修改时间
							if (null != jobelement[5] && "1".equals(jobelement[5].toString()))
							{
								if ("BETWEEN".equalsIgnoreCase(jobelement[6].toString()))
								{
									Element modifyTime = filter.addElement("modifyTime");
									modifyTime.addElement("min").addText(jobelement[7].toString());
									modifyTime.addElement("max").addText(jobelement[8].toString());
								}
								else if ("MORE_THAN".equalsIgnoreCase(jobelement[6].toString()))
								{
									filter.addElement("minAge").addText(jobelement[9].toString());
								}
								else if ("WITHIN".equalsIgnoreCase(jobelement[6].toString()))
								{
									filter.addElement("maxAge").addText(jobelement[10].toString());
								}
							}
						}
					}
				}
				else
				{
					// 数据库扫描目标、Lotus扫描目标、Exchange扫描目标包含资源详细信息
					List<String> tables = jobScheduleDao.getDbLoTusExchageTargetDetail(targetResId);
					for (String tableName : tables)
					{
						if (tableName != null)
						{
							Element table = database.addElement("table");
							table.addAttribute("type", "ALL");
							table.addElement("name").addText(tableName);
						}

					}
					Element filter = database.addElement("filter");
					// if ("1".equals(jobelement[12].toString()))
					// {
					// insertFileTypeFilter(filter, targetIncludeTables, "", 2);
					// }

					// 文件修改时间
					if (null != jobelement[5] && "1".equals(jobelement[5].toString()))
					{
						if ("BETWEEN".equalsIgnoreCase(jobelement[6].toString()))
						{
							Element modifyTime = filter.addElement("modifyTime");
							modifyTime.addElement("min").addText(jobelement[7].toString());
							modifyTime.addElement("max").addText(jobelement[8].toString());
						}
						else if ("MORE_THAN".equalsIgnoreCase(jobelement[6].toString()))
						{
							filter.addElement("minAge").addText(jobelement[9].toString());
						}
						else if ("WITHIN".equalsIgnoreCase(jobelement[6].toString()))
						{
							filter.addElement("maxAge").addText(jobelement[10].toString());
						}
					}
				}

			}

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");
			log.debug("部署生成数据库扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);

		}

	}

	/**
	 * 生成文件共享任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 * @throws ParseException
	 */
	private void generateFileSystemScanXML(List<JobDeployObj> jobDocumentList, List<String> ids) throws ParseException
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getFileDiscoveryTaskInfo(ids));
		for (Object[] jobelement : joblist)
		{
			// 策略ID
			String jobId = jobelement[0].toString();
			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");

			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[8].toString()))
			{
				timerAge = (null != jobelement[9]) ? (jobelement[9].toString()) : "";
			}

			Element jobelementresult = jobele.addAttribute("type", Contants.DISCOVERY).addAttribute("id", jobId)
					.addAttribute("name", jobelement[6].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", Contants.SCAN_TASK_DISCOVER).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[4] ? jobelement[4].toString() : "")
					.addAttribute("orgName", null != jobelement[5] ? jobelement[5].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[7].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");
			Element jobworker = workerNode.addElement("crawler");

			Element jobSetting = jobworker.addElement("settings");

			// 带宽
			Element bandSettings = jobSetting.addElement("bandSettings");

			// 闲时带宽（默认不启动）
			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			// 忙时带宽
			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[10].toString().equals("") ? "0" : jobelement[10].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[13].toString().equals("") ? "0" : jobelement[13].toString());
			settingBusy.addAttribute("start", null != jobelement[11] ? jobelement[11].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[12] ? jobelement[12].toString() : "");

			// settings- credentials
			Element credentials = jobSetting.addElement("credentials");
			credentials.addElement("domain").addText("");
			credentials.addElement("username").addText("");
			credentials.addElement("password").addText("");
			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			// 获取主机信息
			List<Object[]> targetInfo = GenUtil.mapToListObj(jobScheduleDao.getFileTarInfoByJobId(jobId));
			Element jobnetwork = jobworker.addElement("spnetwork");

			Element subnetwork = jobnetwork.addElement("subnetwork");

			Element subCredentials = subnetwork.addElement("credentials");
			subCredentials.addElement("domain").addText(targetInfo.get(0)[7].toString());
			subCredentials.addElement("username").addText(targetInfo.get(0)[4].toString());
			subCredentials.addElement("password").addText(targetInfo.get(0)[5].toString());

			Element shares = subnetwork.addElement("shares");
			Element specified = shares.addElement("specified");

			// 获取选择的共享文件路径
			List<String> shareFolders = jobScheduleDao.getFileSharePath(jobId);
			for (String fullPath : shareFolders)
			{
				specified.addElement("path").addText(fullPath);
			}

			Element nmPorts = shares.addElement("nmPorts");
			nmPorts.addElement("port").addText("139");
			nmPorts.addElement("port").addText("445");
			shares.addElement("scanMethod").addText("TCP");

			// subnetwork-include
			Element include = subnetwork.addElement("include");
			include.addElement("ip").addText(targetInfo.get(0)[1].toString());
			// subnetwork-exclude
			subnetwork.addElement("exclude");

			Element jobfilterelement = jobworker.addElement("filter");
			// isFileNameEnabled 是否启动文件类型过滤
			if ("1".equals(jobelement[14].toString()))
			{
				// 根据策略ID查询包含或排除的文件类型
				List<String> includeFileType = jobScheduleDao.getFileIncludeFileType(jobId);
				List<String> excludeFileType = jobScheduleDao.getFileExcludeFileType(jobId);
				insertFileTypeFilter(jobfilterelement, includeFileType, excludeFileType, 0);
			}
			// isLargerThanEnabled 是否启用文件大小的上限
			if ("1".equals(jobelement[15].toString()))
			{
				jobfilterelement.addElement("maxSize").addText(jobelement[16].toString());
			}
			// isSmallerThanEnalbed 是否启用文件大小的下限
			if ("1".equals(jobelement[17].toString()))
			{
				jobfilterelement.addElement("minSize").addText(jobelement[18].toString());
			}

			// isFileAgeEnabled是否启用文件修改时间过滤
			if ("1".equals(jobelement[19].toString()))
			{
				if ("BETWEEN".equalsIgnoreCase(jobelement[20].toString()))
				{
					Element modifyTime = jobfilterelement.addElement("modifyTime");

					modifyTime.addElement("min").addText(jobelement[21].toString());
					modifyTime.addElement("max").addText(jobelement[22].toString());

				}
				else if ("MORE_THAN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("minAge").addText(jobelement[23].toString());

				}
				else if ("WITHIN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("maxAge").addText(jobelement[24].toString());
				}

			}

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");

			log.debug("部署生成文件共享扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);

		}
	}

	/**
	 * 生成文件Ftp任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 */
	private void generateFtpScanXml(List<JobDeployObj> jobDocumentList, List<String> ids)
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getFtpDiscoveryTaskInfo(ids));
		for (Object[] jobelement : joblist)
		{
			String jobId = jobelement[0].toString();

			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");

			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[8].toString()))
			{
				timerAge = (null != jobelement[9]) ? (jobelement[9].toString()) : "1";
			}

			Element jobelementresult = jobele.addAttribute("type", Contants.DISCOVERY).addAttribute("id", jobId)
					.addAttribute("name", jobelement[6].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", Contants.FTP_SCAN_TASK_DISCOVER).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[4] ? jobelement[4].toString() : "")
					.addAttribute("orgName", null != jobelement[5] ? jobelement[5].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[7].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");
			Element jobworker = workerNode.addElement("crawler");

			Element jobSetting = jobworker.addElement("settings");

			Element credentials = jobSetting.addElement("credentials");

			Element bandSettings = jobSetting.addElement("bandSettings");

			// 闲时带宽（默认不启动）
			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[10].toString().equals("") ? "0" : jobelement[10].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[13].toString().equals("") ? "0" : jobelement[13].toString());
			settingBusy.addAttribute("start", null != jobelement[11] ? jobelement[11].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[12] ? jobelement[12].toString() : "");

			credentials.addElement("domain").addText("");
			credentials.addElement("username").addText("");
			credentials.addElement("password").addText("");
			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			Element spftpwork = jobworker.addElement("spftpwork");

			// 获取主机信息
			List<Object[]> targetInfo = GenUtil.mapToListObj(jobScheduleDao.getFileTarInfoByJobId(jobId));
			if (null != targetInfo && !targetInfo.isEmpty())
			{
				String targetIp = null != targetInfo.get(0)[1] ? targetInfo.get(0)[1].toString() : "";
				String targetUsername = null != targetInfo.get(0)[4] ? targetInfo.get(0)[4].toString() : "";
				String targetPassword = null != targetInfo.get(0)[5] ? targetInfo.get(0)[5].toString() : "";
				String targetDomain = null != targetInfo.get(0)[7] ? targetInfo.get(0)[7].toString() : "";
				String targetPort = null != targetInfo.get(0)[2] ? targetInfo.get(0)[2].toString() : "";

				Element subnetwork = spftpwork.addElement("subnetwork");
				Element credentialsTar = subnetwork.addElement("credentials");
				credentialsTar.addElement("username").addText(targetUsername);
				credentialsTar.addElement("password").addText(targetPassword);
				credentialsTar.addElement("port").addText(targetPort);
				credentialsTar.addElement("domain").addText(targetDomain);

				Element include = subnetwork.addElement("include");
				Element exclude = subnetwork.addElement("exclude");
				include.addElement("ip").addText(targetIp);

				Element shares = subnetwork.addElement("shares");
				Element specified = shares.addElement("specified");

				// 获取主机资源共享路径
				List<String> shareFolders = jobScheduleDao.getFileSharePath(jobId);
				for (String fullPath : shareFolders)
				{
					specified.addElement("path").addText(fullPath);
				}

			}

			Element jobfilterelement = jobworker.addElement("filter");
			// isFileNameEnabled 是否启动文件类型过滤
			if ("1".equals(jobelement[14].toString()))
			{
				// 根据策略ID查询包含或排除的文件类型
				List<String> includeFileType = jobScheduleDao.getFileIncludeFileType(jobId);
				List<String> excludeFileType = jobScheduleDao.getFileExcludeFileType(jobId);
				insertFileTypeFilter(jobfilterelement, includeFileType, excludeFileType, 0);
			}
			// isLargerThanEnabled 是否启用文件大小的上限
			if ("1".equals(jobelement[15].toString()))
			{
				jobfilterelement.addElement("maxSize").addText(jobelement[16].toString());
			}
			// isSmallerThanEnalbed 是否启用文件大小的下限
			if ("1".equals(jobelement[17].toString()))
			{
				jobfilterelement.addElement("minSize").addText(jobelement[18].toString());
			}

			// isFileAgeEnabled是否启用文件修改时间过滤
			if ("1".equals(jobelement[19].toString()))
			{
				if ("BETWEEN".equalsIgnoreCase(jobelement[20].toString()))
				{
					Element modifyTime = jobfilterelement.addElement("modifyTime");

					modifyTime.addElement("min").addText(jobelement[21].toString());
					modifyTime.addElement("max").addText(jobelement[22].toString());

				}
				else if ("MORE_THAN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("minAge").addText(jobelement[23].toString());

				}
				else if ("WITHIN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("maxAge").addText(jobelement[24].toString());
				}

			}

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");

			log.debug("部署生成FTP扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);

		}
	}

	/**
	 * 生成文件Linux任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 */
	private void generateSftpScanXml(List<JobDeployObj> jobDocumentList, List<String> ids)
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getSftpDiscoveryTaskInfo(ids));

		for (Object[] jobelement : joblist)
		{

			String jobId = jobelement[0].toString();

			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");

			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[8].toString()))
			{
				timerAge = (null != jobelement[9]) ? (jobelement[9].toString()) : "1";
			}

			Element jobelementresult = jobele.addAttribute("type", Contants.DISCOVERY).addAttribute("id", jobId)
					.addAttribute("name", jobelement[6].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", Contants.SFTP_SCAN_TASK_DISCOVER).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[4] ? jobelement[4].toString() : "")
					.addAttribute("orgName", null != jobelement[5] ? jobelement[5].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[7].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");
			Element jobworker = workerNode.addElement("crawler");

			Element jobSetting = jobworker.addElement("settings");
			Element credentials = jobSetting.addElement("credentials");
			Element bandSettings = jobSetting.addElement("bandSettings");

			// 闲时带宽（默认不启动）
			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[10].toString().equals("") ? "0" : jobelement[10].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[13].toString().equals("") ? "0" : jobelement[13].toString());
			settingBusy.addAttribute("start", null != jobelement[11] ? jobelement[11].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[12] ? jobelement[12].toString() : "");

			credentials.addElement("domain").addText("");
			credentials.addElement("username").addText("");
			credentials.addElement("password").addText("");
			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			Element spftpwork = jobworker.addElement("spsftpwork");

			// 获取主机信息
			List<Object[]> targetInfo = GenUtil.mapToListObj(jobScheduleDao.getFileTarInfoByJobId(jobId));
			if (null != targetInfo && !targetInfo.isEmpty())
			{
				String targetIp = null != targetInfo.get(0)[1] ? targetInfo.get(0)[1].toString() : "";
				String targetUsername = null != targetInfo.get(0)[4] ? targetInfo.get(0)[4].toString() : "";
				String targetPassword = null != targetInfo.get(0)[5] ? targetInfo.get(0)[5].toString() : "";
				String targetDomain = null != targetInfo.get(0)[7] ? targetInfo.get(0)[7].toString() : "";
				String targetPort = null != targetInfo.get(0)[2] ? targetInfo.get(0)[2].toString() : "";
				String targetKeyType = null != targetInfo.get(0)[10] ? targetInfo.get(0)[10].toString() : "";
				String targetPublicKey = null != targetInfo.get(0)[11] ? targetInfo.get(0)[11].toString() : "";

				Element subnetwork = spftpwork.addElement("subnetwork");
				Element credentialsTar = subnetwork.addElement("credentials");
				credentialsTar.addElement("username").addText(targetUsername);
				if (StringUtils.isNotEmpty(targetPassword))
				{
					credentialsTar.addElement("password").addText(targetPassword);
				}
				else
				{
					credentialsTar.addElement("keyType").addText(targetKeyType);
					credentialsTar.addElement("publicKey").addText(targetPublicKey);
					credentialsTar.addElement("password").addText(targetPassword);
				}
				credentialsTar.addElement("port").addText(targetPort);
				credentialsTar.addElement("domain").addText(targetDomain);

				Element include = subnetwork.addElement("include");
				Element exclude = subnetwork.addElement("exclude");
				include.addElement("ip").addText(targetIp);

				Element shares = subnetwork.addElement("shares");
				Element specified = shares.addElement("specified");

				// 获取主机资源共享路径
				List<String> shareFolders = jobScheduleDao.getFileSharePath(jobId);
				for (String fullPath : shareFolders)
				{
					specified.addElement("path").addText(fullPath);
				}
			}

			// filter节点
			Element jobfilterelement = jobworker.addElement("filter");
			// isFileNameEnabled 是否启动文件类型过滤
			if ("1".equals(jobelement[14].toString()))
			{
				// 根据策略ID查询包含或排除的文件类型
				List<String> includeFileType = jobScheduleDao.getFileIncludeFileType(jobId);
				List<String> excludeFileType = jobScheduleDao.getFileExcludeFileType(jobId);
				insertFileTypeFilter(jobfilterelement, includeFileType, excludeFileType, 0);
			}
			// isLargerThanEnabled 是否启用文件大小的上限
			if ("1".equals(jobelement[15].toString()))
			{
				jobfilterelement.addElement("maxSize").addText(jobelement[16].toString());
			}
			// isSmallerThanEnalbed 是否启用文件大小的下限
			if ("1".equals(jobelement[17].toString()))
			{
				jobfilterelement.addElement("minSize").addText(jobelement[18].toString());
			}

			// isFileAgeEnabled是否启用文件修改时间过滤
			if ("1".equals(jobelement[19].toString()))
			{
				if ("BETWEEN".equalsIgnoreCase(jobelement[20].toString()))
				{
					Element modifyTime = jobfilterelement.addElement("modifyTime");

					modifyTime.addElement("min").addText(jobelement[21].toString());
					modifyTime.addElement("max").addText(jobelement[22].toString());

				}
				else if ("MORE_THAN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("minAge").addText(jobelement[23].toString());

				}
				else if ("WITHIN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("maxAge").addText(jobelement[24].toString());
				}

			}

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");

			log.debug("部署生成Linux扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);

		}

	}

	/**
	 * 生成文件SharePoint任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 * @throws ParseException
	 */
	private void generateSharePointScanXML(List<JobDeployObj> jobDocumentList, List<String> ids) throws ParseException
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getSharePointDiscoveryTaskInfo(ids));

		for (Object[] jobelement : joblist)
		{

			String jobId = jobelement[0].toString();
			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");

			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[8].toString()))
			{
				timerAge = (null != jobelement[9]) ? (jobelement[9].toString()) : "1";
			}

			Element jobelementresult = jobele.addAttribute("type", Contants.DISCOVERY).addAttribute("id", jobId)
					.addAttribute("name", jobelement[6].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", Contants.SHAREPOINT_SCAN_TASK_DISCOVER).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[4] ? jobelement[4].toString() : "")
					.addAttribute("orgName", null != jobelement[5] ? jobelement[5].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[7].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");
			Element jobcrawler = workerNode.addElement("crawler");

			Element jobSetting = jobcrawler.addElement("settings");
			Element jobSharePoint = jobcrawler.addElement("sharepoint");
			Element credentials = jobSetting.addElement("credentials");
			Element bandSettings = jobSetting.addElement("bandSettings");

			// 闲时带宽（默认不启动）
			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[10].toString().equals("") ? "0" : jobelement[10].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[13].toString().equals("") ? "0" : jobelement[13].toString());
			settingBusy.addAttribute("start", null != jobelement[11] ? jobelement[11].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[12] ? jobelement[12].toString() : "");

			List<Object[]> targetInfoList = GenUtil.mapToListObj(jobScheduleDao.getFileTarInfoByJobId(jobId));

			// settings节点
			credentials.addElement("domain")
					.addText(null != targetInfoList.get(0)[4] ? targetInfoList.get(0)[7].toString() : "");
			credentials.addElement("username").addText(targetInfoList.get(0)[4].toString());
			credentials.addElement("password").addText(targetInfoList.get(0)[5].toString());
			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			// sharepoint节点
			jobSharePoint.addElement("baseURL")
					.addText(targetInfoList.get(0)[3].toString().endsWith("/")
							? targetInfoList.get(0)[3].toString().substring(0,
									targetInfoList.get(0)[3].toString().length() - 1)
							: targetInfoList.get(0)[3].toString());
			Element item = jobSharePoint.addElement("item").addAttribute("type", "SITE").addAttribute("name",
					targetInfoList.get(0)[3].toString().endsWith("/")
							? targetInfoList.get(0)[3].toString().substring(0,
									targetInfoList.get(0)[3].toString().length() - 1)
							: targetInfoList.get(0)[3].toString());
			Element propertyEle = item.addElement("metadata").addElement("property");
			propertyEle.addElement("name").addText("site");
			propertyEle.addElement("type").addText("string");
			propertyEle.addElement("value")
					.addText(targetInfoList.get(0)[3].toString().endsWith("/")
							? targetInfoList.get(0)[3].toString().substring(0,
									targetInfoList.get(0)[3].toString().length() - 1)
							: targetInfoList.get(0)[3].toString());

			// filter节点
			Element jobfilterelement = jobcrawler.addElement("filter");
			// isFileNameEnabled 是否启动文件类型过滤
			if ("1".equals(jobelement[14].toString()))
			{
				// 根据策略ID查询包含或排除的文件类型
				List<String> includeFileType = jobScheduleDao.getFileIncludeFileType(jobId);
				List<String> excludeFileType = jobScheduleDao.getFileExcludeFileType(jobId);
				insertFileTypeFilter(jobfilterelement, includeFileType, excludeFileType, 0);
			}
			// isLargerThanEnabled 是否启用文件大小的上限
			if ("1".equals(jobelement[15].toString()))
			{
				jobfilterelement.addElement("maxSize").addText(jobelement[16].toString());
			}
			// isSmallerThanEnalbed 是否启用文件大小的下限
			if ("1".equals(jobelement[17].toString()))
			{
				jobfilterelement.addElement("minSize").addText(jobelement[18].toString());
			}

			// isFileAgeEnabled是否启用文件修改时间过滤
			if ("1".equals(jobelement[19].toString()))
			{
				if ("BETWEEN".equalsIgnoreCase(jobelement[20].toString()))
				{
					Element modifyTime = jobfilterelement.addElement("modifyTime");

					modifyTime.addElement("min").addText(jobelement[21].toString());
					modifyTime.addElement("max").addText(jobelement[22].toString());

				}
				else if ("MORE_THAN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("minAge").addText(jobelement[23].toString());

				}
				else if ("WITHIN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("maxAge").addText(jobelement[24].toString());
				}

			}

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");

			log.debug("部署生成SharePoint扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);

		}
	}

	/**
	 * 生成文件Lotus任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 * @throws ParseException
	 */
	private void generateLotusXML(List<JobDeployObj> jobDocumentList, List<String> ids) throws ParseException
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getLotusDiscoveryTaskInfo(ids));

		for (Object[] jobelement : joblist)
		{
			String jobId = jobelement[0].toString();
			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");

			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[8].toString()))
			{
				timerAge = (null != jobelement[9]) ? (jobelement[9].toString()) : "1";
			}

			Element jobelementresult = jobele.addAttribute("type", Contants.DISCOVERY).addAttribute("id", jobId)
					.addAttribute("name", jobelement[6].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", Contants.LOTUS_SCAN_TASK_DISCOVER).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[4] ? jobelement[4].toString() : "")
					.addAttribute("orgName", null != jobelement[5] ? jobelement[5].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[7].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");
			Element jobworker = workerNode.addElement("crawler");

			// crawler 节点下的settings节点
			Element jobSetting = jobworker.addElement("settings");
			Element bandSettings = jobSetting.addElement("bandSettings");

			// 闲时带宽（默认不启动）
			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[10].toString().equals("") ? "0" : jobelement[10].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[13].toString().equals("") ? "0" : jobelement[13].toString());
			settingBusy.addAttribute("start", null != jobelement[11] ? jobelement[11].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[12] ? jobelement[12].toString() : "");

			Element jobCredentials = jobSetting.addElement("credentials");

			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			List<Object[]> lotusList = GenUtil.mapToListObj(jobScheduleDao.getFileTarInfoByJobId(jobId));

			String targetUsername = null != lotusList.get(0)[4] ? lotusList.get(0)[4].toString() : "";
			String targetPassword = null != lotusList.get(0)[5] ? lotusList.get(0)[5].toString() : "";
			String targetPort = null != lotusList.get(0)[2] ? lotusList.get(0)[2].toString() : "";
			String targetDomain = null != lotusList.get(0)[7] ? lotusList.get(0)[7].toString() : "";

			jobCredentials.addElement("domain").addText(targetDomain);
			jobCredentials.addElement("username").addText(targetUsername);
			jobCredentials.addElement("password").addText(targetPassword);
			jobCredentials.addElement("port").addText(targetPort);
			// crawler 节点下的lotus节点
			Element jobLotusElement = jobworker.addElement("lotus");
			jobLotusElement.addAttribute("type", null != lotusList.get(0)[15] ? lotusList.get(0)[15].toString() : "");
			jobLotusElement.addElement("include").addElement("ip").addText(lotusList.get(0)[1].toString());
			Element userInfoElement = jobLotusElement.addElement("userInfo");

			List<String> lotusDetail = jobScheduleDao.getDbLoTusExchageTargetDetail(lotusList.get(0)[0].toString());
			StringBuffer userListValue = new StringBuffer();
			if (null != lotusDetail && lotusDetail.size() > 0)
			{
				for (int i = 0; i < lotusDetail.size(); i++)
				{
					if (i == (lotusDetail.size() - 1))
					{
						userListValue.append(lotusDetail.get(i));
					}
					else
					{
						userListValue.append(lotusDetail.get(i)).append(";");
					}
				}
			}

			userInfoElement.addElement("userList").addText(userListValue.toString());

			userInfoElement.addElement("scanAttr").addText("True");

			Element jobfilterelement = jobworker.addElement("filter");

			// isFileNameEnabled 是否启动文件类型过滤
			if ("1".equals(jobelement[14].toString()))
			{
				// 根据策略ID查询包含或排除的文件类型
				List<String> includeFileType = jobScheduleDao.getFileIncludeFileType(jobId);
				List<String> excludeFileType = jobScheduleDao.getFileExcludeFileType(jobId);
				insertFileTypeFilter(jobfilterelement, includeFileType, excludeFileType, 0);
			}
			// isLargerThanEnabled 是否启用文件大小的上限
			if ("1".equals(jobelement[15].toString()))
			{
				jobfilterelement.addElement("maxSize").addText(jobelement[16].toString());
			}
			// isSmallerThanEnalbed 是否启用文件大小的下限
			if ("1".equals(jobelement[17].toString()))
			{
				jobfilterelement.addElement("minSize").addText(jobelement[18].toString());
			}

			// isFileAgeEnabled是否启用文件修改时间过滤
			if ("1".equals(jobelement[19].toString()))
			{
				if ("BETWEEN".equalsIgnoreCase(jobelement[20].toString()))
				{
					Element modifyTime = jobfilterelement.addElement("modifyTime");

					modifyTime.addElement("min").addText(jobelement[21].toString());
					modifyTime.addElement("max").addText(jobelement[22].toString());

				}
				else if ("MORE_THAN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("minAge").addText(jobelement[23].toString());

				}
				else if ("WITHIN".equalsIgnoreCase(jobelement[20].toString()))
				{

					jobfilterelement.addElement("maxAge").addText(jobelement[24].toString());
				}

			}

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");
			log.debug("部署生成Lotus扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);
		}
	}

	/**
	 * 生成文件Exchange任务xml文件
	 * @param jobDocumentList
	 * @param ids
	 * @throws ParseException
	 */
	private void generateExchangeScanXML(List<JobDeployObj> jobDocumentList, List<String> ids) throws ParseException
	{
		List<Object[]> joblist = GenUtil.mapToListObj(jobScheduleDao.getExchangeDiscoveryTaskInfo(ids));
		for (Object[] jobelement : joblist)
		{
			String jobId = jobelement[0].toString();
			Document document = DocumentHelper.createDocument();
			Element jobele = document.addElement("job");
			// 策略运行时间扫描
			String timerAge = "1";
			if ("1".equalsIgnoreCase(jobelement[8].toString()))
			{
				timerAge = (null != jobelement[9]) ? (jobelement[9].toString()) : "1";
			}

			Element jobelementresult = jobele.addAttribute("type", Contants.DISCOVERY).addAttribute("id", jobId)
					.addAttribute("name", jobelement[6].toString()).addAttribute("altID", jobId)
					.addAttribute("childType", Contants.EXCHANGE_SCAN_TASK_DISCOVER).addAttribute("timer", timerAge)
					.addAttribute("orgId", null != jobelement[4] ? jobelement[4].toString() : "")
					.addAttribute("orgName", null != jobelement[5] ? jobelement[5].toString() : "");

			// 插入scheduler节点
			insertSchedulerElement(jobelement[7].toString(), jobelement[3].toString(), jobelementresult, "1");

			Element workerNode = jobelementresult.addElement("worker");
			Element jobcrawler = workerNode.addElement("crawler");

			Element jobSetting = jobcrawler.addElement("settings");
			Element jobExchange = jobcrawler.addElement("exchange");
			Element credentials = jobSetting.addElement("credentials");

			// 闲时带宽（默认不启动）
			Element bandSettings = jobSetting.addElement("bandSettings");

			Element freeBand = bandSettings.addElement("freeBand");
			freeBand.addElement("isEnabled").addText("0");
			Element setting = freeBand.addElement("setting").addText("1");
			setting.addAttribute("start", "");
			setting.addAttribute("end", "");

			Element busyBand = bandSettings.addElement("busyBand");
			busyBand.addElement("isEnabled")
					.addText(jobelement[10].toString().equals("") ? "0" : jobelement[10].toString());
			Element settingBusy = busyBand.addElement("setting")
					.addText(jobelement[13].toString().equals("") ? "0" : jobelement[13].toString());
			settingBusy.addAttribute("start", null != jobelement[11] ? jobelement[11].toString() : "");
			settingBusy.addAttribute("end", null != jobelement[12] ? jobelement[12].toString() : "");

			// settings节点 获取主机资源信息
			List<Object[]> targetInfoList = GenUtil.mapToListObj(jobScheduleDao.getFileTarInfoByJobId(jobId));
			credentials.addElement("domain").addText(targetInfoList.get(0)[7].toString());
			credentials.addElement("username").addText(targetInfoList.get(0)[4].toString());
			credentials.addElement("password").addText(targetInfoList.get(0)[5].toString());
			jobSetting.addElement("fullScan").addText(Contants.ON_POLICY_AND_FP_VERSION_UPDATE);

			List mailAddress = jobScheduleDao.getDbLoTusExchageTargetDetail(targetInfoList.get(0)[0].toString());

			// exchange节点
			jobExchange.addElement("useSSL")
					.addText(("1".equals(targetInfoList.get(0)[13].toString())) ? "true" : "false");
			jobExchange.addElement("useImportedServers").addText("true");
			Element include = jobExchange.addElement("include");

			// 去重
			Set mailAddressSet = new HashSet(mailAddress);
			for (Iterator iterator = mailAddressSet.iterator(); iterator.hasNext();)
			{
				String addr = (String) iterator.next();
				include.addElement("id").addText(addr);
			}

			Element additionalServers = jobExchange.addElement("additionalServers");
			additionalServers.addAttribute("edition", targetInfoList.get(0)[14].toString());
			additionalServers.addElement("server").addText(targetInfoList.get(0)[1].toString());
			additionalServers.addElement("server").addText(targetInfoList.get(0)[6].toString());

			// filter节点
			Element jobfilterelement = jobcrawler.addElement("filter");
			jobfilterelement.addElement("include").addText("(?:(?:^.*$))");

			workerNode.addElement("action").addElement("EncryptCognition").addElement("sid").addText("234995846");

			log.debug("部署生成Exchange扫描策略成功!");

			JobDeployObj jobDeployObj = new JobDeployObj();
			jobDeployObj.setJobDocStr(document.asXML().toString());
			jobDeployObj.setJobId(jobId);
			jobDocumentList.add(jobDeployObj);

		}
	}

	/**
	 * jobelement 查询数据库返回的记录 jobid 任务id
	 * @param scheduleType
	 * @param scheduleId
	 * @param jobelementresult
	 * @param isEnable
	 */
	private void insertSchedulerElement(String scheduleType, String scheduleId, Element jobelementresult,
			String isEnable)
	{
		if ("WEEKLY".equals(scheduleType) || "DAILY".equals(scheduleType))
		{
			List<Object[]> jobtimelist = GenUtil.mapToListObj(jobScheduleDao.getJbTime(scheduleId));
			if ("WEEKLY".equals(jobtimelist.get(0)[0].toString()))
			{
				Element schedulemesg = jobelementresult.addElement("schedule")
						.addAttribute("recurType", jobtimelist.get(0)[0].toString()).addAttribute("isEnabled", isEnable)
						.addElement("recurring").addElement("availableTimeSlots");
				sortTimeOfDay("0", schedulemesg, jobtimelist);
				sortTimeOfDay("1", schedulemesg, jobtimelist);
				sortTimeOfDay("2", schedulemesg, jobtimelist);
				sortTimeOfDay("3", schedulemesg, jobtimelist);
				sortTimeOfDay("4", schedulemesg, jobtimelist);
				sortTimeOfDay("5", schedulemesg, jobtimelist);
				sortTimeOfDay("6", schedulemesg, jobtimelist);
			}
			else if ("DAILY".equals(jobtimelist.get(0)[0].toString()))
			{
				Element schedulemesg = jobelementresult.addElement("schedule")
						.addAttribute("recurType", jobtimelist.get(0)[0].toString()).addAttribute("isEnabled", isEnable)
						.addElement("recurring").addElement("availableTimeSlots");
				schedulemesg.addElement("timeSlot").addAttribute("endTime", jobtimelist.get(0)[6].toString())
						.addAttribute("startTime", jobtimelist.get(0)[1].toString());
			}
		}
		if ("CONTINUOUSLY".equals(scheduleType) || "ONCE".equals(scheduleType) || "INTERVAL".equals(scheduleType))
		{
			// 持续执行和一次
			List<Object[]> joboncelist = GenUtil.mapToListObj(jobScheduleDao.getJobOnce(scheduleId));
			if (0 != joboncelist.size())
			{
				if ("CONTINUOUSLY".equals(joboncelist.get(0)[0].toString()))
				{
					Integer continuousSchGap = Integer.parseInt(joboncelist.get(0)[1].toString()) * 60;
					Element continuously = jobelementresult.addElement("schedule")
							.addAttribute("recurType", joboncelist.get(0)[0].toString())
							.addAttribute("isEnabled", isEnable)
							.addAttribute("continuousSchGap", continuousSchGap.toString());
					if ("0".equals(joboncelist.get(0)[2].toString()))
					{
						continuously.addElement("initialRecur").addAttribute("type", "NOW");
					}
					else
					{
						continuously.addElement("initialRecur").addAttribute("date", joboncelist.get(0)[3].toString())
								.addAttribute("type", "TIME").addAttribute("time", joboncelist.get(0)[4].toString());
					}
				}
				else if ("INTERVAL".equals(joboncelist.get(0)[0].toString()))
				{
					Element continuously = jobelementresult.addElement("schedule")
							.addAttribute("recurType", joboncelist.get(0)[0].toString())
							.addAttribute("isEnabled", isEnable)
							.addAttribute("intervalDays", joboncelist.get(0)[1].toString());
					if ("0".equals(joboncelist.get(0)[2].toString()))
					{
						continuously.addElement("initialRecur").addAttribute("type", "NOW");
					}
					else
					{
						continuously.addElement("initialRecur").addAttribute("date", joboncelist.get(0)[3].toString())
								.addAttribute("type", "TIME").addAttribute("time", joboncelist.get(0)[4].toString());
					}
				}
				else if ("ONCE".equals(joboncelist.get(0)[0].toString()))
				{
					Element onceElement = jobelementresult.addElement("schedule")
							.addAttribute("recurType", joboncelist.get(0)[0].toString())
							.addAttribute("isEnabled", isEnable);
					if ("0".equals(joboncelist.get(0)[2].toString()))
					{
						onceElement.addElement("initialRecur").addAttribute("type", "NOW");
					}
					else
					{
						onceElement.addElement("initialRecur").addAttribute("date", joboncelist.get(0)[3].toString())
								.addAttribute("type", "TIME").addAttribute("time", joboncelist.get(0)[4].toString());
					}
				}
			}

		}

	}

	private void sortTimeOfDay(String dayOfWeek, Element parentElement, List<Object[]> timeList)
	{
		List<Object[]> dayTimeList = new ArrayList<Object[]>();
		for (Object[] jobtimeele : timeList)
		{
			if (null != dayOfWeek && null != jobtimeele[5].toString() && !"".equals(jobtimeele[5].toString())
					&& dayOfWeek.equals(jobtimeele[5].toString()))
			{
				dayTimeList.add(jobtimeele);
			}
		}
		if (!dayTimeList.isEmpty())
		{
			parentElement.addElement("timeSlot").addAttribute("weekDay", dayTimeList.get(0)[5].toString())
					.addAttribute("endTime", dayTimeList.get(0)[6].toString())
					.addAttribute("startTime", dayTimeList.get(0)[1].toString());
		}
	}

	private void getDatabaseOrSchemaNameNode(String jobId, String targetResId, Element databaseOrSchemaNameEle,
			Element filter, String databaseType, String databaseName)
	{
		Attribute scanAll = databaseOrSchemaNameEle.attribute("scanAll");
		String scalAllValue = scanAll.getValue();
		if ("1".equals(scalAllValue))
		{
			List<SpColumnResInfo> columnResInfoList = discoveryTasksDao.getColumnResInfosByTargetId(targetResId);
			if (null != columnResInfoList && !columnResInfoList.isEmpty())
			{
				Map<String, List<SpColumnResInfo>> map = new HashMap<String, List<SpColumnResInfo>>();
				for (SpColumnResInfo columnResInfo : columnResInfoList)
				{
					String tableName = columnResInfo.getTableName();
					if (StringUtils.isNotEmpty(tableName) && tableName.startsWith(databaseName + ".")) // 排除掉该资源下非本库的数据
					{
						tableName = tableName.substring((databaseName + ".").length());
						if (map.containsKey(tableName))
						{
							map.get(tableName).add(columnResInfo);
						}
						else
						{
							List<SpColumnResInfo> list = new ArrayList<SpColumnResInfo>();
							list.add(columnResInfo);
							map.put(tableName, list);
						}
					}
				}
				if (!map.isEmpty())
				{
					Set<Map.Entry<String, List<SpColumnResInfo>>> entries = map.entrySet();
					for (Map.Entry<String, List<SpColumnResInfo>> entry : entries)
					{
						String tableName = entry.getKey();
						List<SpColumnResInfo> columnResInfos = entry.getValue();
						Element tableEle = databaseOrSchemaNameEle.addElement("table");
						tableEle.addAttribute("type", "ALL");
						tableEle.addElement("name").addText(tableName);
						if (databaseType.equalsIgnoreCase("MongoDB"))
						{
							tableEle.setName("collection");
						}

						if (null != columnResInfos && !columnResInfos.isEmpty())
						{
							for (SpColumnResInfo columnResInfo : columnResInfos)
							{
								Element column = tableEle.addElement("column");
								column.addAttribute("name", columnResInfo.getColumnName());
								Element shareSettings = column.addElement("share_setting");
								shareSettings.addElement("job_id").addText(jobId);
								shareSettings.addElement("share_type")
										.addText(null != columnResInfo.getType() ? columnResInfo.getType() : "");
								shareSettings.addElement("web_server")
										.addText(null != columnResInfo.getIp() ? columnResInfo.getIp() : "");
								shareSettings.addElement("username").addText(
										null != columnResInfo.getUsername() ? columnResInfo.getUsername() : "");
								shareSettings.addElement("keyType")
										.addText(null != columnResInfo.getKeyType() ? columnResInfo.getKeyType() : "");
								shareSettings.addElement("port")
										.addText(null != columnResInfo.getPort() ? columnResInfo.getPort() : "");
								shareSettings.addElement("publicKey").addText(
										null != columnResInfo.getPublickey() ? columnResInfo.getPublickey() : "");
								shareSettings.addElement("share")
										.addText(null != columnResInfo.getPath() ? columnResInfo.getPath() : "");
								shareSettings.addElement("password").addText(
										null != columnResInfo.getPassword() ? columnResInfo.getPassword() : "");
								shareSettings.addElement("domain")
										.addText(null != columnResInfo.getDomain() ? columnResInfo.getDomain() : "");
								shareSettings.addElement("path_Flag")
										.addText(null != columnResInfo.getPathRelaBType()
												? columnResInfo.getPathRelaBType()
												: "");
							}
						}
					}
				}
			}
		}
		else
		{
			Map<String, String> resIdAndDatabaseName = new HashMap<>();
			resIdAndDatabaseName.put("targetResId", targetResId);
			resIdAndDatabaseName.put("databaseName", databaseName);
			List<String> tables = jobScheduleDao.targetIncludeTables(resIdAndDatabaseName);
			if (null != tables && !tables.isEmpty())
			{
				for (String tableName : tables)
				{
					Element tableEle = databaseOrSchemaNameEle.addElement("table");
					tableEle.addAttribute("type", "ALL");
					tableEle.addElement("name").addText(tableName);
					if (StringUtils.isNotEmpty(targetResId))
					{
						List<SpColumnResInfo> columnResInfos = discoveryTasksDao
								.getColumnsByTargetIdAndTableName(targetResId, databaseName + "." + tableName);
						for (SpColumnResInfo columnResInfo : columnResInfos)
						{
							Element column = tableEle.addElement("column");
							column.addAttribute("name", columnResInfo.getColumnName());
							Element shareSettings = column.addElement("share_setting");
							shareSettings.addElement("job_id").addText(jobId);
							shareSettings.addElement("share_type")
									.addText(null != columnResInfo.getType() ? columnResInfo.getType() : "");
							shareSettings.addElement("web_server")
									.addText(null != columnResInfo.getIp() ? columnResInfo.getIp() : "");
							shareSettings.addElement("username")
									.addText(null != columnResInfo.getUsername() ? columnResInfo.getUsername() : "");
							shareSettings.addElement("keyType")
									.addText(null != columnResInfo.getKeyType() ? columnResInfo.getKeyType() : "");
							shareSettings.addElement("port")
									.addText(null != columnResInfo.getPort() ? columnResInfo.getPort() : "");
							shareSettings.addElement("publicKey")
									.addText(null != columnResInfo.getPublickey() ? columnResInfo.getPublickey() : "");
							shareSettings.addElement("share")
									.addText(null != columnResInfo.getPath() ? columnResInfo.getPath() : "");
							shareSettings.addElement("password")
									.addText(null != columnResInfo.getPassword() ? columnResInfo.getPassword() : "");
							shareSettings.addElement("domain")
									.addText(null != columnResInfo.getDomain() ? columnResInfo.getDomain() : "");
							shareSettings.addElement("path_Flag").addText(
									null != columnResInfo.getPathRelaBType() ? columnResInfo.getPathRelaBType() : "");
						}
					}
				}
			}
		}
	}

	private void insertFileTypeFilter(Element jobfilterelement, List filterincludelist, List filterexcludelist,
			int type)
	{
		Element includetab = null;
		Element excludetab = null;
		String addincludeTest = "";
		String addexcludeTest = "";
		// ---------------------------------处理包含文件类型---------------------------------//
		if (null != filterincludelist && filterincludelist.size() != 0)
		{
			// 数据库扫描做单独区分处理
			if (type != 2)
			{
				includetab = jobfilterelement.addElement("include").addText("(?:");
			}
			else
			{
				includetab = jobfilterelement.addElement("include").addText("(");
			}

			for (Object jobworkerfilterinclude : filterincludelist)
			{
				if (jobworkerfilterinclude.toString() != null && !"".equals(jobworkerfilterinclude.toString()))
				{
					// 高级资源和策略的文件类型查询的结果不一致，策略是每个类型一条记录，而高级资源则是多个类型一条记录，故此处做统一处理
					String[] fileTypes;
					if (jobworkerfilterinclude.toString().indexOf(";") != -1)
					{
						fileTypes = jobworkerfilterinclude.toString().split(";");
					}
					else
					{
						fileTypes = new String[1];
						fileTypes[0] = jobworkerfilterinclude.toString();
					}

					for (String fileType : fileTypes)
					{
						// +号转义
						fileType = fileType.replace("+", "\\+");
						if (type != 2)
						{
							addincludeTest = addincludeTest + "(?:^";
						}
						else
						{
							addincludeTest = addincludeTest + "";
						}
						if (type == 0)
						{
							addincludeTest += ".*\\" + fileType.substring(1, fileType.length()) + "$)|";
						}
						else if (type == 1)
						{
							addincludeTest += ".*" + fileType + "$)|";
						}
						else if (type == 2)
						{
							addincludeTest += fileType + "|";
						}
					}
				}
			}
			if (filterincludelist.get(0).toString().length() != 0)
			{
				addincludeTest = addincludeTest.substring(0, addincludeTest.lastIndexOf("|"));
				addincludeTest = addincludeTest + "";
				includetab = includetab.addText(addincludeTest);
				includetab.addText(")");
			}
		}

		// ---------------------------------处理排除文件类型---------------------------------//
		if (null != filterexcludelist && filterexcludelist.size() != 0)
		{
			if (type != 2)
			{
				excludetab = jobfilterelement.addElement("exclude").addText("(?:");
			}
			else
			{
				excludetab = jobfilterelement.addElement("exclude").addText("(");
			}

			for (Object jobworkerfilterexclude : filterexcludelist)
			{
				if (jobworkerfilterexclude.toString() != null && !"".equals(jobworkerfilterexclude.toString()))
				{
					// 高级资源和策略的文件类型查询的结果不一致，策略是每个类型一条记录，而高级资源则是多个类型一条记录，故此处做统一处理
					String[] fileTypes;
					if (jobworkerfilterexclude.toString().indexOf(";") != -1)
					{
						fileTypes = jobworkerfilterexclude.toString().split(";");
					}
					else
					{
						fileTypes = new String[1];
						fileTypes[0] = jobworkerfilterexclude.toString();
					}
					for (String fileType : fileTypes)
					{
						// +号转义
						fileType = fileType.replace("+", "\\+");
						if (type != 2)
						{
							addexcludeTest = addexcludeTest + "(?:^";
						}
						else
						{
							addexcludeTest = addexcludeTest + "";
						}
						if (type == 0)
						{
							addexcludeTest += ".*\\" + fileType.substring(1, fileType.length()) + "$)|";
						}
						else if (type == 1)
						{
							addexcludeTest += ".*" + fileType + "$)|";
						}
						else if (type == 2)
						{
							addexcludeTest += fileType + "|";
						}
					}
				}
			}
			if (filterexcludelist.get(0).toString().length() != 0)
			{
				addexcludeTest = addexcludeTest.substring(0, addexcludeTest.lastIndexOf("|"));
				addexcludeTest = addexcludeTest + "";
				excludetab = excludetab.addText(addexcludeTest);
				excludetab.addText(")");
			}
		}
	}
}
