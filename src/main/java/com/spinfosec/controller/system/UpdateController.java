package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dao.entity.SpUpdateServerPackage;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.IDataClearSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.service.srv.IUpdateSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.*;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName UpdateController
 * @Description: 〈升级管理〉
 * @date 2019/1/21
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Api(value = "/system/update", tags = "升级管理")
@Controller
@RequestMapping("/system/update")
public class UpdateController
{
	private Logger logger = LoggerFactory.getLogger(UpdateController.class);

	@Autowired
	private IUpdateSrv updateSrv;

	@Autowired
	private IDataClearSrv dataClearSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 分页查询升级信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "分页查询升级管理信息", notes = "分页查询升级管理信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "每页显示条数", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "列表中的字段均可排序，默认version", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "order", value = "升序或降序", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "q", value = "暂不添加搜索功能", required = false, dataType = "", paramType = "query") })
	@RequestMapping(value = { "/page" }, method = RequestMethod.GET)
	public @ResponseBody ResponseBean pageUser(HttpServletRequest req, HttpServletResponse rsp)
	{
		Map<String, Object> queryMap = new HashMap<>();

		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		if (null == currentPageValue || null == pageSizeValue)
		{
			logger.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 基本版本
			String version = req.getParameter("version");
			if (StringUtils.isNotEmpty(version))
			{
				queryMap.put("version", version);
			}
			// 目标版本
			String bak1 = req.getParameter("bak1");
			if (StringUtils.isNotEmpty(bak1))
			{
				queryMap.put("bak1", bak1);
			}
			// 小版本
			String secondaryVersion = req.getParameter("secondaryVersion");
			if (StringUtils.isNotEmpty(secondaryVersion))
			{
				queryMap.put("secondaryVersion", secondaryVersion);
			}
			// 状态
			String deployStatus = req.getParameter("deployStatus");
			if (StringUtils.isNotEmpty(deployStatus))
			{
				queryMap.put("deployStatus", deployStatus);
			}
			// 上传时间
			String uploadTime = req.getParameter("uploadTime");
			if (StringUtils.isNotEmpty(uploadTime))
			{
				queryMap.put("uploadTime", uploadTime);
			}
			// 升级时间
			String deployTime = req.getParameter("deployTime");
			if (StringUtils.isNotEmpty(deployTime))
			{
				queryMap.put("deployTime", deployTime);
			}

		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			// 基本版本
			if (sort.equalsIgnoreCase("version"))
			{
				sort = "u.VERSION";
			}
			// 目标版本
			if (sort.equalsIgnoreCase("bak1"))
			{
				sort = "u.BAK1";
			}
			// 小版本
			if (sort.equalsIgnoreCase("secondaryVersion"))
			{
				sort = "u.SECONDARY_VERSION";
			}
			// 状态
			if (sort.equalsIgnoreCase("deployStatus"))
			{
				sort = "u.DEPLOY_STATUS";
			}
			// 上传时间
			if (sort.equalsIgnoreCase("uploadTime"))
			{
				sort = "u.UPLOAD_TIME";
			}
			// 升级时间
			if (sort.equalsIgnoreCase("deployTime"))
			{
				sort = "u.DEPLOY_TIME";
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<SpUpdateServerPackage> pageInfo = updateSrv.queryUpPackageByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageInfo.getTotal());
		dataJson.put("rows", pageInfo.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 上传升级包
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "上传升级包", notes = "上传升级包")
	@RequestMapping(value = "/uploadServer", method = RequestMethod.POST)
	public @ResponseBody ResponseBean uploadServer(HttpServletRequest req,
			@ApiParam(name = "file", value = "升级压缩包 支持上传格式为：tar.gz 单个上传文件大小不得超过1G", required = true) MultipartFile file)
			throws Exception
	{
		String description = req.getParameter("description");
		String versionType = req.getParameter("versionType");
		String product = System.getenv("PRODUCT"); // 产品名称
		String version = System.getenv("VERSION"); // 大版本号

		product = "SIMP_DBS_S";
		version = "1.5.0.0.4";

		ClassLoader classLoader = UpdateController.class.getClassLoader();
		URL document = classLoader.getResource("document");
		StringBuilder path = new StringBuilder(URLDecoder.decode(document.getPath(), "utf-8").replaceFirst("/", ""));
		path.append(File.separator).append("update").append(File.separator).append("server").append(File.separator);

		String originalFilename = file.getOriginalFilename();

		// 升级包名称
		String originalName = originalFilename.substring(0, originalFilename.lastIndexOf(".tar.gz"));

		// 记录操作日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		operateLogInfo.setOperation("上传服务器端升级包，升级包名称为：" + originalFilename);
		systemSrv.saveOperateLog(operateLogInfo);

		// 升级包字节流
		byte[] bytes = file.getBytes();
		if (bytes.length < 39)
		{
			logger.error(originalFilename + " : 升级包验证失败，请选择正确的升级包！");
			throw new TMCException(RspCode.UPDATE_PACKAGE_FORMAT_ERROR);
		}

		byte[] numByte = Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length);
		int num;
		String numStr = new String(numByte);
		try
		{
			num = Integer.parseInt(numStr);
		}
		catch (NumberFormatException e)
		{
			logger.error("升级包验证失败，请选择正确的升级包！", e);
			throw new TMCException(RspCode.UPDATE_PACKAGE_FORMAT_ERROR);
		}

		// 升级包包含信息，从二进制流中获取
		String filenameViaBinary = new String(Arrays.copyOfRange(bytes, bytes.length - (num + 2), bytes.length - 2));
		if (StringUtils.isEmpty(product) || !filenameViaBinary.startsWith(product))
		{
			logger.error(originalFilename + "升级包验证失败，请选择正确的升级包！");
			throw new TMCException(RspCode.UPDATE_PACKAGE_FORMAT_ERROR);
		}

		filenameViaBinary = filenameViaBinary.substring(product.length() + 1);
		String[] fileNameProps = filenameViaBinary.split("-");

		String destVersion = fileNameProps[1];

		int fileNamePropsLength = fileNameProps.length;
		String timeStamp = fileNameProps[fileNamePropsLength - 2];
		String secondaryVersion = fileNameProps[fileNamePropsLength - 1];

		path.append(originalName).append(File.separator);
		File direct = new File(path.toString());

		if (!direct.exists())
		{
			direct.mkdirs();
		}
		else
		{
			FileUtils.forceDelete(direct);
			direct.mkdirs();
		}

		if (direct.exists())
		{
			path.append(file.getOriginalFilename());
			File toFile = new File(path.toString());
			if (!toFile.exists())
			{
				toFile.createNewFile();
			}

			// 重复判断
			HashMap<String, Object> queryMap = new HashMap<>();
			queryMap.put("version", version);
			queryMap.put("bak1", destVersion);
			queryMap.put("timeStamp", timeStamp);
			queryMap.put("secondaryVersion", secondaryVersion);
			queryMap.put("srcPath", file.getOriginalFilename());

			List<SpUpdateServerPackage> serverPackages = updateSrv.queryUpPackage(queryMap);
			if (!serverPackages.isEmpty())
			{
				SpUpdateServerPackage serverPackage = serverPackages.get(0);
				String[] ids = new String[] { serverPackage.getId() };
				updateSrv.deleteDuplicateServerPackageData(ids);
			}
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(toFile));
			SpUpdateServerPackage serverPackage = new SpUpdateServerPackage();
			serverPackage.setId(GenUtil.getUUID());
			serverPackage.setSystemType(product);
			serverPackage.setVersionType(versionType);
			serverPackage.setVersion(version);
			serverPackage.setBak1(destVersion);
			serverPackage.setTimeStamp(timeStamp);
			serverPackage.setSecondaryVersion(secondaryVersion);
			serverPackage.setDescription(description);
			serverPackage.setFileSize(String.valueOf(file.getSize()));
			serverPackage.setFileType(file.getContentType());
			serverPackage.setPath(path.toString());
			serverPackage.setSrcPath(file.getOriginalFilename());
			serverPackage.setDeployStatus(Contants.UPDATE_DEPLOY_STATUS_UNDEPLOY);// 未部署
			serverPackage.setUploadTime(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN));

			updateSrv.saveUpdatePackage(serverPackage);
			logger.debug("server package upload ok, path = " + path.toString());
		}

