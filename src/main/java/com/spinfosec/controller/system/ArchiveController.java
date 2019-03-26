package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.*;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.EventArchiveLogRsp;
import com.spinfosec.dto.pojo.system.SysOperateArchiveLogRsp;
import com.spinfosec.service.srv.IArchiveSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ArchiveController
 * @Description: 〈日志归档控制层〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/archive")
public class ArchiveController
{
	private static final Logger log = LoggerFactory.getLogger(ArchiveController.class);

	@Autowired
	private IArchiveSrv archiveSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 分页查询安全日志归档记录
	 *
	 * @param req {@link HttpServletRequest}
	 * @return {@code Map<String, Object>}
	 * @throws IOException
	 * @throws TMCException
	 */
	@RequestMapping(value = "/eventlog/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getEventlogByPage(HttpServletRequest req)
	{
		Map<String, Object> queryMap = new HashMap<>();

		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 非系统预置用户只能查询各自创建的信息
		String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		SpAdmins admins = systemSrv.getAdminById(mngUserId);
		if (admins.getDefinitionType().equalsIgnoreCase(Contants.C_USER_DEFINE))
		{
			queryMap.put("createdBy", mngUserId);
		}

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 状态
			String status = querJson.getString("status");
			if (StringUtils.isNotEmpty(status))
			{
				queryMap.put("status", status);
			}

			// 是否删除记录
			String isDel = querJson.getString("isDel");
			if (StringUtils.isNotEmpty(isDel))
			{
				queryMap.put("isDel", isDel);
			}

			// 创建者
			String createdUserName = querJson.getString("createdUserName");
			if (StringUtils.isNotEmpty(createdUserName))
			{
				queryMap.put("createdUserName", createdUserName);
			}

			// 备注
			String description = querJson.getString("description");
			if (StringUtils.isNotEmpty(description))
			{
				queryMap.put("description", description);
			}
		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("status"))
			{
				sort = "e.STATUS";
			}
			else if (sort.equalsIgnoreCase("isDel"))
			{
				sort = "e.ISDEL";
			}
			else if (sort.equalsIgnoreCase("incidentNum"))
			{
				sort = "e.INCIDENT_NUM";
			}
			else if (sort.equalsIgnoreCase("startDate"))
			{
				sort = "e.START_DATE";
			}
			else if (sort.equalsIgnoreCase("endDate"))
			{
				sort = "e.END_DATE";
			}
			else if (sort.equalsIgnoreCase("createDate"))
			{
				sort = "e.CREATE_DATE";
			}
			else if (sort.equalsIgnoreCase("createdUserName"))
			{
				sort = "a.NAME";
			}
		}
		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<EventArchiveLogRsp> pageList = archiveSrv.queryEventByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 分页查询系统操作日志归档记录
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/syslog/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getSyslogByPage(HttpServletRequest req)
	{
		Map<String, Object> queryMap = new HashMap<>();

		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 三权管理员用户只能查询自己创建的用户
		// String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		// queryMap.put("createdBy", mngUserId);

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 状态
			String status = querJson.getString("status");
			if (StringUtils.isNotEmpty(status))
			{
				queryMap.put("status", status);
			}
			// 是否删除记录
			String isDel = querJson.getString("isDel");
			if (StringUtils.isNotEmpty(isDel))
			{
				queryMap.put("isDel", isDel);
			}

			// 创建者
			String createdUserName = querJson.getString("createdUserName");
			if (StringUtils.isNotEmpty(createdUserName))
			{
				queryMap.put("createdUserName", createdUserName);
			}

			// 备注
			String description = querJson.getString("description");
			if (StringUtils.isNotEmpty(description))
			{
				queryMap.put("description", description);
			}
		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("status"))
			{
				sort = "sys.STATUS";
			}
			else if (sort.equalsIgnoreCase("isDel"))
			{
				sort = "sys.ISDEL";
			}
			else if (sort.equalsIgnoreCase("incidentNum"))
			{
				sort = "sys.INCIDENT_NUM";
			}
			else if (sort.equalsIgnoreCase("startDate"))
			{
				sort = "sys.START_DATE";
			}
			else if (sort.equalsIgnoreCase("endDate"))
			{
				sort = "sys.END_DATE";
			}
			else if (sort.equalsIgnoreCase("createDate"))
			{
				sort = "sys.CREATE_DATE";
			}
			else if (sort.equalsIgnoreCase("createdUserName"))
			{
				sort = "a.NAME";
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<SysOperateArchiveLogRsp> pageList = archiveSrv.querySysOperateByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);

	}

	/**
	 * 创建安全日志归档记录
	 * @param req
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/eventLog/initArchive", method = RequestMethod.POST)
	public @ResponseBody ResponseBean initArchive(HttpServletRequest req, @RequestBody SpEventArchiveLog archive)
			throws ParseException
	{

		// 判断是否有正在归档/恢复的任务正在进行。。。
		RspCode isRunning = this.isRun();
		if (isRunning.getCode().equals(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING.getCode()))
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING);
		}

		String userId = (String) req.getSession().getAttribute(SessionItem.userId.name());
		String folderPath = MemInfo.getServletContextPath();

		// 创建日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer opertion = new StringBuffer("新建安全日志归档记录，");
		opertion.append("起始时间：").append(format.format(archive.getStartDate())).append("，结束时间：")
				.append(format.format(archive.getEndDate())).append("，是否删除原记录：")
				.append(archive.getIsdel().equals(Contants.YES) ? "是" : "否");
		operateLogInfo.setOperation(opertion.toString());

		SpEventArchiveLog eventLogArchive = archiveSrv.createEventLogArchive(archive, userId, folderPath,
				operateLogInfo);
		// 开始归档
		createEventLogArchiveByMysqlDump(req, eventLogArchive.getId());
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 开始安全日志归档————MySQLDUMP
	 *
	 * @param req {@link HttpServletRequest}
	 * @return  {@link CodeRsp}
	 * @throws TMCException
	 */
	@RequestMapping(value = "/eventlogByMysqlDump/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean createEventLogArchiveByMysqlDump(HttpServletRequest req, @PathVariable String id)
			throws TMCException
	{
		SpEventArchiveLog acrhiveLogById = archiveSrv.getAcrhiveLogById(id);
		String userId = (String) req.getSession().getAttribute(SessionItem.userId.name());
		Thread startAcrhiveThread = new Thread()
		{
			@Override
			public void run()
			{
				archiveSrv.startEventAcrhive(acrhiveLogById, userId);
			}
		};
		startAcrhiveThread.start();

		return ResultUtil.getSuccessResult();
	}

	/**
	 * 恢复安全日志归档并更新该记录状态
	 *
	 * @param req {@link HttpServletRequest}
	 * @param id 事件日志id
	 * @return {@link CodeRsp}
	 * @throws TMCException
	 */
	@RequestMapping(value = "/recover/eventlog/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean recoverEventLogArchive(HttpServletRequest req, @PathVariable String id)
			throws TMCException
	{
		// 判断是否有正在归档/恢复的任务正在进行。。。
		RspCode isRunning = this.isRun();
		if (isRunning.getCode().equals(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING.getCode()))
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING);
		}

		SpEventArchiveLog spEventArchiveLog = archiveSrv.startEventRecover(id);

		// 创建日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer opertion = new StringBuffer("恢复安全日志归档记录，");
		opertion.append("起始时间：")
				.append(spEventArchiveLog.getStartDate() != null ? format.format(spEventArchiveLog.getStartDate()) : "")
				.append("，结束时间：")
				.append(spEventArchiveLog.getEndDate() != null ? format.format(spEventArchiveLog.getEndDate()) : "");
		operateLogInfo.setOperation(opertion.toString());
		try
		{
			Thread recoverThread = new Thread()
			{
				@Override
				public void run()
				{
					archiveSrv.recoverEventLogArchive(spEventArchiveLog, operateLogInfo);
				}
			};
			if (recoverThread.isAlive())
			{
				recoverThread.interrupt();
				archiveSrv.updateEventArchiveLog(spEventArchiveLog, Contants.LOG_RECOVER_FAIL);
			}
			recoverThread.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			archiveSrv.updateEventArchiveLog(spEventArchiveLog, Contants.LOG_RECOVER_FAIL);
			log.error("安全日志恢复出错！错误原因：", e);
		}

		return ResultUtil.getSuccessResult();
	}

	/**
	 * 创建系统操作日志归档记录
	 * @param req
	 * @param operateArchiveLog
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/syslog/initArchive", method = RequestMethod.POST)
	public @ResponseBody ResponseBean initSyslogArchive(HttpServletRequest req,
			@RequestBody SpSystemOperateArchiveLog operateArchiveLog) throws ParseException
	{

		// 判断是否有正在归档/恢复的任务正在进行。。。
		RspCode isRunning = this.isRun();
		if (isRunning.getCode().equals(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING.getCode()))
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING);
		}

		String userId = (String) req.getSession().getAttribute(SessionItem.userId.name());
		String folderPath = MemInfo.getServletContextPath();

		// 创建日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer opertion = new StringBuffer("新建系统操作日志归档记录，");
		opertion.append("起始时间：").append(format.format(operateArchiveLog.getStartDate())).append("，结束时间：")
				.append(format.format(operateArchiveLog.getEndDate())).append("，是否删除原记录：")
				.append(operateArchiveLog.getIsdel().equals(Contants.YES) ? "是" : "否");
		operateLogInfo.setOperation(opertion.toString());

		SpSystemOperateArchiveLog sysOperateLog = archiveSrv.createSysOperateLogArchive(operateArchiveLog, userId,
				folderPath, operateLogInfo);
		// 开始归档
		createSysLogArchiveByMysqlDump(req, sysOperateLog.getId());
		return ResultUtil.getSuccessResult(sysOperateLog);
	}

	/**
	 * 开始系统操作日志归档————MySQLDUMP
	 * @param req
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/syslogByMysqlDump/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean createSysLogArchiveByMysqlDump(HttpServletRequest req, @PathVariable String id)
			throws TMCException
	{
		String userId = (String) req.getSession().getAttribute(SessionItem.userId.name());
		SpSystemOperateArchiveLog operateArchiveLogById = archiveSrv.getOperateArchiveLogById(id);
		Thread startAcrhiveThread = new Thread()
		{
			@Override
			public void run()
			{
				archiveSrv.startSysOperaRecover(operateArchiveLogById, userId);
			}
		};

		startAcrhiveThread.start();
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 恢复安全日志
	 * @param req
	 * @param id
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/recover/systlog/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean recoverSysLogArchive(HttpServletRequest req, @PathVariable String id)
			throws TMCException
	{

		// 判断是否有正在归档/恢复的任务正在进行。。。
		RspCode isRunning = this.isRun();
		if (isRunning.getCode().equals(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING.getCode()))
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING);
		}

		SpSystemOperateArchiveLog operateArchiveLogById = archiveSrv.startSysOperaRecover(id);

		// 创建日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(req);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer opertion = new StringBuffer("恢复系统操作日志归档记录，");
		opertion.append("起始时间：").append(format.format(operateArchiveLogById.getStartDate())).append("，结束时间：")
				.append(format.format(operateArchiveLogById.getEndDate()));
		operateLogInfo.setOperation(opertion.toString());
		try
		{
			Thread recoverThread = new Thread()
			{
				@Override
				public void run()
				{
					archiveSrv.recoverSysOperaAcrhive(operateArchiveLogById, operateLogInfo);
				}
			};
			recoverThread.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			archiveSrv.updateOperateArchiveLog(operateArchiveLogById, Contants.LOG_RECOVER_FAIL);
			log.error("安全日志恢复出错！错误原因：", e);
		}
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 删除安全日志归档记录
	 * @param req
	 * @param map body中的参数
	 * @return
	 */
	@RequestMapping(value = "/eventlog", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteEventLogArchiveByIds(HttpServletRequest req,
			@RequestBody Map<String, Object> map)

	{
		List<String> ids = (List<String>) map.get("ids");
		if (null != ids)
		{
			archiveSrv.deleteEventArchiveLog(ids);
			return ResultUtil.getSuccessResult();
		}
		else
		{
			log.error("delete by ids,ids is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
	}

	/**
	 * 删除系统日志归档记录
	 * @param req
	 * @param map body中的参数
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/syslog", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteSysLogArchiveByIds(HttpServletRequest req,
			@RequestBody Map<String, Object> map)
			throws TMCException
	{
		List<String> ids = (List<String>) map.get("ids");
		if (null != ids)
		{
			archiveSrv.deleteOperateArchiveLog(ids);
			return ResultUtil.getSuccessResult();
		}
		else
		{
			log.error("delete by ids,ids is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
	}

	/**
	 * 安全日志归档文件下载
	 * @param request
	 * @param id
	 * @param response
	 */
	@RequestMapping(value = "/event/download/{id}", method = RequestMethod.GET)
	public void downLoadAllData(HttpServletRequest request, HttpServletResponse response, @PathVariable String id)
	{
		OutputStream toClient = null;
		InputStream fis = null;
		File downFile = null;
		try
		{
			SpEventArchiveLog acrhiveLogById = archiveSrv.getAcrhiveLogById(id);

			// 创建日志
			SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer opertion = new StringBuffer("下载安全日志归档记录，");
			opertion.append("起始时间：")
					.append(acrhiveLogById.getStartDate() != null ? format.format(acrhiveLogById.getStartDate()) : "")
					.append("，结束时间：")
					.append(acrhiveLogById.getEndDate() != null ? format.format(acrhiveLogById.getEndDate()) : "");

			operateLogInfo.setOperation(opertion.toString());

			String filePath = acrhiveLogById.getPath();
			// path是指欲下载的文件的路径。
			downFile = new File(filePath + ".zip");
			if (!downFile.exists())
			{
				log.debug("安全日志归档文件不存在!");
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
			response.reset();
			// 设置response的Header
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"",
					new String(downFile.getName().getBytes("utf-8"), "ISO-8859-1")));
			response.addHeader("Content-Length", "" + downFile.length());
			response.setContentType("application/octet-strea; charset=utf-8");
			// 输出流定向到http response中
			toClient = new BufferedOutputStream(response.getOutputStream());
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(downFile));
			byte[] buffer = new byte[1204];
			int len = 0;
			while ((len = fis.read(buffer)) != -1)
			{
				toClient.write(buffer, 0, len);
				toClient.flush();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("安全日志数据下载，失败原因:", e);
			throw new TMCException(RspCode.EXPORT_FILE_ERROR, e);
		}
		finally
		{
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(toClient);
		}
	}

	/**
	 * 安全日志上传压缩包进行恢复
	 * @param request
	 * @param req
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/eventRecover/upload", method = RequestMethod.POST)
	public @ResponseBody ResponseBean recoverAllData(HttpServletRequest request, MultipartHttpServletRequest req,
			HttpServletResponse rsp)
	{
		// 判断是否有正在归档/恢复的任务正在进行。。。
		RspCode isRunning = this.isRun();
		if (isRunning.getCode().equals(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING.getCode()))
		{
			throw new TMCException(RspCode.LOG_ARCHIVE_OR_RECOVER_RUNNING);
		}

		String attPath = "";
		Iterator<String> itr = req.getFileNames();
		MultipartFile mpf = null;
		CodeRsp result = new CodeRsp(RspCode.SUCCESS);
		try
		{
			while (itr.hasNext())
			{
				mpf = req.getFile(itr.next());
				// 保存文件
				attPath = MemInfo.getServletContextPath() + "logArchive" + File.separator;
				// 判断是否存在相应的压缩包,存在则备份
				String zipName = mpf.getOriginalFilename();
				File oldZip = new File(attPath + zipName);
				File backZip = new File(attPath + zipName + ".bak_" + System.currentTimeMillis());
				if (oldZip.exists())
				{
					org.apache.commons.io.FileUtils.moveFile(oldZip, backZip);
				}
				// 判断zip包是否解压缩
				if (StringUtils.isNotEmpty(mpf.getOriginalFilename())
						&& mpf.getOriginalFilename().toLowerCase().endsWith(".zip"))
				{
					FileUtils.SaveFileFromInputStream(mpf.getInputStream(), attPath, mpf.getOriginalFilename());
				}

				// 解压zip包,解压后将zip文件删除
				File[] unzipFileList = Zip4JUtil.unzip(attPath + mpf.getOriginalFilename(), attPath,
						Zip4JUtil.DATA_ZIP_PWD);
				String upzipPath = "";
				// 判端压缩包内容是否正确
				boolean isValid = false;
				for (File file : unzipFileList)
				{
					if (file.getName().contains("sp_dscvr_files"))
					{
						upzipPath = file.getParentFile().getAbsolutePath();
						isValid = true;
						break;
					}
				}

				if (isValid)
				{
					// 记录操作日志
					SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
					operateLogInfo.setOperation("安全日志备份恢复-上传数据包恢复数据！数据包名称：" + mpf.getOriginalFilename());
					// 创建归档记录
					SpEventArchiveLog eventArchiveLog = archiveSrv.processArchiveLogRecord(upzipPath,
							(null != request.getSession().getAttribute(SessionItem.userId.name()))
									? request.getSession().getAttribute(SessionItem.userId.name()).toString()
									: "");

					// 开始恢复进程
					Thread recoverThread = new Thread()
					{
						@Override
						public void run()
						{
							archiveSrv.recoverEventLogArchive(eventArchiveLog, operateLogInfo);
						}
					};
					recoverThread.start();

					// 删除备份的旧文件
					org.apache.commons.io.FileUtils.deleteQuietly(backZip);
				}
				else
				{
					// 删除解压的文件
					for (File file : unzipFileList)
					{
						org.apache.commons.io.FileUtils.deleteQuietly(file);
					}
					String path = attPath + mpf.getOriginalFilename();
					org.apache.commons.io.FileUtils.forceDelete(new File(path));
					if (backZip.exists())
					{
						org.apache.commons.io.FileUtils.moveFile(backZip, oldZip);
					}
					result = new CodeRsp(RspCode.DB_BACKUP_PACAGE_VALID);
					return ResultUtil.getDefinedCodeResult(result);
				}

			}
		}
		catch (ZipException zipEx)
		{
			log.error("该文件不是合法的zip文件", zipEx);
			result = new CodeRsp(RspCode.RAR_TYPE_ERROR);
			return ResultUtil.getFailResult(result);
		}
		catch (Exception e)
		{
			log.error("安全日志上传恢复失败！", e);
			result = new CodeRsp(RspCode.FAILURE);
			result.setMsg("安全日志上传恢复失败!");
			return ResultUtil.getFailResult(result);
		}
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
