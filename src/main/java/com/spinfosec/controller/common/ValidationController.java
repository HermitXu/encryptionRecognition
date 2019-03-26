package com.spinfosec.controller.common;

import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.common.ValidationBean;
import com.spinfosec.service.srv.IValidationSrv;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ValidationController
 * @Description: 〈后台检验控制层〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/validation")
public class ValidationController
{
	@Autowired
	private IValidationSrv validationSrv;

	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	public @ResponseBody ResponseBean checkDuplicateData(HttpServletRequest req, @RequestBody ValidationBean data)
	{
		boolean result = validationSrv.checkDuplicateData(data);
		if (!result)
		{
			return ResultUtil.getSuccessResult(false);
		}
		else
		{
			return ResultUtil.getSuccessResult(true);
		}
	}

	/**
	 * 校验是否被引用
	 * @param req
	 * @return
	 * @throws TMCException
	 */
	@RequestMapping(value = "/getNotInUseIds", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getNotInUseIds(HttpServletRequest req, @RequestBody Map<String, Object> parms)
			throws TMCException
	{
		// 校验ID
		List<String> idList = (List<String>) parms.get("ids");
		// 校验类型
		String type = (String) parms.get("type");
		return ResultUtil.getSuccessResult(validationSrv.getNotInUseIds(idList, type));
	}
}
