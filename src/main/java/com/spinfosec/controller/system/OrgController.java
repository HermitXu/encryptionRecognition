package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.OrgTreeBean;
import com.spinfosec.service.srv.IOrgService;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName OrgController
 * @Description: 〈组织结构控制层〉
 * @date 2018/10/16
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/org")
public class OrgController
{
	private static final Logger log = LoggerFactory.getLogger(OrgController.class);
	@Autowired
	private IOrgService orgSrv;

	/**
	 * 获取组织资源树
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getOrgTree", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getOrgTree(HttpServletRequest req)
	{
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		Map<String, Object> queryMap = new HashMap<>();
		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 名称
			String name = querJson.getString("name");
			queryMap.put("name", name);
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);

		return ResultUtil.getSuccessResult(orgSrv.getOrg(queryMap));
	}

	/**
	 * 保存组织单位
	 * @param req
	 * @param orgTreeBean
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/saveOrgUnitDict", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveOrganizeUnit(HttpServletRequest req, @RequestBody OrgTreeBean orgTreeBean)
	{
		if (orgTreeBean != null)
		{
			orgSrv.saveOrgUnit(orgTreeBean);
			return ResultUtil.getSuccessResult();
		}
		else
		{
			log.error("Paramerter is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
	}

	/**
	 * 根据ID获取组织信息
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getOrgById/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getOrgById(HttpServletRequest req, @PathVariable String id)
	{
		return ResultUtil.getSuccessResult(orgSrv.getOrgInfoById(id));
	}

	/**
	 * 更新组织单位
	 * @param req
	 * @param orgTreeBean
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/updateOrgUnitDict", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateOrgUnitDict(HttpServletRequest req, @RequestBody OrgTreeBean orgTreeBean)
	{
		if (orgTreeBean != null)
		{
			if (StringUtils.isNotEmpty(orgTreeBean.getId()))
			{
				orgSrv.updateOrgUnit(orgTreeBean);
				return ResultUtil.getSuccessResult();
			}
			else
			{
				return ResultUtil.getFailResult(new CodeRsp(RspCode.PARAMERTER_ERROR));
			}

		}
		else
		{
			log.error("Paramerter is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
	}

	/**
	 * 判断组织关联用户是否为空
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/isOrgEmpty/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean isOrgEmpty(HttpServletRequest req, @PathVariable String id)
	{
		boolean result = false;
		if (null != id)
		{
			List<Object[]> admins = orgSrv.getUserByOrgId(id);
			if (null != admins && admins.isEmpty())
			{
				result = true;
			}
		}
		return ResultUtil.getSuccessResult(result);
	}

	/**
	 * 删除组织
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delOrg/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean delOrg(HttpServletRequest req, @PathVariable String id)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		if (null != id && id.length() > 0)
		{
			codeRsp = orgSrv.delOrg(id);
		}
		else
		{
			log.error("delete by ids,ids is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

}
