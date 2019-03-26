package com.spinfosec.controller.system;

import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.service.srv.IDataClearSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.OperateLogUtil;
import com.spinfosec.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName DataClearController
 * @Description: 〈数据清理〉
 * @date 2019/1/15
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Api(value = "/system/dataClear", tags = "数据清理")
@RestController
@RequestMapping("/system/dataClear")
public class DataClearController
{
	private Logger log = LoggerFactory.getLogger(DataClearController.class);

	@Autowired
	private IDataClearSrv dataClearSrv;

	@ApiOperation(value = "数据清理", notes = "页面展示数据清理具体内容详见接口文档 十四、数据清理")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody ResponseBean clearData(HttpServletRequest request,
			@RequestBody @ApiParam(name = "要清理的数据类型", value = "DATA_BUSINESS 业务数据<br/>DATA_EVENT 事件数据<br/>DATA_OPERATE_LOG 操作日志数据<br/> "
					+ "DATA_SETTING 系统设置数据<br/>DATA_BACKUP_RECOVER 备份数据<br/>DATA_USER_ROLE 用户和角色<br/>DATA_OTHER 其他", required = true) String[] dataArr)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);

		if (null == dataArr || dataArr.length == 0)
		{
			codeRsp = new CodeRsp();
			codeRsp.setCode("001");
			codeRsp.setMsg("请选择需要清理的数据类型！");
			return ResultUtil.getDefinedCodeResult(codeRsp);
		}

		boolean isExistRunningTask = dataClearSrv.isExistRunningTask();
		if (isExistRunningTask)
		{
			codeRsp = new CodeRsp();
			codeRsp.setCode("002");
			codeRsp.setMsg("任务中心有任务正在运行，请停止后再试！");
			return ResultUtil.getDefinedCodeResult(codeRsp);
		}

		// 停止se
		dataClearSrv.stopCrond();
		boolean isStop = dataClearSrv.stopScanEngine();
		if (!isStop)
		{
			codeRsp = new CodeRsp();
			codeRsp.setCode("003");
			codeRsp.setMsg("SE服务停止失败，请检查服务运行是否正常！");
			return ResultUtil.getDefinedCodeResult(codeRsp);
		}

		// 开始进行数据清理
		try
		{
			log.info("开始进行数据清理...");
			SpSystemOperateLogInfo operateLogInfo = OperateLogUtil.packageSysLog(request);
			dataClearSrv.clearData(dataArr, operateLogInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			codeRsp = new CodeRsp();
			codeRsp.setCode("004");
			codeRsp.setMsg("数据清理失败！");
			return ResultUtil.getDefinedCodeResult(codeRsp);
		}
		finally
		{
			dataClearSrv.startScanEngine();
			dataClearSrv.startCrond();

		}

		return ResultUtil.getSuccessResult();
	}
}
