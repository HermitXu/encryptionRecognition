package com.spinfosec.service.srv;

import com.spinfosec.dto.pojo.system.DscvrFilesRsp;
import com.spinfosec.dto.pojo.system.ExportBusiDataDto;
import com.spinfosec.dto.pojo.system.ReportWordData;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ICensorshipReportSrv
 * @Description: 〈审计报告管理业务类〉
 * @date 2018/10/31
 * All rights Reserved, Designed By SPINFO
 */
public interface ICensorshipReportSrv
{

	/**
	 * 根据查询条件获取事件列表
	 * @param busiDataDto
	 * @return
	 */
	List<DscvrFilesRsp> geteDscvrFilesRspList(ExportBusiDataDto busiDataDto);
	/**
	 * 导出事件PDF
	 * @param busiDataDto
	 * @return
	 */
	String getPdfForEvent(ExportBusiDataDto busiDataDto, String path);

	/**
	 * 根据任务ID查询导出word所需数据
	 * @param id
	 * @return
	 */
	ReportWordData getReportWordByTaskId(String id);
}
