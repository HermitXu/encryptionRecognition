package com.spinfosec.controller.event;

import com.aspose.words.*;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.system.DscvrFilesRsp;
import com.spinfosec.dto.pojo.system.ExportBusiDataDto;
import com.spinfosec.dto.pojo.system.ReportWordData;
import com.spinfosec.service.srv.ICensorshipReportSrv;
import com.spinfosec.service.srv.ITaskCenterSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.ResultUtil;
import com.spinfosec.utils.WordUtil;
import com.spinfosec.utils.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName CensorshipReportController
 * @Description: 〈审计报告控制层〉
 * @date 2018/10/31
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/censorShip/report")
public class CensorshipReportController
{
	private static final Logger log = LoggerFactory.getLogger(CensorshipReportController.class);

	@Autowired
	private ICensorshipReportSrv censorshipReportSrv;

	/**
	 * 常见导出检查事件PDF报告
	 * @param req
	 * @param busiDataDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createEventPDF", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getDataForPdf(HttpServletRequest req, @RequestBody ExportBusiDataDto busiDataDto)
			throws Exception
	{
		// 查询当前用户所产生的事件
		String userId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();

		ClassLoader classLoader = CensorshipReportController.class.getClassLoader();
		URL templates = classLoader.getResource("templates");
		String path = URLDecoder.decode(templates.getPath(), "utf-8").replaceFirst("/", "");

		busiDataDto.setCreatedBy(userId);
		String pdfForEvent = censorshipReportSrv.getPdfForEvent(busiDataDto, path);
		return ResultUtil.getSuccessResult(pdfForEvent);
	}

	/**
	 * PDF打印获取数据
	 * @param req
	 * @param busiDataDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createEventPDFPrint", method = RequestMethod.POST)
	public @ResponseBody ResponseBean getDataForPdfPrint(HttpServletRequest req,
			@RequestBody ExportBusiDataDto busiDataDto) throws Exception
	{
		// 查询当前用户所产生的事件
		String userId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
		busiDataDto.setCreatedBy(userId);
		return ResultUtil.getSuccessResult(censorshipReportSrv.geteDscvrFilesRspList(busiDataDto));
	}

	/**
	 * 根据检查事件PDF名称下载指定文件
	 * @param req
	 * @param rsp
	 * @param pdfName PDF名称
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/exportEventPdf/{pdfName}", method = RequestMethod.GET)
	public void downEventPdf(HttpServletRequest req, HttpServletResponse rsp, @PathVariable String pdfName)
			throws Exception
	{

		OutputStream toClient = null;
		InputStream fis = null;
		try
		{
			ClassLoader classLoader = CensorshipReportController.class.getClassLoader();
			URL templates = classLoader.getResource("templates");
			String path = URLDecoder.decode(templates.getPath(), "utf-8").replaceFirst("/", "");

			File pdfFile = new File(path + File.separator + pdfName);
			if (!pdfFile.exists())
			{
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
			String downLoadName = pdfFile.getName();
			// 清空response
			rsp.reset();
			// 设置response的Header
			rsp.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"",
					new String(downLoadName.getBytes("utf-8"), "ISO-8859-1")));
			rsp.addHeader("Content-Length", "" + pdfFile.length());
			rsp.setContentType("application/octet-strea; charset=utf-8");
			// 输出流定向到http response中
			toClient = new BufferedOutputStream(rsp.getOutputStream());
			fis = new BufferedInputStream(new FileInputStream(pdfFile));

			byte[] pdfByte = new byte[fis.available()];
			fis.read(pdfByte);

			toClient.write(pdfByte);
			toClient.flush();

			fis.close();
			toClient.close();

			// 下载完成后删除原文件
			if (pdfFile.exists())
			{
				FileUtils.deleteQuietly(pdfFile);
			}
		}
		catch (Exception e)
		{
			log.error("检查事件PDF报告下载失败...", e);
		}
	}

	/**
	 * 创建任务word报告
	 * @param req
	 * @param parms
	 * @return
	 */
	@RequestMapping(value = "/createTaskReport", method = RequestMethod.POST)
	public @ResponseBody ResponseBean createTaskReport(HttpServletRequest req, @RequestBody Map<String, Object> parms)
			throws Exception

