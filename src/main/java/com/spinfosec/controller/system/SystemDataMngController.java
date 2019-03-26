package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpDataBackups;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.SystemArchiveLogRsp;
import com.spinfosec.service.srv.IArchiveSrv;
import com.spinfosec.service.srv.ISystemDataMngSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.*;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SystemDataMngController
 * @Description: 〈系统备份〉
 * @date 2019/1/16
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Api(value = "/system/dataMngr", tags = "系统备份")
@RestController
@RequestMapping("/system/dataMng")
public class SystemDataMngController
{
	private Logger log = LoggerFactory.getLogger(SystemDataMngController.class);

	@Autowired
	private ApplicationProperty property;

	@Autowired
	private ISystemDataMngSrv systemDataMngSrv;

	@Autowired
	private IArchiveSrv archiveSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 分页查询系统备份信息
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws TMCException
	 */
	@ApiOperation(value = "分页查询系统备份信息", notes = "分页查询系统备份信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "每页显示条数", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "可排序字段：<br/>createDate 创建时间<br/>status 状态<br/>createdUserName 创建者", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "order", value = "升序或降序", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "系统备份类型<br/> 业务数据 BUSI<br/>系统数据 SYSTEM<br/>安全数据 SAFE", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "q", value = "查询条件JSON格式<br/>createDate_startTime 备份开始时间<br/>createDate_endTime 备份结束时间", required = false, dataType = "", paramType = "query") })
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean pageDataBackup(HttpServletRequest req) throws IOException, TMCException
	{
		Map<String, Object> queryMap = new HashMap<>();

		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");
		String type = req.getParameter("type");

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 只能查看自己创建的系统备份信息 sysadmin可以查看全部系统备份信息
		String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		SpAdmins admins = systemSrv.getAdminById(mngUserId);
		if (!admins.getDefinitionType().equalsIgnoreCase(Contants.C_PRE_DEFINE))
		{
			queryMap.put("createdBy", mngUserId);
		}

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 备份开始时间
			String createDate_startTime = (String) querJson.get("createDate_startTime");
			if (StringUtils.isNotEmpty(createDate_startTime))
			{
				queryMap.put("createDate_startTime", createDate_startTime);
			}

			// 备份结束时间
			String createDate_endTime = (String) querJson.get("createDate_endTime");
			if (StringUtils.isNotEmpty(createDate_endTime))
			{
				queryMap.put("createDate_endTime", createDate_endTime);
			}
		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("createDate"))
			{
				sort = "d.CREATE_DATE";
			}
			else if (sort.equalsIgnoreCase("status"))
			{
				sort = "d.STATUS";
			}
			else if (sort.equalsIgnoreCase("createdUserName"))
			{
				sort = "d.USERNAME";
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);
		queryMap.put("type", type);

		PageInfo<SystemArchiveLogRsp> pageInfo = systemDataMngSrv.querySpAdminsByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageInfo.getTotal());
		dataJson.put("rows", pageInfo.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 创建系统备份记录
	 * @param request
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "记录归档信息并开始归档", notes = "开始归档")
	@ApiImplicitParam(name = "type", value = "系统备份类型<br/> 业务数据 BUSI<br/>系统数据 SYSTEM<br/>安全数据 SAFE", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/initArchive/{type}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean initArchive(HttpServletRequest request, @PathVariable String type)
	{
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
		String userId = null != request.getSession().getAttribute(SessionItem.userId.name())
				? request.getSession().getAttribute(SessionItem.userId.name()).toString()
				: "";
		SpDataBackups dataBackups = systemDataMngSrv.createSystemData(type, operateLogInfo, userId);
		// TODO 此处将系统备份记录对象返回 页面再次发送请求开始备份进程 或 后台直接调用开始备份进程接口
		createAllDataAchive(request, dataBackups.getId());
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 开始归档进程
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据id对某条记录进行归档", notes = "开始归档")
	@ApiImplicitParam(name = "id", value = "记录ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/achive/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean createAllDataAchive(HttpServletRequest request, @PathVariable String id)
	{
		SpDataBackups dataBackups = systemDataMngSrv.getystemAchiveLogById(id);
		try
		{
			Thread archiveThread = new Thread()
			{
				@Override
				public void run()
				{
					systemDataMngSrv.achiveAllData(dataBackups);
				}
			};

			archiveThread.start();
			archiveThread.join(Long.parseLong(property.getLogArchiveTimeOut()));
			if (archiveThread.isAlive())
			{
				archiveThread.interrupt();
				systemDataMngSrv.updateDataBackups(dataBackups, Contants.LOG_ARCHIVE_FAIL);
			}
		}
		catch (InterruptedException e)
		{
			systemDataMngSrv.updateDataBackups(dataBackups, Contants.LOG_ARCHIVE_FAIL);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			systemDataMngSrv.updateDataBackups(dataBackups, Contants.LOG_ARCHIVE_FAIL);
			throw e;
		}
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 下载备份数据
	 * @param request
	 * @param id
	 * @param rsp
	 */
	@ApiOperation(value = "根据ID下载备份数据", notes = "根据ID下载备份数据")
	@ApiImplicitParam(name = "id", value = "备份ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/file/download/{id}", method = RequestMethod.GET)
	public void downLoadAllData(HttpServletRequest request, @PathVariable String id, HttpServletResponse rsp)
	{
		InputStream fis = null;
		OutputStream toClient = null;
		try
		{
			SpDataBackups dataBackups = systemDataMngSrv.getystemAchiveLogById(id);
			// 下载次数+1
			dataBackups.setDownloadOptlock(dataBackups.getDownloadOptlock() + 1);
			systemDataMngSrv.updateDataBackups(dataBackups);

			// 压缩包路径
			String descFilePath = dataBackups.getLocalpath() + ".zip";

			File downFile = new File(descFilePath);
			if (!downFile.exists())
			{
				log.debug("系统备份数据下载，文件不存在!");
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}

			// 清空response
			rsp.reset();
			// 设置response的Header
			rsp.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"",
					new String(downFile.getName().getBytes("utf-8"), "ISO-8859-1")));
			rsp.addHeader("Content-Length", "" + downFile.length());

			rsp.setContentType("application/octet-strea; charset=utf-8");
			// 输出流定向到http response中
			toClient = new BufferedOutputStream(rsp.getOutputStream());
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(downFile));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) != -1)
			{
				toClient.write(buffer, 0, len);
				toClient.flush();
			}
		}
		catch (Exception e)
		{
			log.error("系统备份数据下载，失败原因:" + e);
			throw new TMCException(RspCode.DOWNLOAD_ERROR, e);
		}
		finally
		{
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(toClient);
		}
	}

	/**
	 * 判断当前是否有 安全日志 系统日志 系统备份任务在归档或恢复
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "判断当前是否有 安全日志 系统日志 系统备份任务在归档或恢复", notes = "判断当前是否有 安全日志 系统日志 系统备份任务在归档或恢复")
	@RequestMapping(value = "/archiveValid", method = RequestMethod.GET)
	public @ResponseBody ResponseBean archiveValid(HttpServletRequest request)
	{
		RspCode running = archiveSrv.isArchiveOrRecoverRunning(Contants.LOG_ARCHIVE_RUNNING,
				Contants.LOG_RECOVER_RUNNING);
		return ResultUtil.getSuccessResult(new CodeRsp(running));
	}

	/**
	 * 根据ID进行恢复
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID进行系统备份数据恢复", notes = "根据ID进行系统备份数据恢复")
	@ApiImplicitParam(name = "id", value = "备份ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/recover/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean recoverAllData(HttpServletRequest request, @PathVariable String id)
	{

		// TODO 此处单独发送请求判断 或直接在后台进行判断 判断是否有正在归档/恢复的任务正在进行。。。
		RspCode isRunning = this.isRun();
		if (isRunning.getCode().equals(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING.getCode()))
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING);
		}
		SpDataBackups dataBackups = systemDataMngSrv.getystemAchiveLogById(id);
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
		try
		{
			Thread recoverThread = new Thread()
			{
				@Override
				public void run()
				{
					systemDataMngSrv.recoverDataById(dataBackups, operateLogInfo);
				}
			};
			recoverThread.start();
			recoverThread.join(Long.parseLong(property.getLogArchiveTimeOut()));
			if (recoverThread.isAlive())
			{
				recoverThread.interrupt();
				systemDataMngSrv.updateDataBackups(dataBackups, Contants.LOG_RECOVER_FAIL);
			}
		}
		catch (TMCException e)
		{
			systemDataMngSrv.updateDataBackups(dataBackups, Contants.LOG_RECOVER_FAIL);
			throw e;
		}
		catch (InterruptedException e)
		{
			systemDataMngSrv.updateDataBackups(dataBackups, Contants.LOG_RECOVER_FAIL);
			e.printStackTrace();
		}
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 上传数据压缩包进行系统备份恢复
	 * @param request
	 * @param mpf
	 * @param rsp
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "上传数据压缩包进行系统备份恢复", notes = "上传数据压缩包进行系统备份恢复")
	@ApiImplicitParam(name = "type", value = "系统备份类型<br/> 业务数据 BUSI<br/>系统数据 SYSTEM<br/>安全数据 SAFE", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/upload/recover/{type}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean recoverFileUpload(HttpServletRequest request,
			@ApiParam(name = "mpf", value = "恢复数据zip压缩包", required = true) MultipartFile mpf, HttpServletResponse rsp,
			@PathVariable String type)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		// 每次恢复前先清空临时文件目录
		String tmpPath = MemInfo.getServletContextPath() + "/archive/";
		File tmpFolder = new File(tmpPath);
		File[] tmpFiles = tmpFolder.listFiles();
		if (null != tmpFiles && tmpFiles.length > 0)
		{
			for (int i = 0; i < tmpFiles.length; i++)
			{
				if (null != tmpFiles[i] && tmpFiles[i].exists())
				{
					tmpFiles[i].delete();
				}
			}
		}

		String attPath = "";

		try
		{
			// 保存文件
			attPath = MemInfo.getServletContextPath() + File.separator + "systemArchive/";
			// 判断zip包是否解压缩
			if (StringUtils.isNotEmpty(mpf.getOriginalFilename())
					&& mpf.getOriginalFilename().toLowerCase().endsWith(".zip"))
			{

				FileUtils.SaveFileFromInputStream(mpf.getInputStream(), attPath, mpf.getOriginalFilename());
				// 解压zip包,解压后将zip文件删除

				File[] unzipFileList = Zip4JUtil.unzip(attPath + mpf.getOriginalFilename(), attPath,
						Zip4JUtil.DATA_ZIP_PWD);
				String logPath = "";
				boolean isValid = false;

				if (unzipFileList.length != 0)
				{
					for (File file : unzipFileList)
					{
						if (file.getName().contains(type))
						{
							isValid = true;
							logPath = file.getParentFile().getAbsolutePath();
							break;
						}
					}
					if (isValid)
					{
						// 记录操作日志
						SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
						operateLogInfo.setOperation("数据备份恢复-上传数据包恢复数据！数据包名称：" + mpf.getOriginalFilename());

						SpDataBackups dataBackups = systemDataMngSrv.createDataBackups(logPath, type,
								null != request.getSession().getAttribute(SessionItem.userId.name())
										? request.getSession().getAttribute(SessionItem.userId.name()).toString()
										: "");
						Thread recoverThread = new Thread()
						{
							@Override
							public void run()
							{
								systemDataMngSrv.recoverDataById(dataBackups, operateLogInfo);
							}
						};

						recoverThread.start();
					}
					else
					{
						codeRsp = new CodeRsp(RspCode.DB_BACKUP_PACAGE_VALID);
					}

				}
				else
				{
					codeRsp = new CodeRsp(RspCode.DB_BACKUP_PACAGE_VALID);
				}

			}
			else
			{
				codeRsp = new CodeRsp(RspCode.RAR_TYPE_ERROR);
			}

		}
		catch (Exception e)
		{
			if (e instanceof TMCException)
			{
				TMCException tmcException = (TMCException) e;
				codeRsp = new CodeRsp(tmcException.getErrCode());
			}
		}
		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

	/**
	 * 根据ID删除系统备份数据
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID删除记录和数据", notes = "根据ID删除记录和数据")
	@ApiImplicitParam(name = "id", value = "备份ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean deleteLog(HttpServletRequest request, @PathVariable String id)
	{
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
		systemDataMngSrv.deleteLog(id, operateLogInfo);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 判断当前是否有 安全日志 系统日志 系统备份任务在归档或恢复
	 * @return
	 */
	private RspCode isRun()
	{
		RspCode isRunning = archiveSrv.isArchiveOrRecoverRunning(Contants.LOG_ARCHIVE_RUNNING,
				Contants.LOG_RECOVER_RUNNING);
		return isRunning;
	}
}
