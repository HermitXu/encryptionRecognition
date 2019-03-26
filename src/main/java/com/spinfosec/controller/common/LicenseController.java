package com.spinfosec.controller.common;

import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.ILicenseSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.LicenseUtil;
import com.spinfosec.utils.OperateLogUtil;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName LicenseController
 * @Description: 〈License许可管理控制层〉
 * @date 2018/11/8
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/license")
public class LicenseController
{
	private Logger log = LoggerFactory.getLogger(LicenseController.class);

	@Autowired
	private ILicenseSrv licenseSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 获取机器码
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mcode", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getMcode(HttpServletRequest req) throws Exception
	{
		String mode = LicenseUtil.getLocalMcode();
		return ResultUtil.getSuccessResult(mode);
	}

	/**
	* 获取当前License
	* @param req
	* @return
	*/
	@RequestMapping(value = "/getCurrentLicenseInfo", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getCurrentLicenseInfo(HttpServletRequest req)
	{
		return ResultUtil.getSuccessResult(licenseSrv.getCurrentLicenseInfo());
	}

	/**
	* 上传License
	* @param req
	* @param request
	* @return
	* @throws Exception
	*/
	@RequestMapping(value = "/uploadLicense", method = RequestMethod.POST)
	public @ResponseBody ResponseBean uploadLicense(MultipartHttpServletRequest req, HttpServletRequest request)
			throws Exception
	{
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
		StringBuffer operation = new StringBuffer();
		File licDirect = null;
		try
		{
			log.info("start upload!");
			Iterator<String> iterator = req.getFileNames();
			String fileName = "";
			while (iterator.hasNext())
			{
				String name = iterator.next();
				List<MultipartFile> files = req.getFiles(name);
				for (MultipartFile file : files)
				{
					// 验证升级包格式是否正确
					fileName = file.getOriginalFilename();
					Pattern pattern = Pattern.compile(".*\\.(LIC)$", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(fileName);
					if (!matcher.matches())
					{
						operation.append("上传许可失败，失败原因：证书文件格式不正确！");
						operateLogInfo.setOperation(operation.toString());
						systemSrv.saveOperateLog(operateLogInfo);
						log.error(fileName + " : 证书文件格式不正确，请选择正确的文件证书！");
						return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.LICENSE_FILE_NOT_RIGHT));
					}
					byte[] bytes = file.getBytes();

					StringBuilder path = new StringBuilder();
					path.append(MemInfo.getServletContextPath()).append(File.separatorChar).append("document")
							.append(File.separator).append("license");
					licDirect = new File(path.toString());
					saveFileToLocal(licDirect, req, file, bytes);
				}
			}
			CodeRsp codeRsp = licenseSrv.installAndVerifyLicense();
			if (!RspCode.SUCCESS.getCode().equals(codeRsp.getCode()))
			{
				operation.append("上传许可失败，失败原因：" + codeRsp.getMsg());
				operateLogInfo.setOperation(operation.toString());
				systemSrv.saveOperateLog(operateLogInfo);
				return ResultUtil.getDefinedCodeResult(codeRsp);
			}
			else
			{
				operation.append("上传许可成功！");
				operateLogInfo.setOperation(operation.toString());
				systemSrv.saveOperateLog(operateLogInfo);
				return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.SUCCESS));
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.LICENSE_INSTALL_FAIL);
		}
		finally
		{
			// 安装完成后删除license上传包
			if (null != licDirect && licDirect.isDirectory())
			{
				FileUtils.cleanDirectory(licDirect);
			}
		}
	}

	private void saveFileToLocal(File direct, MultipartHttpServletRequest req, MultipartFile file, byte[] bytes)
			throws IOException
	{
		if (!direct.exists())
		{
			direct.mkdirs();
		}
		else
		{
			// 清空目录
			FileUtils.cleanDirectory(direct);
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
}