		return ResultUtil.getSuccessResult();
	}

	@ApiOperation(value = "部署升级包", notes = "部署升级包")
	@ApiImplicitParam(name = "id", value = "升级包ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/deployServerPackage/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deployServerPackage(HttpServletRequest request, @PathVariable String id)
			throws Exception
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		// 升级前判断是否有策略在运行
		boolean runningTask = dataClearSrv.isExistRunningTask();
		if (runningTask)
		{
			codeRsp = new CodeRsp(RspCode.FAILURE);
			codeRsp.setMsg("任务中心有任务正在运行，不能进行升级！");
		}
		else
		{
			dataClearSrv.stopCrond();
			RspCode rspCode = updateSrv.deployServerPackage(id);
			codeRsp = new CodeRsp(rspCode);
		}
		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

	/**
	 * 升级后手动重启服务器
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "重启服务器", notes = "重启服务器")
	@RequestMapping(value = "/restartServer", method = RequestMethod.POST)
	public @ResponseBody ResponseBean restartServer(HttpServletRequest request)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		// 升级前判断是否有策略在运行
		boolean runningTask = dataClearSrv.isExistRunningTask();
		if (runningTask)
		{
			codeRsp = new CodeRsp(RspCode.FAILURE);
			codeRsp.setMsg("任务中心有任务正在运行，不能进行升级！");
		}
		else
		{
			RspCode rspCode = updateSrv.restartServer();
			codeRsp = new CodeRsp(rspCode);
		}
		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

	/**
	 * 删除升级包
	 * @param req
	 * @return
	 */
	@ApiOperation(value = "删除升级包", notes = "删除升级包")
	@RequestMapping(value = "/deleteServerPackage", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteServerPackage(HttpServletRequest req,
			@ApiParam(name = "ids", value = "删除升级包ID集合") @RequestBody Map<String, Object> parms)
	{
		List<String> idList = (List<String>) parms.get("ids");
		updateSrv.deleteServerPackage(idList);
		return ResultUtil.getSuccessResult();
	}

}