	{
		// 要导出的该条策略所产生的数据
		String id = (String) parms.get("id");
		// 是否合格 0合格 1不合格
		String isQualified = (String) parms.get("isQualified");
		// 是否导出事件详情PDF 0 不导出 1导出
		String isPDF = (String) parms.get("isPDF");
		// 报告类型
		List<String> templates = (List<String>) parms.get("reportType");


		ClassLoader classLoader = CensorshipReportController.class.getClassLoader();
		URL url = classLoader.getResource("");
		String resources = URLDecoder.decode(url.getPath(), "utf-8").replaceFirst("/", "");
		String templatePath = "";

		// 任务报告生成文件夹路径
		String reportFolderPath = resources + File.separatorChar + "report" + File.separatorChar
				+ System.currentTimeMillis();
		File reportFolderFile = new File(reportFolderPath);
		if (!reportFolderFile.exists())
		{
			reportFolderFile.mkdirs();
		}
		else
		{
			FileUtils.deleteQuietly(reportFolderFile);
			reportFolderFile.mkdirs();
		}
		try
		{
			// 获取插入word中的数据
			ReportWordData reportWordData = censorshipReportSrv.getReportWordByTaskId(id);

		List<String> paths = new ArrayList<>();

			for (String template : templates)
			{
				// 被检单位
				if (template.equals("1"))
				{
					templatePath = resources + File.separatorChar + "templates" + File.separatorChar
							+ "beCheckedOrgTemplate.doc";
				}
				// 检查单位
				else if (template.equals("2"))
				{
					templatePath = resources + File.separatorChar + "templates" + File.separatorChar
							+ "checkedOrgTemplate.doc";
				}
				// 留档
				else if (template.equals("3"))
				{
					templatePath = resources + File.separatorChar + "templates" + File.separatorChar
							+ "archiveOrgTemplate.doc";
				}
				File templateFile = new File(templatePath);
				if (!templateFile.exists())
				{
					log.info("任务报告模板不存在!" + templatePath);
					throw new TMCException(RspCode.EXPORT_FILE_ERROR);
				}

				// 破解去除水印
				if (!WordUtil.getLicense())
				{
					WordUtil.getLicense();
				}
				Document doc = new Document(templateFile.getAbsolutePath());

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] filed1 = { "reportId", "checkTime", "beCheckOrg", "checkOrg", "checkTarget", "beCheckedNum",
						"ciphertextNum", "plaintextNum", "businessPassNum", "unBusinessPassNum", "isQualified" };
				// 报告ID
				String reportId = "JY-" + (int) ((Math.random() * 9 + 1) * 100000);
				// 检查时间
				String checkTime = format.format(reportWordData.getCheckDate());
				// 被检单位
				String beCheckOrg = reportWordData.getBeCheckOrgName();
				// 检查单位
				String checkOrg = reportWordData.getCheckOrgName();
				// 检查目标
				String checkTarget = reportWordData.getTargetIp();
				// 被检文件数量
				String beCheckedNum = reportWordData.getCheckNum();

				// 密文总数
				Long ciphertextNum = reportWordData.getCiphertextNum();
				// 明文总数
				Long plaintextNum = reportWordData.getPlaintextNum();
				// 商用密码密文数
				Long businessPassNum = reportWordData.getBusinessPassNum();
				// 非商用密码密文数
				Long unBusinessPassNum = reportWordData.getUnBusinessPassNum();

				Object[] val1 = { reportId, checkTime, beCheckOrg, checkOrg, checkTarget, beCheckedNum, ciphertextNum,
						plaintextNum, businessPassNum, unBusinessPassNum, isQualified.equals("0") ? "合格" : "不合格" };

				doc.getMailMerge().execute(filed1, val1);

				// File noTickFile = new File(
				// resources + File.separator + "templates" + File.separatorChar + "noTick.png");
				//
				// // 插入是否合格图片 0合格 1不合格
				// // builder.moveToBookmark("isQualified");
				// if (isQualified.equals("1"))
				// {
				// builder.insertImage(noTickFile.getAbsolutePath());
				// }
				// else
				// {
				//
				// }
				doc.updateFields();

				String fileName = "";
				if (template.equals("1"))
				{
					fileName = "-被检单位.doc";
				}
				else if (template.equals("2"))
				{
					fileName = "-检查单位.doc";
				}
				else if (template.equals("3"))
				{
					fileName = "-留档.doc";
				}
				String filePath = reportWordData.getName() + fileName;
				String reportFilePath = reportFolderPath + File.separatorChar + filePath;

				doc.save(reportFilePath);

				paths.add(filePath);

			}

			// 是否导出PDF 0 不导出 1导出
			if (isPDF.equals("1"))
			{
				String userId = req.getSession().getAttribute(SessionItem.userId.toString()).toString();
				ExportBusiDataDto busiDataDto = new ExportBusiDataDto();
				busiDataDto.setCreatedBy(userId);
				busiDataDto.setJobId(id);
				List<DscvrFilesRsp> dscvrFilesRspList = censorshipReportSrv.geteDscvrFilesRspList(busiDataDto);
				if (!dscvrFilesRspList.isEmpty())
				{
					com.spinfosec.utils.FileUtils.createEventPdfFile(dscvrFilesRspList, reportFolderPath);
				}
			}

			String reportZipPath = ZipUtil.buildDestinationZipFilePath(reportFolderFile, null);
			ZipUtil.zipMultiFile(reportFolderFile.getAbsolutePath(), reportZipPath, true);
			// ZipUtil.zip(reportFolderPath, reportFolderPath + ".zip");
			// 下载完成后删除报告文件夹
			if (reportFolderFile.exists())
			{
				FileUtils.deleteQuietly(reportFolderFile);
			}

		}
		catch (Exception e)
		{
			log.info("任务报告导出失败：失败ID" + id, e);

		}
		finally
		{
			if (reportFolderFile.exists())
			{
				FileUtils.deleteQuietly(reportFolderFile);
				FileUtils.deleteQuietly(new File(reportFolderPath + ".zip"));
			}
		}

