package com.spinfosec.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dao.entity.SpServerStatus;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.IConfigPropertiesSrv;
import com.spinfosec.service.srv.IEventSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.service.srv.ITaskCenterSrv;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName OverviewController
 * @Description: 〈概览控制层〉
 * @date 2018/11/19
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/overview")
public class OverviewController
{
	private Logger log = LoggerFactory.getLogger(OverviewController.class);

	@Autowired
	private ITaskCenterSrv taskCenterSrv;

	@Autowired
	private IEventSrv eventSrv;

	@Autowired
	private ISystemSrv systemSrv;

	@Autowired
	private IConfigPropertiesSrv configPropertiesSrv;

	/**
	 * 概览页-获取任务统计
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getTaskOverview", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getTaskOverview(HttpServletRequest req)
	{

		// 非系统预置用户只能查询各自创建的信息
		String mngUserId = "";
		String sessionUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		SpAdmins admins = systemSrv.getAdminById(sessionUserId);
		if (admins.getDefinitionType().equalsIgnoreCase(Contants.C_USER_DEFINE))
		{
			mngUserId = sessionUserId;
		}
		// 获取总任务数
		JSONObject result = new JSONObject();
		int allTaskNum = taskCenterSrv.getTaskNum(null, mngUserId);
		result.put("allTaskNum", allTaskNum);
		result.put("allTaskWeek", allTaskNum - taskCenterSrv.getTaskNumTimeAgo(null, mngUserId));

		// 获取未运行任务数
		int unstart = taskCenterSrv.getTaskNum("unstart", mngUserId);
		result.put("unStartTaskNum", unstart);
		result.put("unStartTaskWeek", unstart - taskCenterSrv.getTaskNumTimeAgo("unstart", mngUserId));

		// 获取已运行任务数
		int alreadyRun = taskCenterSrv.getTaskNum("alreadyRun", mngUserId);
		result.put("alreadyRunTaskNum", alreadyRun);
		result.put("alreadyRunTaskWeek", alreadyRun - taskCenterSrv.getTaskNumTimeAgo("alreadyRun", mngUserId));

		return ResultUtil.getSuccessResult(result);
	}

	/**
	 * 概览页-加密算法类型占比
	 * @param req
	 * @param period 周期 DAY日 WEEK周 MONTH月 YEAR年
	 * @return
	 */
	@RequestMapping(value = "/getAlgorithmOverview/{period}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getAlgorithmOverview(HttpServletRequest req, @PathVariable String period)
	{


		String mngUserId = "";
		// 非系统预置用户只能查询各自创建的信息
		String sessionUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		SpAdmins admins = systemSrv.getAdminById(sessionUserId);
		if (admins.getDefinitionType().equalsIgnoreCase(Contants.C_USER_DEFINE))
		{
			mngUserId = sessionUserId;
		}
		List<JSONObject> result = new ArrayList<>();
		List<Object[]> algorithmScale = eventSrv.getAlgorithmScale(period, mngUserId);

		if (algorithmScale != null && algorithmScale.size() != 0)
		{
			for (Object[] algorithmAndNum : algorithmScale)
			{
				JSONObject obj = new JSONObject();
				obj.put("name", algorithmAndNum[0]);
				obj.put("value", algorithmAndNum[1]);
				result.add(obj);
			}
		}
		return ResultUtil.getSuccessResult(result);
	}

	/**
	 * 概览页-加密未加密文件数
	 * @param req
	 * @param period 周期 DAY日 WEEK周 MONTH月 YEAR年
	 * @return
	 */
	@RequestMapping(value = "/getIsEncryptOverview/{period}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getIsEncryptOverview(HttpServletRequest req, @PathVariable String period)
	{
		// 非系统预置用户只能查询各自创建的信息
		String mngUserId = "";
		String sessionUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		SpAdmins admins = systemSrv.getAdminById(sessionUserId);
		if (admins.getDefinitionType().equalsIgnoreCase(Contants.C_USER_DEFINE))
		{
			mngUserId = sessionUserId;
		}
		List<JSONObject> result = new ArrayList<>();
		List<Object[]> isEncrypScale = eventSrv.getIsEncrypScale(period, mngUserId);

		if (isEncrypScale != null && isEncrypScale.size() != 0)
		{
			for (Object[] algorithmAndNum : isEncrypScale)
			{
				JSONObject obj = new JSONObject();
				obj.put("name", algorithmAndNum[0]);
				obj.put("value", algorithmAndNum[1]);
				result.add(obj);
			}
		}
		return ResultUtil.getSuccessResult(result);
	}

	/**
	 * 概览页-CPU、内存、存储空间
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getServerStatusOverview", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getServerStatusOverview(HttpServletRequest req)
	{
		JSONObject obj = new JSONObject();
		SpServerStatus serverStatus = configPropertiesSrv.getServerStatus();
		if (serverStatus == null)
		{
			obj.put("cpu", 0);
			obj.put("disk", 0);
			obj.put("ram", 0);
		}
		else
		{
			DecimalFormat df = new DecimalFormat("#.0");
			obj.put("cpu", serverStatus.getCpuUsage());
			Double totalDis = Double.parseDouble(serverStatus.getTotalDisk());
			double percent = ((totalDis - serverStatus.getFreeDisk()) / totalDis) * 100;
			obj.put("disk", Double.valueOf(df.format(percent)));
			obj.put("ram", serverStatus.getMemUsage());
		}
		return ResultUtil.getSuccessResult(obj);
	}
}
