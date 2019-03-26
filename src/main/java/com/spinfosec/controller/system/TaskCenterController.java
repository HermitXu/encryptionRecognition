package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpTaskFiles;
import com.spinfosec.dao.entity.SpTaskSipped;
import com.spinfosec.dto.enums.OperationStatus;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.JobMsg;
import com.spinfosec.dto.pojo.system.OperateMsg;
import com.spinfosec.dto.pojo.system.TaskCenterRsp;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.service.srv.ITaskCenterSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TaskCenterController
 * @Description: 〈任务中心控制层〉
 * @date 2018/10/26
 * All rights Reserved, Designed By SPINFO
 */
@Api(value = "/system/taskCenter", tags = "任务列表")
@RestController
@RequestMapping("/system/taskCenter")
public class TaskCenterController
{

	private Logger log = LoggerFactory.getLogger(TaskCenterController.class);

	@Autowired
	private ITaskCenterSrv taskCenterSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 分页获取任务中心信息(列表获取最新的任务信息isLast = "1")
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getTaskCentByPage(HttpServletRequest req) throws Exception
	{
		Map<String, Object> queryMap = new HashMap<>();

		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		// 非系统预置用户只能查询各自创建的信息
		String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		SpAdmins admins = systemSrv.getAdminById(mngUserId);
		if (admins.getDefinitionType().equalsIgnoreCase(Contants.C_USER_DEFINE))
		{
			queryMap.put("createdBy", mngUserId);
		}

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 任务名称（即策略名称）
			String name = querJson.getString("name");
			if (StringUtils.isNotEmpty(name))
			{
				queryMap.put("name", name);
			}
			// 任务类型
			String type = querJson.getString("type");
			if (StringUtils.isNotEmpty(type))
			{
				queryMap.put("type", type);
			}
			// 任务目标
			String ip = querJson.getString("ip");
			if (StringUtils.isNotEmpty(ip))
			{
				queryMap.put("ip", ip);
			}
			// 任务开始起始时间
			String startTime_beginTime = querJson.getString("startTime_beginTime");
			if (StringUtils.isNotEmpty(startTime_beginTime))
			{
				queryMap.put("startTime_beginTime", startTime_beginTime);
			}
			// 任务开始结束时间
			String startTime_endTime = querJson.getString("startTime_endTime");
			if (StringUtils.isNotEmpty(startTime_endTime))
			{
				queryMap.put("startTime_endTime", startTime_endTime);
			}

			// 任务结束起始时间
			String endTime_beginTime = querJson.getString("endTime_beginTime");
			if (StringUtils.isNotEmpty(endTime_beginTime))
			{
				queryMap.put("endTime_beginTime", endTime_beginTime);
			}

			// 任务结束 结束时间
			String endTime_endTime = querJson.getString("endTime_endTime");
			if (StringUtils.isNotEmpty(endTime_endTime))
			{
				queryMap.put("endTime_endTime", endTime_endTime);
			}

		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			// 任务名称
			if (sort.equalsIgnoreCase("name"))
			{
				sort = "dis.NAME";
			}
			// 任务类型
			else if (sort.equalsIgnoreCase("type"))
			{
				sort = "tar.RES_TYPE";
			}
			// 任务目标
			else if (sort.equalsIgnoreCase("ip"))
			{
				sort = "tar.IP";
			}
			// 任务开始时间 任务结束时间 任务进度 任务结果 失败信息 扫描文件大小 扫描文件数量 已处理
			else if (sort.equalsIgnoreCase("startTime") || sort.equalsIgnoreCase("endTime")
					|| sort.equalsIgnoreCase("status") || sort.equalsIgnoreCase("result")
					|| sort.equalsIgnoreCase("failReason") || sort.equalsIgnoreCase("size")
					|| sort.equalsIgnoreCase("totalCount") || sort.equalsIgnoreCase("successCount"))
			{
				sort = "t." + sort;
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<TaskCenterRsp> pageList = taskCenterSrv.queryTaskCentByPage(queryMap);

		// 根据策略ID获取与该策略相关的历史任务信息
		for (TaskCenterRsp taskCenterRsp : pageList.getList())
		{
			String jobId = taskCenterRsp.getId();
			queryMap.put("id", jobId);
			List<TaskCenterRsp> childTask = taskCenterSrv.getTaskInfoByDisId(queryMap);
			taskCenterRsp.setChildTask(childTask);

		}

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 分页查询任务处理成功文件
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询任务处理成功文件", notes = "分页查询任务处理成功文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "每页显示条数", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "排序字段（默认字段scanTime）", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "order", value = "升序或降序", required = true, dataType = "String", paramType = "query") })
	@RequestMapping(value = "/pageSuccessFile", method = RequestMethod.GET)
	public @ResponseBody ResponseBean pageSuccessFile(HttpServletRequest req)
			throws Exception
	{

		Map<String, Object> queryMap = new HashMap<>();

		String taskId = req.getParameter("taskId");
		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);
		queryMap.put("taskId", taskId);

		PageInfo<SpTaskFiles> pageList = taskCenterSrv.querySuccessFileByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 分页查询任务处理失败文件
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询任务处理失败文件", notes = "分页查询任务处理成功文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "每页显示条数", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "排序字段（默认字段scanTime）", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "order", value = "升序或降序", required = true, dataType = "String", paramType = "query") })
	@RequestMapping(value = "/pageFailFile", method = RequestMethod.GET)
	public @ResponseBody ResponseBean pageFailFile(HttpServletRequest req) throws Exception
	{

		Map<String, Object> queryMap = new HashMap<>();

		String taskId = req.getParameter("taskId");
		String currentPageValue = req.getParameter("page");
		String pageSizeValue = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);
		queryMap.put("taskId", taskId);

		PageInfo<SpTaskSipped> pageList = taskCenterSrv.queryTaskFailFileByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 根据策略ID获取更多任务信息
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTaskInfoByDisId", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getTaskInfoByDisId(HttpServletRequest req,
			@RequestBody Map<String, Object> quereMap) throws Exception
	{

		if (quereMap.get("id") == null)
		{
			return ResultUtil.getFailResult(new CodeRsp(RspCode.PARAMERTER_ERROR));
		}
		List<TaskCenterRsp> taskCenterRspList = taskCenterSrv.getTaskInfoByDisId(quereMap);
		return ResultUtil.getSuccessResult(taskCenterRspList);
	}

	/**
	 * 删除任务信息
	 * @param req
	 * @param parms
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/deleteTaskByIds", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteTaskByIds(HttpServletRequest req, @RequestBody Map<String, Object> parms)
			throws TMCException
	{
		List<String> ids = (List<String>) parms.get("ids");
		if (null != ids)
		{
			taskCenterSrv.deleteTaskById(ids);
			return ResultUtil.getSuccessResult();
		}
		else
		{
			log.error("delete by ids,ids is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
	}

	/**
	 * 启动任务
	 * @param req
	 * @param jobId  要启动的任务ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/start/{jobId}/{taskId}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean startJob(HttpServletRequest req, @PathVariable String jobId,
			@PathVariable String taskId) throws Exception
	{
		if (StringUtils.isEmpty(jobId) || StringUtils.isNotEmpty(taskId))
		{
			OperateMsg result = taskCenterSrv.startJob(jobId);
			CodeRsp res = new CodeRsp(RspCode.SUCCESS);
			if (null != result)
			{
				List<JobMsg> jobMsgs = result.getContent();
				if (null != jobMsgs && jobMsgs.size() > 0)
				{
					JobMsg msg = jobMsgs.get(0);
					if (null != msg)
					{
						log.info("启动任务返回码：" + msg.getCode());
						res.setCode(msg.getCode());
						res.setMsg(RspCode.getRspCodeByCode(msg.getCode()).getDescription());
					}
					// 启动成功修改任务状态
					if (res.getCode().equals(RspCode.JOB_SUCCESS.getCode()))
					{
						res = new CodeRsp(RspCode.SUCCESS);
						// 任务状态修改为启动中...
						taskCenterSrv.updateTaskOperationStatus(jobId, taskId, OperationStatus.STARTING.name());
					}
				}
			}
			return ResultUtil.getDefinedCodeResult(res);
		}
		else
		{
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PARAMERTER_ERROR));
		}
	}

	/**
	 * 暂停任务
	 * @param req
	 * @param jobId 要暂停的任务ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pause/{jobId}/{taskId}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean pauseJob(HttpServletRequest req, @PathVariable String jobId,
			@PathVariable String taskId) throws Exception
	{
		if (StringUtils.isEmpty(jobId) || StringUtils.isNotEmpty(taskId))
		{
			OperateMsg result = taskCenterSrv.pauseJob(jobId);
			CodeRsp res = new CodeRsp(RspCode.SUCCESS);
			if (null != result)
			{
				List<JobMsg> jobMsgs = result.getContent();
				if (null != jobMsgs && jobMsgs.size() > 0)
				{
					JobMsg msg = jobMsgs.get(0);
					if (null != msg)
					{
						log.info("暂停任务返回码：" + msg.getCode());
						res.setCode(msg.getCode());
						res.setMsg(RspCode.getRspCodeByCode(msg.getCode()).getDescription());
					}
					// 启动成功修改任务状态
					if (res.getCode().equals(RspCode.JOB_SUCCESS.getCode()))
					{
						res = new CodeRsp(RspCode.SUCCESS);
						// 修改任务状态...
						taskCenterSrv.updateTaskOperationStatus(jobId, taskId, OperationStatus.PAUSED.name());
					}
				}
				return ResultUtil.getDefinedCodeResult(res);
			}
			else
			{
				throw new TMCException(RspCode.JOB_FAILURE);
			}
		}
		else
		{
			return ResultUtil.getDefinedCodeResult(new CodeRsp(RspCode.PARAMERTER_ERROR));
		}
	}
}
