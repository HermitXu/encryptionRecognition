package com.spinfosec.service.impl;

import com.spinfosec.dao.tactic.DiscoveryTasksDao;
import com.spinfosec.dto.pojo.system.LdapData;
import com.spinfosec.service.srv.ITargetResSrv;
import com.spinfosec.system.MemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TargetResSrvImpl
 * @Description: 〈主机资源业务处理实现类〉
 * @date 2018/11/7
 * All rights Reserved, Designed By SPINFO
 */
@Service("targetResSrv")
public class TargetResSrvImpl implements ITargetResSrv
{
	private Logger log = LoggerFactory.getLogger(TargetResSrvImpl.class);

	@Autowired
	private DiscoveryTasksDao discoveryTasksDao;

	@Autowired
	private TestConnectSrvImpl testConnectSrv;

	/**
	 * 获取密钥文件
	 * @return
	 */
	@Override
	public File[] getSftpPublicKeyFiles()
	{
		File keyDir = new File(getPublicKeyPath());
		if (!keyDir.exists())
		{
			keyDir.mkdirs();
		}
		File[] files = keyDir.listFiles();
		return files;
	}

	/**
	 * 获取密钥文件路径
	 * @return
	 */
	@Override
	public String getPublicKeyPath()
	{
		String resources = MemInfo.getServletContextPath();
		StringBuffer keyPath = new StringBuffer(resources);
		keyPath.append(File.separatorChar);
		keyPath.append("document");
		keyPath.append(File.separator);
		keyPath.append("sftpKey");
		keyPath.append(File.separator);
		return keyPath.toString();
	}

	/**
	 * 本地保存密钥
	 * @param direct
	 * @param req
	 * @param file
	 * @param bytes
	 */
	@Override
	public void saveFileToLocal(File direct, MultipartHttpServletRequest req, MultipartFile file, byte[] bytes)
			throws IOException
	{
		if (!direct.exists())
		{
			direct.mkdirs();
		}

		StringBuilder toPath = new StringBuilder(direct.getAbsolutePath()).append(File.separator)
				.append(file.getOriginalFilename());
		File toFile = new File(toPath.toString());
		if (!toFile.exists())
		{
			toFile.createNewFile();
		}

		FileCopyUtils.copy(bytes, toFile);
	}

	/**
	 * 根据文件名判断密钥是否存在
	 * @param fileName
	 * @return
	 */
	@Override
	public boolean getPublicKeyByName(String fileName)
	{
		List<String> list = discoveryTasksDao.getPublicKeyNameByName(fileName);
		if (null != list && list.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * 删除密钥
	 * @param fileName
	 * @return
	 */
	@Override
	public boolean deleteSftpPublicKey(String fileName)
	{
		boolean isOk = false;
		String keyPath = getPublicKeyPath() + fileName;
		File keyFile = new File(keyPath);
		if (null != keyFile && keyFile.exists())
		{
			isOk = keyFile.delete();
		}
		return isOk;
	}

	/**
	 * Exchange连接获取信息
	 * @param ou
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<LdapData> getLdapUsers(String ou) throws Exception
	{
		return testConnectSrv.getLdapUsers(ou);
	}

	/**
	 * 通过主机资源ID查询密码
	 * @param targetId 主机资源ID
	 * @return
	 */
	@Override
	public String getTargetPass(String targetId)
	{
		return discoveryTasksDao.getTargetPassByTargeId(targetId);
	}
}
