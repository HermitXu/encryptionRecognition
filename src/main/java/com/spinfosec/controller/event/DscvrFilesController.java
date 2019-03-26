package com.spinfosec.controller.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.DscvrFilesRsp;
import com.spinfosec.service.srv.IEventSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DscvrFilesController
 * @Description: 〈检查事件控制层〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/event/dscvrFiles")
public class DscvrFilesController
{

	private static final Logger log = LoggerFactory.getLogger(DscvrFilesController.class);

	@Autowired
	private IEventSrv eventSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 分页查询检查事件信息
	 * @param req
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getDscvrFilesByPage(HttpServletRequest req) throws Exception
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
			// 任务名称
			String jobName = querJson.getString("jobName");
			if (StringUtils.isNotEmpty(jobName))
			{
				queryMap.put("jobName", jobName);
			}

			// JobId
            String jobId = querJson.getString("jobId");
            if (StringUtils.isNotEmpty(jobId))
            {
                queryMap.put("jobId", jobId);
            }


            // 任务类型
			String taskType = querJson.getString("taskType");
			if (StringUtils.isNotEmpty(taskType))
			{
				queryMap.put("taskType", taskType);
			}

			// ip
			String ip = querJson.getString("ip");
			if (StringUtils.isNotEmpty(ip))
			{
				queryMap.put("ip", ip);
			}

			// 文件名称
			String fileName = querJson.getString("fileName");
			if (StringUtils.isNotEmpty(fileName))
			{
				queryMap.put("fileName", fileName);
			}

			// 文件类型
			String fileExtension = querJson.getString("fileExtension");
			if (StringUtils.isNotEmpty(fileExtension))
			{
				queryMap.put("fileExtension", fileExtension);
			}

			// 文件路径
			String filePath = querJson.getString("filePath");
			if (StringUtils.isNotEmpty(filePath))
			{
				queryMap.put("filePath", filePath);
			}

			// 是否加密
			String isEncrypt = querJson.getString("isEncrypt");
			if (StringUtils.isNotEmpty(isEncrypt))
			{
				queryMap.put("isEncrypt", isEncrypt);
			}

			// 加密算法
			String algorithmType = querJson.getString("algorithmType");
			if (StringUtils.isNotEmpty(algorithmType))
			{
				queryMap.put("algorithmType", algorithmType);
			}

			// 检查开始时间
			String detectDateTs_beginTime = querJson.getString("detectDateTs_beginTime");
			if (StringUtils.isNotEmpty(detectDateTs_beginTime))
			{
				queryMap.put("detectDateTs_beginTime", detectDateTs_beginTime);
			}

			// 检查结束时间
			String detectDateTs_endTime = querJson.getString("detectDateTs_endTime");
			if (StringUtils.isNotEmpty(detectDateTs_endTime))
			{
				queryMap.put("detectDateTs_endTime", detectDateTs_endTime);
			}
		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("jobName"))
			{
				sort = "f.JOB_NAME";
			}
			else if (sort.equalsIgnoreCase("fileName"))
			{
				sort = "f.FILE_NAME";
			}
			else if (sort.equalsIgnoreCase("fileExtension"))
			{
				sort = "f.FILE_EXTENSION";
			}
			else if (sort.equalsIgnoreCase("filePath"))
			{
				sort = "f.FILE_PATH";
			}
			else if (sort.equalsIgnoreCase("isEncrypt"))
			{
				sort = "f.IS_ENCRYPT";
			}
			else if (sort.equalsIgnoreCase("algorithmType"))
			{
				sort = "f.ALGORITHM_TYPE";
			}
			else if (sort.equalsIgnoreCase("taskType"))
			{
				sort = "f.TASK_TYPE";
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<DscvrFilesRsp> pageList = eventSrv.queryByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}
}
