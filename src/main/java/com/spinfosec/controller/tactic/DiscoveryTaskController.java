package com.spinfosec.controller.tactic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpEncryptionAlgorithm;
import com.spinfosec.dao.entity.SpPlcFileTypes;
import com.spinfosec.dao.entity.SpTargetRes;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.tatic.DiscoveryTaskFormData;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.service.srv.ITacticSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DiscoveryTaskController
 * @Description: 〈扫描策略控制层〉
 * @date 2018/10/22
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/tactic/discoverytask")
public class DiscoveryTaskController
{
	private Logger log = LoggerFactory.getLogger(DiscoveryTaskController.class);

	@Autowired
	private ITacticSrv tacticSrv;

	/**
	 * 分页查询扫描策略
	 * @param req
	 * @return
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

		// 三权管理员用户只能查询自己创建的扫描策略
		// String mngUserId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		// queryMap.put("createdBy", mngUserId);

		if (null == currentPageValue || null == pageSizeValue)
		{
			log.error("currentPage or pageSize is null.");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}

		// 查询条件
		JSONObject querJson = (JSONObject) JSON.parse(req.getParameter("p"));

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		return ResultUtil.getSuccessResult();
	}

	/**
	 * 保存扫描策略
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveDiscoverytask", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveDiscoverytask(HttpServletRequest req, @RequestBody DiscoveryTaskFormData data)
			throws Exception
	{
		String userId = (String) req.getSession().getAttribute(SessionItem.userId.name());
		data.getTargetResFormData().getTargetRes().setCreatedBy(userId);
		data.getDiscoveryTasks().setCreatedBy(userId);
		TargetResFormData formData = data.getTargetResFormData();
		SpTargetRes targetRes = tacticSrv.saveTargetRes(formData);
		formData.setTargetRes(targetRes);
		tacticSrv.saveDiscoveryTask(data);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 更新扫描策略
	 *
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/updateDiscoverytask", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateDiscoveryTasks(HttpServletRequest req,
			@RequestBody DiscoveryTaskFormData data)
			throws Exception
	{
		String userId = (String) req.getSession().getAttribute(SessionItem.userId.name());
		data.getTargetResFormData().getTargetRes().setCreatedBy(userId);
		data.getDiscoveryTasks().setCreatedBy(userId);
		tacticSrv.updateSpPlcDiscoveryTask(data);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 根据ID查询扫描策略信息
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDiscoveryTaskById/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getDiscoveryTaskById(HttpServletRequest req, @PathVariable String id)
			throws Exception
	{
		DiscoveryTaskFormData data = tacticSrv.getDiscoveryTaskById(id);
		return ResultUtil.getSuccessResult(data);
	}

	/**
	 * 根据ID删除扫描策略信息
	 * @param req
	 * @param parms 删除参数
	 * @return
	 */
	@RequestMapping(value = "/deleteDiscoveryTasks", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteDiscoveryTaskById(HttpServletRequest req,
			@RequestBody Map<String, Object> parms)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		List<String> idList = (List<String>) parms.get("ids");
		if (!idList.isEmpty())
		{
			codeRsp = tacticSrv.deleteJobByMq(idList);
			if (null != codeRsp && RspCode.JOB_SUCCESS.getCode().equals(codeRsp.getCode()))
			{
				// 删除策略主表
				tacticSrv.deleteDiscoveryTaskById(idList);
			}
			else
			{
				return ResultUtil.getDefinedCodeResult(codeRsp);
			}
		}
		else
		{
			log.error("根据id删除扫描策略,id为空!");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 获取文件类型
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFileTypesByPage", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getFileTypesByPage(HttpServletRequest req) throws Exception
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
			// 是否常用
			String commonUse = querJson.getString("commonUse");
			if (StringUtils.isNotEmpty(commonUse))
			{
				queryMap.put("commonUse", commonUse);
			}

			// 文件类型
			String extension = querJson.getString("extension");
			if (StringUtils.isNotEmpty(extension))
			{
				queryMap.put("extension", extension);
			}

			// 文件类别
			String formatGroup = querJson.getString("formatGroup");
			if (StringUtils.isNotEmpty(formatGroup))
			{
				// 中文处理
				formatGroup = URLDecoder.decode(formatGroup, "utf-8");
				queryMap.put("formatGroup", formatGroup);
			}
		}

		// 排序处理
		if (null != sort && !"".equals(sort))
		{
			if (sort.equalsIgnoreCase("extension"))
			{
				sort = "f.EXTENSION";
			}
			else if (sort.equalsIgnoreCase("formatGroup"))
			{
				sort = "fc.NAME_CN";
			}
		}

		queryMap.put("sort", sort);
		queryMap.put("order", order);
		queryMap.put("currentPage", currentPageValue);
		queryMap.put("pageSize", pageSizeValue);

		PageInfo<SpPlcFileTypes> pageList = tacticSrv.getFileTypesByPage(queryMap);

		JSONObject dataJson = new JSONObject();
		dataJson.put("total", pageList.getTotal());
		dataJson.put("rows", pageList.getList());
		return ResultUtil.getSuccessResult(dataJson);
	}

	/**
	 * 获取常用文件类型
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCommonFileType", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getCommonFileType(HttpServletRequest req) throws Exception
	{
		List<SpPlcFileTypes> commonFileType = tacticSrv.getCommonFileType();
		return ResultUtil.getSuccessResult(commonFileType);
	}

	/**
	 * 添加/移除常用文件类型
	 * @param req
	 * @param id
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/moveCommonFileType/{id}/{state}", method = RequestMethod.GET)
	public @ResponseBody ResponseBean moveCommonFileType(HttpServletRequest req, @PathVariable String id,
			@PathVariable String state) throws Exception
	{
		CodeRsp code = new CodeRsp(RspCode.SUCCESS);
		if (StringUtils.isEmpty(id))
		{
			code = new CodeRsp(RspCode.OBJECE_NOT_EXIST);
			return ResultUtil.getDefinedCodeResult(code);
		}

		tacticSrv.updateFileTypeCommon(id, state);
		return ResultUtil.getSuccessResult();
	}

	/**
	 * 获取加密算法库
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getEncryptionAlgorithm", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getEncryptionAlgorithm(HttpServletRequest req) throws Exception
	{
		List<SpEncryptionAlgorithm> encryptionAlgorithm = tacticSrv.getEncryptionAlgorithm();
		return ResultUtil.getSuccessResult(encryptionAlgorithm);
	}

}
