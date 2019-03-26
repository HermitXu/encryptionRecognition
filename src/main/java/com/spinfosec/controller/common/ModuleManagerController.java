package com.spinfosec.controller.common;

import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.ModuleBean;
import com.spinfosec.service.srv.IModuleSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ModuleManagerController
 * @Description: 〈模块管理〉
 * @date 2019/3/20
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Api(value = "/module", tags = "模块管理")
@RestController
@RequestMapping("/module")
public class ModuleManagerController
{
	private static final Logger log = LoggerFactory.getLogger(ModuleManagerController.class);

	@Autowired
	private IModuleSrv moduleSrv;

	@ApiOperation(value = "/获取所有模块", notes = "获取所有模块")
	@RequestMapping(value = "/getAllModule", method = RequestMethod.GET)
	public @ResponseBody ResponseBean<TreeData> getAllModule(HttpServletRequest request, HttpServletResponse response)
	{
		return ResultUtil.getSuccessResult(moduleSrv.getAllModule());
	}

	@ApiOperation(value = "/保存模块", notes = "保存模块")
	@RequestMapping(value = "/saveModule", method = RequestMethod.POST)
	public @ResponseBody ResponseBean saveModule(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody ModuleBean moduleBean)
	{
		moduleSrv.saveModuleBean(moduleBean);
		return ResultUtil.getSuccessResult();
	}

	@ApiOperation(value = "/更新模块", notes = "更新模块")
	@RequestMapping(value = "/updateModule", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateModule(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody ModuleBean moduleBean)
	{
		moduleSrv.updateModuleBean(moduleBean);
		return ResultUtil.getSuccessResult();
	}

	@ApiOperation(value = "/删除模块", notes = "删除模块")
	@ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/deleteModule/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deleteModule(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable String id)
	{
		moduleSrv.deleteCodeDecodes(id);
		return ResultUtil.getSuccessResult();
	}

	@ApiOperation(value = "/上下移动模块", notes = "上下移动模块")
	@RequestMapping(value = "/upOrDownModule", method = RequestMethod.POST)
	public @ResponseBody ResponseBean upOrDownModule(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody List<SpCodeDecodes> list)
	{
		for (SpCodeDecodes code : list)
		{
			if (StringUtils.isEmpty(code.getId()) && null == Integer.valueOf(code.getOrders()))
			{
				log.info("移动模块发生错误!");
				new TMCException(RspCode.PARAMERTER_ERROR);
			}
		}

		int order1 = list.get(0).getOrders();
		int order2 = list.get(1).getOrders();

		SpCodeDecodes code1 = list.get(0);
		SpCodeDecodes code2 = list.get(1);

		code1.setOrders(order2);
		code2.setOrders(order1);

		moduleSrv.updateCodeDecodes(code1);
		moduleSrv.updateCodeDecodes(code2);

		return ResultUtil.getSuccessResult();
	}

}
