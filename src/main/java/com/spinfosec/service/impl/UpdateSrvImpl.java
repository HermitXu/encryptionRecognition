package com.spinfosec.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.UpdateDao;
import com.spinfosec.dao.entity.SpUpdateServerPackage;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.service.srv.IUpdateSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.GZip;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName UpdateSrvImpl
 * @Description: 〈升级管理〉
 * @date 2019/1/21
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("updateSrv")
public class UpdateSrvImpl implements IUpdateSrv
{
	private Logger logger = LoggerFactory.getLogger(UpdateSrvImpl.class);

	@Autowired
	private UpdateDao updateDao;

	/**
	 * 分页查询升级管理信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SpUpdateServerPackage> queryUpPackageByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SpUpdateServerPackage> list = updateDao.queryUpPackage(queryMap);
		PageInfo<SpUpdateServerPackage> pageList = new PageInfo<>(list);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 根据条件查找对应的升级包
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<SpUpdateServerPackage> queryUpPackage(Map<String, Object> queryMap)
	{
		return updateDao.queryUpPackage(queryMap);
	}

	/**
	 * 保存系统升级包
	 * @param updateServerPackage
	 */
	@Override
	public void saveUpdatePackage(SpUpdateServerPackage updateServerPackage)
	{
		updateDao.saveUpdatePackage(updateServerPackage);
	}

	/**
	 * 服务端升级包升级
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public RspCode deployServerPackage(String id) throws Exception
	{
		SpUpdateServerPackage serverPackage = updateDao.getUpdatePackageById(id);
		// 先记录升级成功
		serverPackage.setDeployStatus(Contants.UPDATE_DEPLOY_STATUS_DEPLOY_SUCCESS);
		serverPackage.setErrorCode("0");
		serverPackage.setDeployMsg("升级成功");
		serverPackage.setDeployTime(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN));
		updateDao.updateUpdatePackage(serverPackage);

		// 解压升级包
		String path = serverPackage.getPath();
		String dest;
		if (path.toLowerCase().endsWith(".tar.gz"))
		{
			dest = path.substring(0, path.lastIndexOf(File.separator));
		}
		else
		{
			// 内部错误，错误码统一为9
			serverPackage.setErrorCode("9");
			serverPackage.setDeployMsg("升级包格式不正确");
			saveServerDeployFailureStatus(serverPackage);
			return RspCode.INNER_ERROR;
		}

		List<String> rtFileNames = new ArrayList<>();
		boolean unzipSuccess = GZip.unTargzFile(path, dest, rtFileNames);
		if (!unzipSuccess)
		{
			serverPackage.setErrorCode("9");
			serverPackage.setDeployMsg("升级包解压缩失败");
			saveServerDeployFailureStatus(serverPackage);
			return RspCode.INNER_ERROR;
		}
		logger.debug("unzip success?  true");

		// 执行升级引导脚本
		logger.debug(
				"==========================================start exec shell======================================");
		String updateFilePath = dest + File.separator + "update.sh";
		logger.debug("==========================================" + updateFilePath
				+ "======================================");

		File cmdFile = new File(updateFilePath);
		if (!cmdFile.exists() || !cmdFile.isFile())
		{
			serverPackage.setErrorCode("9");
			serverPackage.setDeployMsg("升级包引导安装文件不存在或路径错误");
			saveServerDeployFailureStatus(serverPackage);
			return RspCode.INNER_ERROR;
		}

		String inputString;
		Process process;
		ProcessBuilder processBuilder = new ProcessBuilder("sh", updateFilePath);
		processBuilder.directory(new File(dest));
		processBuilder.redirectErrorStream(true);
		process = processBuilder.start();

		BufferedReader stdInput = null;
		try
		{
			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((inputString = stdInput.readLine()) != null)
			{
				logger.info(inputString);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error("------------------------------------" + e);
		}
		finally
		{
			if (null != stdInput)
			{
				IOUtils.closeQuietly(stdInput);
			}
		}

		logger.info("wait.......................");
		process.waitFor();
		logger.info("wait.......................");
		int status = process.exitValue();
		logger.debug("status: " + status);
		logger.debug("==========================================end exec shell======================================");

		// 升级错误码
		if (status > 0)
		{
			serverPackage.setErrorCode(status + "");
			switch (status)
			{
				case 1:
					serverPackage.setDeployMsg("升级包版本不匹配");
					break;
				case 2:
					serverPackage.setDeployMsg("服务停止失败");
					break;
				case 3:
					serverPackage.setDeployMsg("复制文件失败");
					break;
				case 4:
					serverPackage.setDeployMsg("服务启动失败");
					break;
				case 5:
					serverPackage.setDeployMsg("导入SQL脚本失败");
				default:
					break;
			}
			saveServerDeployFailureStatus(serverPackage);
			return RspCode.INNER_ERROR;
		}
		return RspCode.SUCCESS;
	}

	/**
	 * 重启服务器
	 * @return
	 */
	@Override
	public RspCode restartServer()
	{
		String[] cmds = { "/bin/sh", "-c", "shutdown -r now" };
		try
		{
			Runtime.getRuntime().exec(cmds);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.debug("服务器重启发生错误...... " + e.toString());
			return RspCode.INNER_ERROR;

		}
		return RspCode.SUCCESS;
	}

	/**
	 * 上传服务端升级包时，删除库里相同升级包信息
	 * @param idArr
	 */
	@Override
	public void deleteDuplicateServerPackageData(String[] idArr)
	{
		for (String id : idArr)
		{
			updateDao.deleteUpdatePackageById(id);
		}
	}

	/**
	 * 删除服务端升级包(并删除文件)
	 * @param ids
	 */
	@Override
	public void deleteServerPackage(List<String> ids)
	{
		for (String id : ids)
		{
			SpUpdateServerPackage serverPackage = updateDao.getUpdatePackageById(id);
			String path = serverPackage.getPath();
			if (path.toLowerCase().endsWith(".tar.gz"))
			{
				String pkgDir = path.substring(0, path.lastIndexOf(File.separator));
				File pkgFile = new File(pkgDir);
				if (pkgFile.exists() && pkgFile.isDirectory())
				{
					try
					{
						FileUtils.deleteDirectory(pkgFile);
					}
					catch (IOException e)
					{
						logger.error("升级包删除发生错误...", e);
					}
				}
			}
			File file = new File(path);
			if (file.exists())
			{
				boolean delete = file.delete();
				if (delete)
				{
					updateDao.deleteUpdatePackageById(id);
				}
			}
			else
			{
				updateDao.deleteUpdatePackageById(id);
			}
		}
	}

	// 记录服务器升级失败
	private void saveServerDeployFailureStatus(SpUpdateServerPackage serverPackage)
	{
		serverPackage.setDeployStatus(Contants.UPDATE_DEPLOY_STATUS_DEPLOY_FAIL);
		updateDao.updateUpdatePackage(serverPackage);
	}
}
