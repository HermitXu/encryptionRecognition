package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
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
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SysOperateLogControtller
 * @Description: 〈系统操作日志控制层〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/operateLog")
public class SysOperateLogControtller
{

	private static final Logger log = LoggerFactory.getLogger(SysOperateLogControtller.class);

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 分页查询系统操作日志
	 * @param req
	 * @param rsp
	 * @return
	 */
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
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}


		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 用户名
			String adminName = querJson.getString("adminName");
			if (StringUtils.isNotEmpty(adminName))
			{
				queryMap.put("adminName", adminName);
			}

			// 角色名
			String roleName = querJson.getString("roleName");
			if (StringUtils.isNotEmpty(roleName))
			{
				queryMap.put("roleName", roleName);
			}

			// 操作内容
			String operation = querJson.getString("operation");
			if (StringUtils.isNotEmpty(operation))
			{
				queryMap.put("operation", operation);
			}

		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<SpSystemOperateLogInfo> pageList = systemSrv.queryOperateLogByPage(queryMap);
		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}
}
