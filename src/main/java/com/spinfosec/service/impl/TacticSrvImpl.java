package com.spinfosec.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.TaskCenterDao;
import com.spinfosec.dao.entity.*;
import com.spinfosec.dao.tactic.DiscoveryTasksDao;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.deploy.OperateJobData;
import com.spinfosec.dto.pojo.system.JobMsg;
import com.spinfosec.dto.pojo.system.OperateMsg;
import com.spinfosec.dto.pojo.system.tatic.DiscoveryTaskFormData;
import com.spinfosec.dto.pojo.system.tatic.SchedulingData;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.service.srv.ITacticSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.AESPython;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.GenUtil;
import com.spinfosec.utils.MQUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TacticSrvImpl
 * @Description: 〈扫描策略业务处理实现类〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("tacticSrv")
public class TacticSrvImpl implements ITacticSrv
{

	private Logger log = LoggerFactory.getLogger(TacticSrvImpl.class);

	@Autowired
	private DiscoveryTasksDao discoveryTasksDao;

	@Autowired
	private TargetResSrvImpl targetResSrv;

	@Autowired
	private TaskCenterDao taskCenterDao;

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;

	/**
	 * 保存目标资源
	 * @param formData
	 * @return
	 * @throws Exception
	 */
	@Override
	public SpTargetRes saveTargetRes(TargetResFormData formData) throws Exception
	{
		SpTargetRes targetRes = formData.getTargetRes();
		String targetResId = GenUtil.getUUID();
		targetRes.setId(targetResId);
		targetRes.setCreateDate(new Date());
		targetRes.setLastModifyDate(new Date());
		// 对密码先用SM2解密 再用AESPython加密存在库中 因为部署任务时要用生成definition给SE
		if (targetRes.getResType().equalsIgnoreCase("Sftp") && targetRes.getKeyType().equalsIgnoreCase("rsa"))
		{
			// 读取密钥内容
			String publicKeyPath = targetResSrv.getPublicKeyPath() + File.separatorChar + targetRes.getPublicKeyName();
			File keyFile = new File(publicKeyPath);
			if (null != keyFile && keyFile.exists())
			{
				String publicKeyValue = FileUtils.readFileToString(keyFile);
				targetRes.setPublicKey(AESPython.Encrypt(publicKeyValue, AESPython.SKEY));
			}
			else
			{
				log.debug("publicKey file not exist. path = " + publicKeyPath);
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
		}
		else
		{
			if (StringUtils.isNotEmpty(targetRes.getPassword()))
			{
				String password = targetRes.getPassword();
				password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, password));
				targetRes.setPassword(AESPython.Encrypt(password, AESPython.SKEY));
			}
			else
			{

			}
			if (StringUtils.isNotEmpty(targetRes.getSharePassword()))
			{
				String sharePassword = targetRes.getSharePassword();
				sharePassword = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, sharePassword));
				targetRes.setSharePassword(AESPython.Encrypt(sharePassword, AESPython.SKEY));
			}
		}
		// 保存主机资源
		discoveryTasksDao.saveTargetRes(targetRes);

		// 保存主机资源资源树详情
		List<SpTargetResDetail> targetResDetailList = formData.getTargetResDetailList();
		for (SpTargetResDetail targetResDetail : targetResDetailList)
		{
			SpTargetResDetail detail = new SpTargetResDetail();
			detail.setId(GenUtil.getUUID());
			detail.setTargetResId(targetRes.getId());
			detail.setName(targetResDetail.getName());
			detail.setDisplayPath(targetResDetail.getDisplayPath());
			detail.setFileFolderType(targetResDetail.getFileFolderType());
			detail.setFileSize(targetResDetail.getFileSize());
			detail.setIsScanAllOnDatabase(targetResDetail.getIsScanAllOnDatabase());
			detail.setDatabaseName(targetResDetail.getDatabaseName());
			detail.setModifyDateTs(targetResDetail.getModifyDateTs());
			detail.setModifyDateTz(targetResDetail.getModifyDateTz());
			detail.setPath(targetResDetail.getPath());
			discoveryTasksDao.saveTargetResDetail(detail);
		}

		// 保存列设置的相关信息
		if (null != formData.getColumnResInfos() && formData.getColumnResInfos().size() > 0)
		{
			List<SpColumnResInfo> columnResInfoList = formData.getColumnResInfos();
			for (SpColumnResInfo spColumnResInfo : columnResInfoList)
			{
				if (StringUtils.isNotEmpty(spColumnResInfo.getPassword()))
				{
					String password = spColumnResInfo.getPassword();
					password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, password));
					spColumnResInfo.setPassword(AESPython.Encrypt(password, AESPython.SKEY));
				}
				spColumnResInfo.setId(GenUtil.getUUID());
				if ("rsa".equalsIgnoreCase(spColumnResInfo.getKeyType()))
				{
					String publicKeyPath = targetResSrv.getPublicKeyPath() + spColumnResInfo.getPublicKeyName();
					File keyFile = new File(publicKeyPath);
					if (null != keyFile && keyFile.exists())
					{
						String publicKeyValue = org.apache.commons.io.FileUtils.readFileToString(keyFile);
						spColumnResInfo.setPublickey(AESPython.Encrypt(publicKeyValue, AESPython.SKEY));
					}
					else
					{
						log.debug("publicKey file not exist. path = " + publicKeyPath);
						throw new TMCException(RspCode.FILE_NOT_EXIST);
					}
				}
				else
				{
					spColumnResInfo.setPublickey("");
				}
				spColumnResInfo.setTargetResId(targetResId.trim());
				discoveryTasksDao.saveColumnResInfos(spColumnResInfo);
			}
		}

		return targetRes;
	}

	/**
	 * 更新主机资源
	 * @param formData
	 * @return
	 */
	@Override
	public SpTargetRes updateTargetRes(TargetResFormData formData) throws Exception
	{

		SpTargetRes targetRes = formData.getTargetRes();

		// 原始主机资源ID 方便查原始密码
		String oldTarId = targetRes.getId();
		String oldTarPassword = discoveryTasksDao.getTargetPassByTargeId(oldTarId);
		List<SpColumnResInfo> oldColumnResInfos = discoveryTasksDao.getColumnResInfosByTargetId(oldTarId);

		// 删除原始主机资源关联信息
		deleteTargetRes(oldTarId);

		String targetResId = GenUtil.getUUID();
		targetRes.setId(targetResId);
		targetRes.setCreateDate(new Date());
		targetRes.setLastModifyDate(new Date());
		// 对密码先用SM2解密 再用AESPython加密存在库中 因为部署任务时要用生成definition给SE
		if (targetRes.getResType().equalsIgnoreCase("Sftp") && targetRes.getKeyType().equalsIgnoreCase("rsa"))
		{
			// 读取密钥内容
			String publicKeyPath = targetResSrv.getPublicKeyPath() + File.separatorChar + targetRes.getPublicKeyName();
			File keyFile = new File(publicKeyPath);
			if (null != keyFile && keyFile.exists())
			{
				String publicKeyValue = FileUtils.readFileToString(keyFile);
				targetRes.setPublicKey(AESPython.Encrypt(publicKeyValue, AESPython.SKEY));
			}
			else
			{
				log.debug("publicKey file not exist. path = " + publicKeyPath);
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
		}
		else
		{
			if (StringUtils.isNotEmpty(targetRes.getPassword()))
			{
				String password = targetRes.getPassword();
				password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, password));
				targetRes.setPassword(AESPython.Encrypt(password, AESPython.SKEY));
			}
			else
			{
				// 若没有传递密码则使用数据库中的原有的密码
				targetRes.setPassword(oldTarPassword);

			}
			// if (StringUtils.isNotEmpty(targetRes.getSharePassword()))
			// {
			// String sharePassword = targetRes.getSharePassword();
			// sharePassword = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, sharePassword));
			// targetRes.setSharePassword(AESPython.Encrypt(sharePassword, AESPython.SKEY));
			// }
		}
		// 保存主机资源
		discoveryTasksDao.saveTargetRes(targetRes);

		// 保存主机资源资源树详情
		List<SpTargetResDetail> targetResDetailList = formData.getTargetResDetailList();
		for (SpTargetResDetail targetResDetail : targetResDetailList)
		{
			SpTargetResDetail detail = new SpTargetResDetail();
			detail.setId(GenUtil.getUUID());
			detail.setTargetResId(targetRes.getId());
			detail.setName(targetResDetail.getName());
			detail.setDisplayPath(targetResDetail.getDisplayPath());
			detail.setFileFolderType(targetResDetail.getFileFolderType());
			detail.setFileSize(targetResDetail.getFileSize());
			detail.setIsScanAllOnDatabase(targetResDetail.getIsScanAllOnDatabase());
			detail.setDatabaseName(targetResDetail.getDatabaseName());
			detail.setModifyDateTs(targetResDetail.getModifyDateTs());
			detail.setModifyDateTz(targetResDetail.getModifyDateTz());
			detail.setPath(targetResDetail.getPath());
			discoveryTasksDao.saveTargetResDetail(detail);
		}

		// 保存列设置的相关信息
		if (null != formData.getColumnResInfos() && formData.getColumnResInfos().size() > 0)
		{
			List<SpColumnResInfo> columnResInfoList = formData.getColumnResInfos();
			for (SpColumnResInfo spColumnResInfo : columnResInfoList)
			{
				// 如果没返回密码 则用原来数据库里的密码
				if (StringUtils.isNotEmpty(spColumnResInfo.getPassword()))
				{
					String password = spColumnResInfo.getPassword();
					password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, password));
					spColumnResInfo.setPassword(AESPython.Encrypt(password, AESPython.SKEY));
				}
				else
				{
					a: for (SpColumnResInfo oldColumnResInfo : oldColumnResInfos)
					{
						if (oldColumnResInfo.getId().equals(spColumnResInfo.getId()))
						{
							spColumnResInfo.setPassword(oldColumnResInfo.getPassword());
							break a;
						}
					}
				}
				spColumnResInfo.setId(GenUtil.getUUID());
				if ("rsa".equalsIgnoreCase(spColumnResInfo.getKeyType()))
				{
					String publicKeyPath = targetResSrv.getPublicKeyPath() + spColumnResInfo.getPublicKeyName();
					File keyFile = new File(publicKeyPath);
					if (null != keyFile && keyFile.exists())
					{
						String publicKeyValue = org.apache.commons.io.FileUtils.readFileToString(keyFile);
						spColumnResInfo.setPublickey(AESPython.Encrypt(publicKeyValue, AESPython.SKEY));
					}
					else
					{
						log.debug("publicKey file not exist. path = " + publicKeyPath);
						throw new TMCException(RspCode.FILE_NOT_EXIST);
					}
				}
				else
				{
					spColumnResInfo.setPublickey("");
				}
				spColumnResInfo.setTargetResId(targetResId.trim());
				discoveryTasksDao.saveColumnResInfos(spColumnResInfo);
			}
		}

		return targetRes;
	}

	/**
	 * 根据ID查询扫描策略
	 * @param id
	 * @return
	 */
	@Override
	public DiscoveryTaskFormData getDiscoveryTaskById(String id)
	{
		DiscoveryTaskFormData data = new DiscoveryTaskFormData();
		// 查询扫描策略主表
		SpDiscoveryTasks discoveryTasks = discoveryTasksDao.getDiscoveryTaskById(id);
		if (null == discoveryTasks)
		{
			return null;
		}
		data.setDiscoveryTasks(discoveryTasks);

		// 查询扫描策略排除文件类型列表
		List<String> excldFileTypesList = null;
		SpDscvrExcldFileTypes excldFileTypes = discoveryTasksDao.getExcldFileByDisId(id);
		if (null != excldFileTypes)
		{
			String excldFile = excldFileTypes.getFileType();
			String[] excldFileSplit = excldFile.split(";");
			excldFileTypesList = Arrays.asList(excldFileSplit);
			data.setExcldFileTypes(excldFileTypesList);
		}

		// 查询扫描策略包含文件类型列表
		List<String> incldFileTypesList = null;
		SpDscvrIncldFileTypes incldFileTypes = discoveryTasksDao.getIncldFileByDisId(id);
		if (null != incldFileTypes)
		{
			String incldFile = incldFileTypes.getFileType();
			String[] incldFileSplit = incldFile.split(";");
			incldFileTypesList = Arrays.asList(incldFileSplit);
			data.setIncldFileTypes(incldFileTypesList);
		}

		// 查询时间调度信息
		SchedulingData schedulingData = new SchedulingData();
		SpSchedulingData spSchedulingData = discoveryTasksDao.getSchedulingDataById(discoveryTasks.getSchedulingId());
		schedulingData.setSpSchedulingData(spSchedulingData);
		data.setSchedulingData(schedulingData);

		// 查询资源主机
		SpTargetRes targetRes = discoveryTasksDao.getTargetResById(discoveryTasks.getTargetId());
		TargetResFormData targetResFormData = new TargetResFormData();
		targetResFormData.setTargetRes(targetRes);
		// 查询资源主机详细信息
		List<SpTargetResDetail> targetResDetails = discoveryTasksDao.getTargetResDetailByTargetId(targetRes.getId());
		targetResFormData.setTargetResDetailList(targetResDetails);

		// 查询列设置关系信息
		List<SpColumnResInfo> columnResInfos = discoveryTasksDao.getColumnResInfosByTargetId(targetRes.getId());
		targetResFormData.setColumnResInfos(columnResInfos);

		data.setTargetResFormData(targetResFormData);


		// 查询加密算法库和文件类型关系
		List<SpAlgorithmFileType> algorithmFileTypes = discoveryTasksDao
				.getAlgorithmFileTypeByDisId(discoveryTasks.getId());
		data.setAlgorithmFileTypeList(algorithmFileTypes);

		return data;
	}

	/**
	 * 保存扫描策略
	 * @param data
	 */
	@Override
	public void saveDiscoveryTask(DiscoveryTaskFormData data)
	{
		// 生成扫描策略主表ID
		String discoveryTaskId = GenUtil.getUUID();

		// 保存扫描策略排除文件类型列表
		List<String> excldFileTypes = data.getExcldFileTypes();
		if (null != excldFileTypes && excldFileTypes.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (String fileType : excldFileTypes)
			{
				if (fileType.isEmpty())
				{
					continue;
				}
				if (sb.length() == 0)
				{
					sb.append(fileType);
				}
				else
				{
					sb.append(";").append(fileType);
				}
			}
			SpDscvrExcldFileTypes dscvrExcldFileTypes = new SpDscvrExcldFileTypes();
			dscvrExcldFileTypes.setId(GenUtil.getUUID());
			dscvrExcldFileTypes.setFileType(sb.toString());
			dscvrExcldFileTypes.setDiscoveryTaskId(discoveryTaskId);
			dscvrExcldFileTypes.setElementIndex(new BigDecimal(0));
			discoveryTasksDao.saveSpDscvrExcldFileTypes(dscvrExcldFileTypes);
		}

		// 保存扫描策略包含文件类型列表
		List<String> incldFileTypes = data.getIncldFileTypes();
		if (null != incldFileTypes && incldFileTypes.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (String fileType : incldFileTypes)
			{
				if (fileType.isEmpty())
				{
					continue;
				}
				if (sb.length() == 0)
				{
					sb.append(fileType);
				}
				else
				{
					sb.append(";").append(fileType);
				}
			}
			SpDscvrIncldFileTypes dscvrIncldFileTypes = new SpDscvrIncldFileTypes();
			dscvrIncldFileTypes.setId(GenUtil.getUUID());
			dscvrIncldFileTypes.setDiscoveryTaskId(discoveryTaskId);
			dscvrIncldFileTypes.setFileType(sb.toString());
			dscvrIncldFileTypes.setElementIndex(new BigDecimal(0));
			discoveryTasksDao.saveSpDscvrIncldFileTypes(dscvrIncldFileTypes);
		}

		// 保存调度信息时间
		SchedulingData schedulingData = data.getSchedulingData();
		String schedulingDataId = GenUtil.getUUID();
		if (null != schedulingData)
		{
			// 保存时间调度主表信息
			SpSchedulingData spSchedulingData = schedulingData.getSpSchedulingData();
			spSchedulingData.setId(schedulingDataId);
			if ("ONCE".equals(spSchedulingData.getFrequencyType())
					|| "CONTINUOUSLY".equals(spSchedulingData.getFrequencyType()))
			{
				// 如果未选择不早于或开始于 时 任务的开始时间设置为系统当前时间
				if (spSchedulingData.getUseInitialRecur() == (double) 0)
				{
					spSchedulingData.setStartDate(new Date());
				}
			}
			discoveryTasksDao.saveSpSchedulingData(spSchedulingData);
		}

		// 保存加密算法库文件类型关系表
		List<SpAlgorithmFileType> algorithmFileTypeList = data.getAlgorithmFileTypeList();
		for (SpAlgorithmFileType algorithmFileType : algorithmFileTypeList)
		{
			algorithmFileType.setId(GenUtil.getUUID());
			algorithmFileType.setJobId(discoveryTaskId);
			discoveryTasksDao.saveAlgorithmFileType(algorithmFileType);
		}

		// 得到扫描策略主表信息
		SpDiscoveryTasks discoveryTasks = data.getDiscoveryTasks();
		// 关联时间调度ID
		discoveryTasks.setSchedulingId(schedulingDataId);
		discoveryTasks.setId(discoveryTaskId);
		discoveryTasks.setCreateDate(new Date());
		discoveryTasks.setTargetId(data.getTargetResFormData().getTargetRes().getId());
		// 保存扫描策略主表信息
		discoveryTasksDao.saveDiscoveryTask(discoveryTasks);
	}

	/**
	 * @Title: updateSpPlcDiscoveryTask
	 * @Description: 更新扫描策略
	 * @param: @param id   
	 * @return: void
	 * @throws
	 */
	@Override
	public void updateSpPlcDiscoveryTask(DiscoveryTaskFormData data) throws Exception
	{
		SpDiscoveryTasks discoveryTasks = discoveryTasksDao.getDiscoveryTaskById(data.getDiscoveryTasks().getId());
		if (null == discoveryTasks)
		{
			throw new TMCException(RspCode.OBJECE_NOT_EXIST);
		}
		// 查询出原来计划信息
		DiscoveryTaskFormData srcData = getDiscoveryTaskById(discoveryTasks.getId());
		// 删除整点信息
		if (null != srcData.getSchedulingData() && null != srcData.getSchedulingData().getSpSchedulingData())
		{
			List<SpSchedulingDayTf> tfList = discoveryTasksDao
					.getDayTfByschedulingDataId(srcData.getSchedulingData().getSpSchedulingData().getId());
			for (SpSchedulingDayTf spSchedulingDayTf : tfList)
			{
				if (null != spSchedulingDayTf)
				{
					// 删除整点信息
					discoveryTasksDao.deleteSpDayTimeframeHoursBydayTimeframeId(spSchedulingDayTf.getDayTfId());
					// 删除天信息
					discoveryTasksDao.deleteDayTimeframeById(spSchedulingDayTf.getDayTfId());
					// 删除关联表信息
					discoveryTasksDao.deleteDayTfById(spSchedulingDayTf.getId());
				}
			}
			// 删除时间计划表
			discoveryTasksDao.deleteSchedulingDataById(srcData.getSchedulingData().getSpSchedulingData().getId());
		}

		// 保存时间调度主表信息
		SchedulingData schedulingData = data.getSchedulingData();
		String schedulingDataId = GenUtil.getUUID();
		if (null != schedulingData)
		{
			SpSchedulingData spSchedulingData = schedulingData.getSpSchedulingData();
			spSchedulingData.setId(schedulingDataId);
			discoveryTasksDao.saveSpSchedulingData(spSchedulingData);
		}
		// 更新扫略策略时间计划关联
		discoveryTasks.setSchedulingId(schedulingDataId);

		// 删除扫描策略包含文件类型列表
		discoveryTasksDao.deleteIncldFileTypesBydiscoveryTaskId(data.getDiscoveryTasks().getId());
		// 删除扫描策略排除文件类型列表
		discoveryTasksDao.deleteExcldFileTypesBydiscoveryTaskId(data.getDiscoveryTasks().getId());

		// 保存扫描策略排除文件类型列表
		List<String> excldFileTypes = data.getExcldFileTypes();
		if (null != excldFileTypes && excldFileTypes.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (String fileType : excldFileTypes)
			{
				if (fileType.isEmpty())
				{
					continue;
				}
				if (sb.length() == 0)
				{
					sb.append(fileType);
				}
				else
				{
					sb.append(";").append(fileType);
				}
			}
			SpDscvrExcldFileTypes dscvrExcldFileTypes = new SpDscvrExcldFileTypes();
			dscvrExcldFileTypes.setId(GenUtil.getUUID());
			dscvrExcldFileTypes.setFileType(sb.toString());
			dscvrExcldFileTypes.setDiscoveryTaskId(discoveryTasks.getId());
			dscvrExcldFileTypes.setElementIndex(new BigDecimal(0));
			discoveryTasksDao.saveSpDscvrExcldFileTypes(dscvrExcldFileTypes);
		}

		// 保存扫描策略包含文件类型列表
		List<String> incldFileTypes = data.getIncldFileTypes();
		if (null != incldFileTypes && incldFileTypes.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (String fileType : incldFileTypes)
			{
				if (fileType.isEmpty())
				{
					continue;
				}
				if (sb.length() == 0)
				{
					sb.append(fileType);
				}
				else
				{
					sb.append(";").append(fileType);
				}
			}
			SpDscvrIncldFileTypes dscvrIncldFileTypes = new SpDscvrIncldFileTypes();
			dscvrIncldFileTypes.setId(GenUtil.getUUID());
			dscvrIncldFileTypes.setDiscoveryTaskId(discoveryTasks.getId());
			dscvrIncldFileTypes.setFileType(sb.toString());
			dscvrIncldFileTypes.setElementIndex(new BigDecimal(0));
			discoveryTasksDao.saveSpDscvrIncldFileTypes(dscvrIncldFileTypes);
		}

		// 重新保存主机资源和详细信息
		TargetResFormData formData = data.getTargetResFormData();
		SpTargetRes targetRes = updateTargetRes(formData);

		// 删除策略和加密算法关系
		discoveryTasksDao.deleteAlgorithmFileType(data.getDiscoveryTasks().getId());
		// 重新保存策略和加密算法关系
		List<SpAlgorithmFileType> algorithmFileTypeList = data.getAlgorithmFileTypeList();
		for (SpAlgorithmFileType algorithmFileType : algorithmFileTypeList)
		{
			algorithmFileType.setId(GenUtil.getUUID());
			algorithmFileType.setJobId(data.getDiscoveryTasks().getId());
			discoveryTasksDao.saveAlgorithmFileType(algorithmFileType);
		}

		// 更新扫描策略表中主机资源ID
		discoveryTasks.setTargetId(targetRes.getId());

		// discoveryTasks.setBeCheckedOrgId(data.getDiscoveryTasks().getBeCheckedOrgId());
		// 策略基本信息
		discoveryTasks.setName(data.getDiscoveryTasks().getName());
		// 备注
		discoveryTasks.setDescription(data.getDiscoveryTasks().getDescription());
		// TODO 编辑时不可更改类型
		discoveryTasks.setDiscoveryTaskType(data.getDiscoveryTasks().getDiscoveryTaskType());
		// 部署状态 SYNCHRONIZED, UNSYNCHRONIZED_NEW, UNSYNCHRONIZED_EDIT
		discoveryTasks.setElementStatus(data.getDiscoveryTasks().getElementStatus());
		// 是否启用文件名称过滤
		discoveryTasks.setIsFileNameEnabled(data.getDiscoveryTasks().getIsFileNameEnabled());
		// 常用文件类型
		discoveryTasks.setCommonOrcustomFileType(data.getDiscoveryTasks().getCommonOrcustomFileType());
		// 文件时间过滤是否启用
		discoveryTasks.setIsFileAgeEnabled(data.getDiscoveryTasks().getIsFileAgeEnabled());
		// 对应文件时间过滤选项内的三个radio选项
		discoveryTasks.setScanPeriodType(data.getDiscoveryTasks().getScanPeriodType());
		// 多少个月之内修改过的文件
		discoveryTasks.setModifiedWithinMonths(data.getDiscoveryTasks().getModifiedWithinMonths());
		// 多少个月之前修改的文件
		discoveryTasks.setModifiedMonthsAgo(data.getDiscoveryTasks().getModifiedMonthsAgo());
		// 修改时间开始
		discoveryTasks.setModifiedFromDate(data.getDiscoveryTasks().getModifiedFromDate());
		// 修改日期结束
		discoveryTasks.setModifiedToDate(data.getDiscoveryTasks().getModifiedToDate());
		// 是否启用文件大小的上下限
		discoveryTasks.setIsLargerThanEnabled(data.getDiscoveryTasks().getIsLargerThanEnabled());
		// 上限文件大小
		discoveryTasks.setSizeLargerThan(data.getDiscoveryTasks().getSizeLargerThan());
		// 是否启用文件下限
		discoveryTasks.setIsSmallerThanEnalbed(data.getDiscoveryTasks().getIsSmallerThanEnalbed());
		// 文件下限大小
		discoveryTasks.setSizeSmallerThan(data.getDiscoveryTasks().getSizeSmallerThan());
		// C_USER_DEFINE C_PRE_DEFINE 用户自定义
		discoveryTasks.setDefinitionType(Contants.C_USER_DEFINE);
		// 文档内ocr启用
		discoveryTasks.setOcrEnabled(data.getDiscoveryTasks().getOcrEnabled());
		// cpu限制启用
		discoveryTasks.setIsCpuLimitEnabled(data.getDiscoveryTasks().getIsCpuLimitEnabled());
		// cpu限制
		discoveryTasks.setCpuLimitValue(data.getDiscoveryTasks().getCpuLimitValue());
		// 忙时开始时间
		discoveryTasks.setBusyBandwidthStartdate(data.getDiscoveryTasks().getBusyBandwidthStartdate());
		// 忙时结束时间
		discoveryTasks.setBusyBandwidthEnddate(data.getDiscoveryTasks().getBusyBandwidthEnddate());
		// 忙时带宽限制启用
		discoveryTasks.setBusyBandwidthEnabled(data.getDiscoveryTasks().getBusyBandwidthEnabled());
		// 忙时带宽值
		discoveryTasks.setBusyBandwidthLimit(data.getDiscoveryTasks().getBusyBandwidthLimit());
		// 扫描百分比
		discoveryTasks.setPercentEnabled(data.getDiscoveryTasks().getPercentEnabled());
		// 百分比扫描百分数
		discoveryTasks.setPercentAge(data.getDiscoveryTasks().getPercentAge());
		// 更新策略运行时间设置
		discoveryTasks.setTimerEnabled(data.getDiscoveryTasks().getTimerEnabled());
		// 计时器扫描值
		discoveryTasks.setTimerAge(data.getDiscoveryTasks().getTimerAge());
		// 被检单位
		discoveryTasks.setBeCheckedOrgId(data.getDiscoveryTasks().getBeCheckedOrgId());
		// 检查人
		discoveryTasks.setCheckUser(data.getDiscoveryTasks().getCheckUser());
		// 检查时间
		discoveryTasks.setCheckDate(data.getDiscoveryTasks().getCheckDate());
		// ocr预处理
		discoveryTasks.setOcrPreprocess(data.getDiscoveryTasks().getOcrPreprocess());
		// 检查单位
		discoveryTasks.setCheckOrgName(data.getDiscoveryTasks().getCheckOrgName());

		// 查看是否存在未开始的任务信息 预置一条任务中心信息 显示时体现出该策略未被运行
		Map<String, Object> conditionMap = new HashMap<>();
		conditionMap.put("jobId", discoveryTasks.getId());
		conditionMap.put("isLast", 1);
		conditionMap.put("result", "UNSTART");

		List<SpTask> taskByConditionMap = taskCenterDao.getTaskByConditionMap(conditionMap);
		if (taskByConditionMap.isEmpty())
		{
			taskCenterDao.updateTaskIsLastByJobId(discoveryTasks.getId(), "0");
			SpTask task = new SpTask();
			task.setId(GenUtil.getUUID());
			task.setJobId(discoveryTasks.getId());
			task.setName(discoveryTasks.getName());
			task.setType(discoveryTasks.getDiscoveryTaskType());
			// 置为最新任务
			task.setIsLast("1");
			task.setResult("UNSTART");
			taskCenterDao.saveTask(task);
		}

		discoveryTasksDao.updateDiscoveryTask(discoveryTasks);

	}

	/**
	 * 根据主机资源ID删除主机资源信息
	 * @param targetId  主机资源ID
	 */
	@Override
	public void deleteTargetRes(String targetId)
	{
		// 根据策略ID查询对应的主机资源ID
		// 删除主机资源
		discoveryTasksDao.deleteTargetResById(targetId);
		// 删除主机资源详细信息
		discoveryTasksDao.deleteTargetResDetailByTargetId(targetId);
		// 删除列设置关系表
		discoveryTasksDao.deleteColumnResInfos(targetId);

	}

	@Override
	public CodeRsp deleteJobByMq(List<String> jobIds)
	{
		String code = "";
		try
		{
			// 构造请求数据结构体
			OperateJobData msgData = new OperateJobData();
			String msgJson = JSON.toJSONString(msgData.getInstance(jobIds, OperateJobData.DELETE_JOB));
			String responseText = MQUtil
					.sendMessage(connectionFactory, Contants.JOB_OPERATE, msgJson, 2 * 60 * 1000);
			// 解析返回内容
			OperateMsg response = JSON.parseObject(responseText, OperateMsg.class);
			if (null != response)
			{
				List<JobMsg> content = response.getContent();
				if (null != content && !content.isEmpty())
				{
					JobMsg jobMsg = content.get(0);
					if (null != jobMsg)
					{
						code = jobMsg.getCode();
						return new CodeRsp(RspCode.getRspCodeByCode(code));
					}
				}
				return new CodeRsp(RspCode.DELETE_FAILED);
			}
		}
		catch (TMCException e)
		{
			log.error("删除扫描策略,消息通信异常,错误原因:"+code+" " + e.getMessage(), e);
			return new CodeRsp(RspCode.MQ_CONNECTION_ERROR);
		}

		return null;
	}

	/**
	 * 根据ID删除扫描策略
	 * @param idList
	 */
	@Override
	public void deleteDiscoveryTaskById(List<String> idList)
	{
		for (String id : idList)
		{
			// 先根据ID查询出所有扫描策略信息
			DiscoveryTaskFormData data = getDiscoveryTaskById(id);
			// 删除整点信息
			if (null != data.getSchedulingData() && null != data.getSchedulingData().getSpSchedulingData())
			{
				List<SpSchedulingDayTf> tfList = discoveryTasksDao
						.getDayTfByschedulingDataId(data.getSchedulingData().getSpSchedulingData().getId());
				for (SpSchedulingDayTf spSchedulingDayTf : tfList)
				{
					if (null != spSchedulingDayTf)
					{
						// 删除整点信息
						discoveryTasksDao.deleteSpDayTimeframeHoursBydayTimeframeId(spSchedulingDayTf.getDayTfId());
						// 删除天信息
						discoveryTasksDao.deleteDayTimeframeById(spSchedulingDayTf.getDayTfId());
						// 删除关联表信息
						discoveryTasksDao.deleteDayTfById(spSchedulingDayTf.getId());
					}
				}
				// 删除时间计划表
				discoveryTasksDao.deleteSchedulingDataById(data.getSchedulingData().getSpSchedulingData().getId());
			}

			// 删除扫描策略包含文件类型列表
			discoveryTasksDao.deleteIncldFileTypesBydiscoveryTaskId(data.getDiscoveryTasks().getId());
			// 删除扫描策略排除文件类型列表
			discoveryTasksDao.deleteExcldFileTypesBydiscoveryTaskId(data.getDiscoveryTasks().getId());

			// 删除扫描策略主表
			discoveryTasksDao.deleteDiscoveryTaskById(data.getDiscoveryTasks().getId());

			// 删除策略和加密算法关系
			discoveryTasksDao.deleteAlgorithmFileType(data.getDiscoveryTasks().getId());

			// 删除主机资源相关信息
			deleteTargetRes(data.getDiscoveryTasks().getTargetId());

			// // TODO 删除该策略下的所有任务
			discoveryTasksDao.deleteTaskInfoByDisId(data.getDiscoveryTasks().getId());

		}
	}

	/**
	 * 获取所有文件类型
	 * @return
	 */
	@Override
	public PageInfo<SpPlcFileTypes> getFileTypesByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SpPlcFileTypes> fileTypeList = discoveryTasksDao.getFileType(queryMap);
		PageInfo<SpPlcFileTypes> pageInfo = new PageInfo<>(fileTypeList);
		return pageInfo;
	}

	/**
	 * 获取常用文件类型
	 * @return
	 */
	@Override
	public List<SpPlcFileTypes> getCommonFileType()
	{
		return discoveryTasksDao.getCommonFileType();
	}

	/**
	 * 添加/移除常用文件类型
	 * @param id
	 * @param state
	 */
	@Override
	public void updateFileTypeCommon(String id, String state)
	{
		discoveryTasksDao.updateFileTypeCommon(id, state);
	}

	/**
	 * 获取加密算法库
	 * @return
	 */
	@Override
	public List<SpEncryptionAlgorithm> getEncryptionAlgorithm()
	{
		return discoveryTasksDao.getEncryptionAlgorithm();
	}

	/**
	 * 根据用户ID获取其创建的所有任务ID
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> getJobIdListByUserId(String userId)
	{
		return discoveryTasksDao.getJobIdListByUserId(userId);
	}
}