		return ResultUtil.getSuccessResult(reportFolderFile.getName() + ".zip");
	}

	/**
	 * 根据任务中心报告名称导出对应文件
	 * @param req
	 * @param rsp
	 * @param reportName  报告名称
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/exportTaskReport/{reportName}", method = RequestMethod.GET)
	public void downTaskReport(HttpServletRequest req, HttpServletResponse rsp, @PathVariable String reportName)
			throws Exception
	{
		OutputStream toClient = null;
		InputStream fis = null;
		try
		{
			ClassLoader classLoader = CensorshipReportController.class.getClassLoader();
			URL url = classLoader.getResource("");
			String resources = URLDecoder.decode(url.getPath(), "utf-8").replaceFirst("/", "");

			File reportDocFile = new File(resources + File.separatorChar + "report" + File.separator + reportName);
			if (!reportDocFile.exists())
			{
				throw new TMCException(RspCode.FILE_NOT_EXIST);
			}
			String downLoadName = reportDocFile.getName();
			// 清空response
			rsp.reset();
			// 设置response的Header
			rsp.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"",
					new String(downLoadName.getBytes("utf-8"), "ISO-8859-1")));
			rsp.addHeader("Content-Length", "" + reportDocFile.length());
			rsp.setContentType("application/octet-strea; charset=utf-8");
			// 输出流定向到http response中
			toClient = new BufferedOutputStream(rsp.getOutputStream());
			fis = new BufferedInputStream(new FileInputStream(reportDocFile));

			byte[] pdfByte = new byte[fis.available()];
			fis.read(pdfByte);

			toClient.write(pdfByte);
			toClient.flush();

			fis.close();
			toClient.close();

			// 下载完成后删除报告文件夹和打包ZIP
			if (reportDocFile.exists())
			{
				FileUtils.deleteQuietly(reportDocFile);
			}
		}
		catch (Exception e)
		{
			log.error("任务中心报告下载失败...", e);
		}
	}
}
