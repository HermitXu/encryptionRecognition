package com.spinfosec.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpRoleModulePermissions;
import com.spinfosec.dao.entity.SpRoles;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.RoleReq;
import com.spinfosec.service.srv.ISystemSrv;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName RoleController
 * @Description: 〈角色管理控制层〉
 * @date 2018/10/15
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/roles")
public class RoleController
{
	private static final Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private ISystemSrv systemSrv;

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public @ResponseBody ResponseBean querySpRolesByPage(HttpServletRequest req) throws IOException, TMCException
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

		// 三权管理员用户只能查询自己创建的角色
		String mngRoleId = req.getSession().getAttribute(SessionItem.roleId.toString()).toString();
		queryMap.put("createdBy", mngRoleId);

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("q"));
		if (querJson != null)
		{
			// 名称
			String name = querJson.getString("name");
			if (StringUtils.isNotEmpty(name))
			{
				queryMap.put("name", name);
			}
			// 描述
			String description = querJson.getString("description");
			if (StringUtils.isNotEmpty(description))
			{
				queryMap.put("description", description);
			}
			// 创建时间
			String createDate = querJson.getString("createDate");
			if (StringUtils.isNotEmpty(createDate))
			{
				queryMap.put("createDate", createDate);
			}

			String definitionType = querJson.getString("definitionType");
			if (StringUtils.isNotEmpty(definitionType))
			{
				queryMap.put("definitionType", definitionType);
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<SpRoles> pageList = systemSrv.querySpRolesByPage(queryMap);
		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());

		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 获取所有角色信息
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws TMCException
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getAllRoles(HttpServletRequest req) throws IOException, TMCException
	{
		String roleId = req.getSession().getAttribute(SessionItem.roleId.name()).toString();
		return ResultUtil.getSuccessResult(systemSrv.getAllSpRole(roleId));
	}

	/**
	 * 保存角色
	 * @param req
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveRole(HttpServletRequest req, @RequestBody RoleReq data)
	{
		String roleId = req.getSession().getAttribute(SessionItem.roleId.name()).toString();
		systemSrv.saveSpRoles(data, roleId);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 更新角色
	 * @param req
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateRole(HttpServletRequest req, @RequestBody RoleReq data)
	{
		systemSrv.updateSpRoles(data);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 根据角色ID获取所有权限菜单
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getMenuByRoleId", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getParentMenuByRoleId(HttpServletRequest req)
	{
		String roleId = req.getParameter("roleId");
		return ResultUtil.getSuccessResult(systemSrv.getMenuByRoleId(roleId));
	}

	/**
	 * 根据角色ID获取该角色的拥有模块和角色信息
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getRoleDataById/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getRoleDataById(HttpServletRequest req, @PathVariable String id)
	{
		return ResultUtil.getSuccessResult(systemSrv.getRoleDataById(id));
	}

	/**
	 * 根据ID删除角色信息
	 * @param req
	 * @param parms 放在body中的参数集合
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteByIds(HttpServletRequest req, @RequestBody Map<String, Object> parms)
			throws TMCException
	{
		List<String> idList = (List<String>) parms.get("ids");
		if (null != idList)
		{
			if (systemSrv.deleteSpRoles(idList))
			{
				return ResultUtil.getSuccessResult();
			}
			else
			{
				return ResultUtil.getFailResult(new CodeRsp(RspCode.DEFAULT_CAN_NOT_DELETE));
			}
		}
		else
		{
			log.error("delete by ids,ids is null.");
			throw new TMCException(RspCode.OBJECE_NOT_EXIST);
		}
	}

}
