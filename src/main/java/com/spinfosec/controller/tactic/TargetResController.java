package com.spinfosec.controller.tactic;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.ADConfig;
import com.spinfosec.dto.pojo.system.LdapData;
import com.spinfosec.dto.pojo.system.tatic.TargetResDataForMQ;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.service.srv.ITargetResSrv;
import com.spinfosec.service.srv.ITestConnectSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.*;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TargetResController
 * @Description: 〈主机资源控制层〉
 * @date 2018/11/5
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/target")
public class TargetResController
{
	private Logger log = LoggerFactory.getLogger(TargetResController.class);

	@Autowired
	private ITargetResSrv targetResSrv;

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;

	@Autowired
	private ITestConnectSrv testConnectSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 主机资源连接
	 * @param req
	 * @param targetResForMq
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/targetResDataRequest", method = RequestMethod.POST)
	public @ResponseBody ResponseBean targetResDataRequest(HttpServletRequest req,
			@RequestBody TargetResDataForMQ targetResForMq) throws TMCException
	{
		String targetId = targetResForMq.getTargetId();
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);

		String type = targetResForMq.getType();
		// 当访问的为阿里oss时记录连接日志
		if ("file".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问文件共享");
		}
		else if ("DB".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问数据库");
		}
		else if ("Exchange".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问Exchange");
		}
		else if ("SharePoint".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问SharePoint");
		}
		else if ("Lotus".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问Lotus");
		}
		else if ("Ftp".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问Ftp");
		}
		else if ("Sftp".equalsIgnoreCase(type))
		{
			operateLogInfo.setOperation("连接访问Linux主机");
		}
		systemSrv.saveOperateLog(operateLogInfo);

