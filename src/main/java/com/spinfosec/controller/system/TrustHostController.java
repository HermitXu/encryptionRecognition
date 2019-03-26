package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpAdminHostSetting;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.TrustHostBean;
import com.spinfosec.service.srv.ITrustHostSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.ResultUtil;
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
 * @ClassName SystemSecHostController
 * @Description: 〈信任主机管理控制层〉
 * @date 2018/10/15
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/trustHost")
public class TrustHostController
{
	private static final Logger log = LoggerFactory.getLogger(TrustHostController.class);

	@Autowired
	private ITrustHostSrv trustHostSrv;

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean querySpSecHostByPage(HttpServletRequest req) throws Exception
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

		// 三权管理员用户只能查询自己创建的信任主机
		String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		queryMap.put("createdBy", mngUserId);

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 主机名称
			String name = querJson.getString("name");
			if (StringUtils.isNotEmpty(name))
			{
				queryMap.put("name", name);
			}

			// 信任IP
			String hostIp = querJson.getString("hostIp");
			if (StringUtils.isNotEmpty(hostIp))
			{
				queryMap.put("hostIp", hostIp);
			}

			// 创建时间
			String createTime = querJson.getString("createTime");
			if (StringUtils.isNotEmpty(createTime))
			{
				queryMap.put("createTime", createTime);
			}

			// 创建用户
			String userCreateName = querJson.getString("userCreateName");
			if (StringUtils.isNotEmpty(userCreateName))
			{
				queryMap.put("userCreateName", userCreateName);
			}

			// 创建角色
			String roleCreateName = querJson.getString("roleCreateName");
			if (StringUtils.isNotEmpty(roleCreateName))
			{
				queryMap.put("roleCreateName", roleCreateName);
			}

			// 描述
			String description = querJson.getString("description");
			if (StringUtils.isNotEmpty(description))
			{
				queryMap.put("description", description);
			}
		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("name"))
			{
				sort = "h.NAME";
			}
			else if (sort.equalsIgnoreCase("hostIp"))
			{
				sort = "h.HOST_IP";
			}
			else if (sort.equalsIgnoreCase("createTime"))
			{
				sort = "h.CREATE_DATE";
			}
			else if (sort.equalsIgnoreCase("userCreateName"))
			{
				sort = "a.USERNAME";
			}

			else if (sort.equalsIgnoreCase("roleCreateName"))
			{
				sort = "r.NAME";
			}

		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<TrustHostBean> pageList = trustHostSrv.querByPage(queryMap);
		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 获取所有信任主机信息
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getAllSecHost(HttpServletRequest req) throws Exception
	{
		String userId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		return ResultUtil.getSuccessResult(trustHostSrv.getAllSecHost(userId));
	}

	@RequestMapping(value = "/getSecHostById/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getSecHostById(HttpServletRequest req, @PathVariable String id) throws Exception
	{
		return ResultUtil.getSuccessResult(trustHostSrv.getHostSettingById(id));
	}

	@RequestMapping(value = "/addSecHost", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveHostSec(HttpServletRequest req, @RequestBody SpAdminHostSetting host)
	{
		String userId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		host.setCreatedBy(userId);
		trustHostSrv.saveHostSec(host);
		return ResultUtil.getSuccessResult();
	}

	@RequestMapping(value = "updateSecHost", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateSecHost(HttpServletRequest request, @RequestBody SpAdminHostSetting host)
	{
		trustHostSrv.updateHostSec(host);
		return ResultUtil.getSuccessResult();
	}

	@RequestMapping(value = "/deleteSecHost/{ids}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deletePort(HttpServletRequest req, @PathVariable String ids)
	{
		trustHostSrv.deleteHostSec(ids);
		return ResultUtil.getSuccessResult();
	}

}
