package com.spinfosec.controller.system;

import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.LotusDatabaseDto;
import com.spinfosec.service.srv.ILotusConfigSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.utils.OperateLogUtil;
import com.spinfosec.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName LotusConfigController
 * @Description: 〈Lotusp配置〉
 * @date 2018/11/16
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping(value = "/lotus/config")
public class LotusConfigController
{
	private static final Logger log = LoggerFactory.getLogger(LotusConfigController.class);

	@Autowired
	private ILotusConfigSrv lotusConfigSrv;

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 获取Lotus邮箱类型配置
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getMail", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getLotusMailConfig(HttpServletRequest request)
	{
		LotusDatabaseDto mailConfig = lotusConfigSrv.getLotusMailConfig();
		return ResultUtil.getSuccessResult(mailConfig);
	}

	/**
	 * 更新Lotus邮箱类型配置
	 * @param request
	 * @param lotusDatabaseDto
	 * @return
	 */
	@RequestMapping(value = "/saveMail", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateLotusMailConfig(HttpServletRequest request,
			@RequestBody LotusDatabaseDto lotusDatabaseDto)
	{
		CodeRsp codeRsp = lotusConfigSrv.updateLotusMailConfig(lotusDatabaseDto);
		if (codeRsp.getCode().equals("000"))
		{
			// 操作日志
			SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
			StringBuilder operateDes = new StringBuilder();
			operateDes.append("Lotus配置：邮件类型，" + "路径：" + lotusDatabaseDto.getPath() + "，视图：" + lotusDatabaseDto.getView()
					+ "，字段：" + lotusDatabaseDto.getField());
			operateLogInfo.setOperation(operateDes.toString());
			systemSrv.saveOperateLog(operateLogInfo);
		}

		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

	/**
	 * 获取Lotus文档类型配置
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getDocument", method = RequestMethod.GET)
	public @ResponseBody ResponseBean getDocumentConfig(HttpServletRequest request)
	{
		List<LotusDatabaseDto> documentConfig = lotusConfigSrv.getLotusDocumentConfig();
		return ResultUtil.getSuccessResult(documentConfig);
	}

	/**
	 * 更新Lotus文档类型配置
	 * @param request
	 * @param lotusDatabaseDtos
	 * @return
	 */
	@RequestMapping(value = "/saveDocument", method = RequestMethod.POST)
	public @ResponseBody ResponseBean updateLotusDocumentConfig(HttpServletRequest request,
			@RequestBody List<LotusDatabaseDto> lotusDatabaseDtos)
	{
		// 操作日志
		SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
		CodeRsp codeRsp = lotusConfigSrv.updateLotusDocumentConfig(lotusDatabaseDtos, operateLogInfo);

		return ResultUtil.getDefinedCodeResult(codeRsp);
	}

}