		String uuid = GenUtil.getUUID();
		targetResForMq.setId(uuid);
		JSONObject jsonObject = null;
		String json;
		try
		{
			// 密码解密加密
			if (StringUtils.isNotEmpty(targetResForMq.getPassword()))
			{
				String password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, targetResForMq.getPassword()));
				targetResForMq.setPassword(AESPython.Encrypt(password, AESPython.SKEY));
			}
			// 如果没传递密码 判断targetId是否存在 存在去库中查询密码
			else
			{

				// 判断Linux是否使用密码
				if ("Sftp".equalsIgnoreCase(type) && !"rsa".equalsIgnoreCase(targetResForMq.getKeyType()))
				{
					if (StringUtils.isNotEmpty(targetId))
					{
						String password = targetResSrv.getTargetPass(targetId);
						if (StringUtils.isNotEmpty(password))
						{
							targetResForMq.setPassword(password);
						}
						else
						{
							return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PASSWORD_CAN_NOT_BE_NULL));
						}
					}
					else
					{
						return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PASSWORD_CAN_NOT_BE_NULL));
					}

				}
				else if (!"Sftp".equalsIgnoreCase(type))
				{
					if (StringUtils.isNotEmpty(targetId))
					{
						String password = targetResSrv.getTargetPass(targetId);
						if (StringUtils.isNotEmpty(password))
						{
							targetResForMq.setPassword(password);
						}
						else
						{
							return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PASSWORD_CAN_NOT_BE_NULL));
						}
					}
					else
					{
						return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PASSWORD_CAN_NOT_BE_NULL));
					}

				}
			}
			// Linux密钥处理
			if (StringUtils.isNotEmpty(targetResForMq.getPublicKeyName()))
			{
				String publicKeyPath = targetResSrv.getPublicKeyPath() + targetResForMq.getPublicKeyName();
				File keyFile = new File(publicKeyPath);
				if (null != keyFile && keyFile.exists())
				{
					String publicKeyValue = FileUtils.readFileToString(keyFile);
					targetResForMq.setPublicKey(AESPython.Encrypt(publicKeyValue, AESPython.SKEY));
				}
			}

			json = GenUtil.beanToJson(targetResForMq);
			log.info("测试连接格式：" + json);
			String response = MQUtil.sendMessage(connectionFactory, Contants.TARGET_RES_OPERATE, json, 5 * 60 * 1000);
			log.info("响应为：" + response);

			jsonObject = JSONObject.parseObject(response);
			String errCode = jsonObject.getString("errCode");
			if (errCode.equals("510000"))
			{
				// Exchange连接成功则查询名称和组织单元
				if (targetResForMq.getType().equalsIgnoreCase("Exchange"))
				{
					List<LdapData> ldapUsers = testConnectSrv.getLdapUsers("");
					return ResultUtil.getSuccessResult(ldapUsers);
				}
				else
				{
					return ResultUtil.getSuccessResult(jsonObject);
				}
			}
			else
			{
				CodeRsp codeRsp = new CodeRsp(RspCode.CONNECT_ERROR);
				if (errCode.equals("510004"))
				{
					codeRsp.setMsg("连接失败,用户名或密码不正确！");
				}
				else if (errCode.equals("510021"))
				{
					codeRsp.setMsg("连接Lotus服务器失败，服务未启动或未知Lotus主机!");
				}
				else if (errCode.equals("510030"))
				{
					codeRsp.setMsg("连接失败！无效的网络路径!");
				}
				else if (errCode.equals("510040"))
				{
					codeRsp.setMsg("主机连接失败!");
				}
				else if (errCode.equals("510041"))
				{
					codeRsp.setMsg("未知的用户名或错误密码!");
				}
				else if (errCode.equals("510050"))
				{
					codeRsp.setMsg("数据库连接失败！");
				}

				return ResultUtil.getDefinedCodeResult(codeRsp, jsonObject);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.CONNECT_ERROR, e);

		}
	}

	/**
	 * Linux上传密钥
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/publicKeyUpload", method = RequestMethod.POST)
	public @ResponseBody ResponseBean publicKeyUpload(MultipartHttpServletRequest req, HttpServletRequest request)
			throws Exception
	{
		File keyDir = null;
		try
		{
			log.info("start upload public !");
			Iterator<String> iterator = req.getFileNames();
			while (iterator.hasNext())
			{
				String name = iterator.next();
				List<MultipartFile> files = req.getFiles(name);
				for (MultipartFile file : files)
				{
					byte[] bytes = file.getBytes();
					String publicKeyPath = targetResSrv.getPublicKeyPath();
					keyDir = new File(publicKeyPath);
					targetResSrv.saveFileToLocal(keyDir, req, file, bytes);
				}
			}

			log.info("upload publicKey ok!");
			return ResultUtil.getSuccessResult();
		}
		catch (Exception e)
		{
			log.info("密钥上传失败!", e);
			return ResultUtil.getFailResult(new CodeRsp(RspCode.FILE_UPLOAD_ERROR));
		}
	}

	/**
	 * 获取密钥列表
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getPublicKeyList", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getPublicKeyList(HttpServletRequest req)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		File[] files = targetResSrv.getSftpPublicKeyFiles();
		for (File file : files)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", file.getName());
			map.put("text", file.getName());
			list.add(map);
		}
		return ResultUtil.getSuccessResult(list);
	}

	/**
	 * 删除密钥
	 * @param req
	 * @param fileNameArr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deleteSftpPublicKey/{fileNameArr}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteSftpPublicKey(HttpServletRequest req, @PathVariable String[] fileNameArr)
			throws UnsupportedEncodingException
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		boolean isExist = false;
		String fileName = "";
		for (int i = 0; i < fileNameArr.length; i++)
		{
			if (i == 0)
			{
				fileName += fileNameArr[i];
			}
			else
			{
				fileName += "." + fileNameArr[i];
			}
		}

		/**
		 * 判断该密钥是否在被使用
		 */
		boolean isKeyExist = targetResSrv.getPublicKeyByName(fileName);
		if (isKeyExist == isExist)
		{

			boolean isOk = targetResSrv.deleteSftpPublicKey(fileName);
			if (isOk)
			{
				return ResultUtil.getSuccessResult();
			}
			else
			{

				codeRsp = new CodeRsp(RspCode.DELETE_FAILED);
				codeRsp.setMsg("删除失败！");
				return ResultUtil.getDefinedCodeResult(codeRsp);
			}
		}
		else
		{
			codeRsp = new CodeRsp(RspCode.OBJECT_IS_USED);
			codeRsp.setMsg("秘钥文件正在被使用！");
			return ResultUtil.getDefinedCodeResult(codeRsp);
		}
	}

	/**
	 * 获取Exchange连接信息
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/requestExchangeInfo", method = RequestMethod.POST)
	public @ResponseBody ResponseBean requestExchangeInfo(HttpServletRequest req) throws Exception
	{

		List<LdapData> ldapUsers = targetResSrv.getLdapUsers("");
		return ResultUtil.getSuccessResult(ldapUsers);
	}
}
