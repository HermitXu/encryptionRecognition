package com.spinfosec.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.controller.event.CensorshipReportController;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.service.srv.IAuthSrv;
import com.spinfosec.service.srv.ILicenseSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.IpUtils;
import com.spinfosec.utils.OperateLogUtil;
import com.spinfosec.utils.ResultUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName CommController
 * @Description: 〈公共接口〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
@Api(value = "/common", tags = "公共接口")
@RestController
@RequestMapping("/common")
public class CommController
{
	private static final Logger log = LoggerFactory.getLogger(CommController.class);

	@Autowired
	private IAuthSrv authSrv;

	@Autowired
	private ISystemSrv systemSrv;

	@Autowired
	private ILicenseSrv licenseSrv;

	/**
	 * 注销
	 * @param  {@link HttpServletRequest}
	 * @return {@link CodeRsp} 状态码
	 */
	@ApiOperation(value = "注销", notes = "注销")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody ResponseBean logout(HttpServletRequest request, HttpServletResponse response)
	{
		SpSystemOperateLogInfo systemOperateLogInfo = OperateLogUtil.packageSysLog(request);
		systemOperateLogInfo.setOperation("用户注销，注销IP：" + IpUtils.getIpAddr(request));
		systemOperateLogInfo.setResult(0);

		// 销毁session
		HttpSession session = request.getSession(true);
		String userName = (String) session.getAttribute(SessionItem.userName.name());
		// 如果存在用户则保存日志
		if (StringUtils.isNotEmpty(userName))
		{
			systemSrv.saveOperateLog(systemOperateLogInfo);
		}
		session.invalidate();
		log.info("用户" + userName + "退出...");

		return ResultUtil.getSuccessResult();
	}

	/**
	 * 获取菜单
	 * @param req
	 * @param resp
	 * @return
	 */
	@ApiOperation(value = "获取权限菜单", notes = "获取登录用户的权限菜单")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/getMenu/{userId}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getMeun(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable String userId) throws IOException
	{
		// 判断是否使用ukey
		String isUkey = MemInfo.getSecPasswordPolicyInfo().get(SessionItem.ukeyEnable.name());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("isUkey", isUkey);
		List<TreeData> treeData = new ArrayList<>();
		try
		{
			// 验证License 许可不合法则跳转到许可管理界面
			if (!licenseSrv.checkLicense())
			{
				treeData = packLicenseMenu();
				jsonObject.put("menu", treeData);
				return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.LICENSE_VERIFY_FAIL_NEND_TO_REUPLOAD),
						jsonObject);
			}
			else
			{
				// 许可剩余有效期
				if (null != MemInfo.getServletContext().getAttribute("licenseLive")
						&& StringUtils.isNotEmpty(MemInfo.getServletContext().getAttribute("licenseLive").toString()))
				{
					jsonObject.put("licenseLive",
							Integer.parseInt(MemInfo.getServletContext().getAttribute("licenseLive").toString()));
				}
				treeData = authSrv.initRoleFunctions(userId);
				jsonObject.put("menu", treeData);
				return ResultUtil.getSuccessResult(jsonObject);
			}
		}
		catch (Exception e)
		{
			treeData = packLicenseMenu();
			jsonObject.put("menu", treeData);
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.LICENSE_VERIFY_FAIL_NEND_TO_REUPLOAD),
					jsonObject);
		}

	}

	/**
	 * 下载Ukey驱动
	 * @param req
	 * @param rsp
	 * @throws Exception
	 */
	@ApiOperation(value = "下载Ukey驱动程序", notes = "下载Ukey驱动程序")
	@RequestMapping(value = "/downloadUkeyDrive", method = RequestMethod.GET)
	public void downloadUkeyDrive(HttpServletRequest req, HttpServletResponse rsp) throws Exception
	{
		try
		{
			OutputStream toClient = null;
			InputStream fis = null;
			ClassLoader classLoader = CommController.class.getClassLoader();
			URL document = classLoader.getResource("document");
			String path = URLDecoder.decode(document.getPath(), "utf-8").replaceFirst("/", "");
			String drivePath = path + File.separator + "sp_security.exe";
			File driveFile = new File(drivePath);
			if (!driveFile.exists())
			{
				log.error("Ukey驱动文件不存在！");
				throw new TMCException(RspCode.OBJECE_NOT_EXIST);
			}
			// 清空response
			rsp.reset();
			// 设置response的Header
			rsp.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"",
					new String(driveFile.getName().getBytes("utf-8"), "ISO-8859-1")));
			rsp.addHeader("Content-Length", "" + driveFile.length());
			rsp.setContentType("application/octet-strea; charset=utf-8");
			toClient = new BufferedOutputStream(rsp.getOutputStream());
			fis = new BufferedInputStream(new FileInputStream(driveFile));
			byte[] pdfByte = new byte[fis.available()];
			fis.read(pdfByte);
			toClient.write(pdfByte);
			toClient.flush();
			fis.close();
			toClient.close();
		}
		catch (Exception e)
		{
			log.error("Ukey驱动下载失败", e);
		}

	}

	private List<TreeData> packLicenseMenu()
	{
		List<TreeData> treeData = new ArrayList<>();
		// 组装父菜单
		TreeData parentTree = new TreeData();
		parentTree.setIcon("settings");
		parentTree.setName("系统");
		parentTree.setUrl("/pages/system");
		parentTree.setIsShow(true);
		parentTree.setShowChild(true);

		// 创建父菜单中的子菜单
		List<TreeData> childrenList = new ArrayList<>();

		TreeData licenseTree = new TreeData();
		licenseTree.setName("许可认证");
		licenseTree.setUrl("/pages/system/license");
		licenseTree.setIsShow(true);

		// 加入到子菜单
		childrenList.add(licenseTree);
		parentTree.setChildren(childrenList);

		treeData.add(parentTree);
		return treeData;
	}

}
