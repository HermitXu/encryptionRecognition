package com.spinfosec.service.srv;

import com.spinfosec.dto.pojo.system.LdapData;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ITargetResSrv
 * @Description: 〈主机资源业务处理接口〉
 * @date 2018/11/7
 * All rights Reserved, Designed By SPINFO
 */
public interface ITargetResSrv
{
	/**
	 * 获取密钥文件
	 * @return
	 */
	File[] getSftpPublicKeyFiles();

	/**
	 * 获取密钥文件路径
	 * @return
	 */
	String getPublicKeyPath();

	/**
	 * 本地保存密钥
	 * @param direct
	 * @param req
	 * @param file
	 * @param bytes
	 */
	void saveFileToLocal(File direct, MultipartHttpServletRequest req, MultipartFile file, byte[] bytes)
			throws IOException;

	/**
	 * 根据文件名判断密钥是否存在
	 * @param fileName
	 * @return
	 */
	boolean getPublicKeyByName(String fileName);

	/**
	 * 删除密钥
	 * @param fileName
	 * @return
	 */
	boolean deleteSftpPublicKey(String fileName);

	/**
	 * Exchange连接获取信息
	 * @param ou
	 * @return
	 * @throws Exception
	 */
	List<LdapData> getLdapUsers(String ou) throws Exception;

	/**
	 * 通过主机资源ID查询密码
	 * @param targetId 主机资源ID
	 * @return
	 */
	String getTargetPass(String targetId);

}
