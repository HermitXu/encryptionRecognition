package com.spinfosec.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.spinfosec.controller.event.CensorshipReportController;
import com.spinfosec.dto.pojo.system.DscvrFilesRsp;
import com.spinfosec.system.MemInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName FileUtils
 * @Description: 〈文件工具类〉
 * @date 2018/10/31
 * All rights Reserved, Designed By SPINFO
 */
public class FileUtils
{
	static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 生成检查事件PDF报告
	 * @param dscvrFilesRsps
	 * @return
	 * @throws Exception
	 */
	public static String createEventPdfFile(List<DscvrFilesRsp> dscvrFilesRsps, String pdfPath) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		ClassLoader classLoader = CensorshipReportController.class.getClassLoader();
		URL url = classLoader.getResource("");
		String path = URLDecoder.decode(url.getPath(), "utf-8").replaceFirst("/", "");

		BaseFont baseFontChinese = BaseFont.createFont(
				path + File.separatorChar + "templates" + File.separatorChar + "simsun.ttf", BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED);
		Paragraph newLine = new Paragraph("\n");

		// 1-创建文本对象 Document
		Document document = new Document(PageSize.A4, -30, -30, 0, 5);
		// 2-初始化 pdf输出对象 PdfWriter
		String fileName = System.currentTimeMillis() + ".pdf";
		String filePath = pdfPath + File.separator + fileName;
		PdfWriter.getInstance(document, new FileOutputStream(filePath));

		// 3-打开 Document
		document.open();

		// 检查时间
		Font detectDateFont = new Font(baseFontChinese, 10, Font.BOLD);
		Paragraph detectDatePar = new Paragraph("日期:" + format.format(new Date()), detectDateFont);
		detectDatePar.setIndentationLeft(465);
		document.add(detectDatePar);
		document.add(newLine);

		// 标题
		String systitle = "加密文件检查系统";
		Font systitleFont = new Font(baseFontChinese, 25, Font.BOLD);
		Paragraph systitleParagraph = new Paragraph(systitle, systitleFont);
		systitleParagraph.setAlignment(Element.ALIGN_CENTER);

		String titile = "检查事件报告";
		Font titleFont = new Font(baseFontChinese, 15, Font.NORMAL);
		Paragraph titleParagraph = new Paragraph(titile, titleFont);
		titleParagraph.setAlignment(Element.ALIGN_CENTER);

		document.add(systitleParagraph);
		document.add(newLine);
		document.add(titleParagraph);
		document.add(newLine);

		// 生成表格
		PdfPTable table = new PdfPTable(10);
		table.setLockedWidth(false);
		table.setTotalWidth(1800);

		// 每列宽度
		table.setTotalWidth(new float[] { 55, 95, 95, 95, 95, 95, 95, 95, 95, 90 });

		Font headcellFont = new Font(baseFontChinese, 8, Font.BOLD);

		PdfPCell cell1 = new PdfPCell(new Phrase("序号", headcellFont));
		cell1.setUseAscender(true);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER); // 水平居中
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE); // 垂直居中
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("任务名称", headcellFont));
		cell2.setUseAscender(true);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("任务类型", headcellFont));
		cell3.setUseAscender(true);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Phrase("IP", headcellFont));
		cell4.setUseAscender(true);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell4);

		PdfPCell cell5 = new PdfPCell(new Phrase("文件名称", headcellFont));
		cell5.setUseAscender(true);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell5);

		PdfPCell cell6 = new PdfPCell(new Phrase("文件类型", headcellFont));
		cell6.setUseAscender(true);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell6);

		PdfPCell cell7 = new PdfPCell(new Phrase("文件路径", headcellFont));
		cell7.setUseAscender(true);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell7);

		PdfPCell cell8 = new PdfPCell(new Phrase("检查时间", headcellFont));
		cell8.setUseAscender(true);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell8);

		PdfPCell cell9 = new PdfPCell(new Phrase("是否加密", headcellFont));
		cell9.setUseAscender(true);
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell9);

		PdfPCell cell10 = new PdfPCell(new Phrase("加密算法类型", headcellFont));
		cell10.setUseAscender(true);
		cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell10);

		Font cellFont = new Font(baseFontChinese, 8);

		for (int i = 0; i < dscvrFilesRsps.size(); i++)
		{
			DscvrFilesRsp dscvrFilesRsp = dscvrFilesRsps.get(i);

			// 序号
			PdfPCell eventCell1 = new PdfPCell(new Phrase(i + 1 + "", cellFont));
			eventCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell1);

			// 任务名称
			PdfPCell eventCell2 = new PdfPCell(new Phrase(dscvrFilesRsp.getJobName(), cellFont));
			eventCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell2);

			// 任务类型
			PdfPCell eventCell3 = new PdfPCell(new Phrase(
					CommonUtil.getDscvrFilesType(CommonUtil.getDscvrFilesType(dscvrFilesRsp.getTaskType())), cellFont));
			eventCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell3);

			// IP
			PdfPCell eventCell4 = new PdfPCell(new Phrase(dscvrFilesRsp.getIp(), cellFont));
			eventCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell4);

			// 文件名称
			PdfPCell eventCell5 = new PdfPCell(new Phrase(dscvrFilesRsp.getFileName(), cellFont));
			eventCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell5);

			// 文件类型
			PdfPCell eventCell6 = new PdfPCell(new Phrase(dscvrFilesRsp.getFileExtension(), cellFont));
			eventCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell6);

			// 文件路径
			PdfPCell eventCell7 = new PdfPCell(new Phrase(dscvrFilesRsp.getFilePath(), cellFont));
			eventCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell7);

			// 检查时间
			PdfPCell eventCell8 = new PdfPCell(new Phrase(format.format(dscvrFilesRsp.getDetectDateTs()), cellFont));
			eventCell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell8);

			// 是否加密
			PdfPCell eventCell9 = new PdfPCell(
					new Phrase((formatIsEncrypt(dscvrFilesRsp.getIsEncrypt() + "")), cellFont));
			eventCell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell9);

			// 加密算法类型
			PdfPCell eventCell10 = new PdfPCell(new Phrase(
					StringUtils.isEmpty(dscvrFilesRsp.getAlgorithmType())?"未知":dscvrFilesRsp.getAlgorithmType(), cellFont));
			eventCell10.setHorizontalAlignment(Element.ALIGN_CENTER);
			eventCell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(eventCell10);

		}

		document.add(table);

		// 5-关闭 Document
		document.close();

		return fileName;
	}

	private static String formatIsEncrypt(String isEncrypt)
	{

		switch (isEncrypt)
		{
			case "0.0":
				isEncrypt = "否";
				break;
			case "1.0":
				isEncrypt = "是";
				break;
			case "2.0":
				isEncrypt = "未知";
				break;
			default:

		}
		return isEncrypt;
	}

	/**
	 * 保存文件
	 *
	 * @param stream
	 * @param path
	 * @param filename
	 * @throws IOException
	 */
	public static void SaveFileFromInputStream(InputStream stream, String path, String filename) throws IOException
	{
		// 创建文件夹
		File dir = new File(path);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		FileOutputStream fs = new FileOutputStream(path + filename);
		byte[] buffer = new byte[1024 * 1024];
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1)
		{
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}

	public static void clearDirectory(String dir)
	{
		File dirFile = new File(dir);
		if (dirFile.exists())
		{
			if (dirFile.isDirectory())
			{
				File[] files = dirFile.listFiles();
				for (File file : files)
				{
					org.apache.commons.io.FileUtils.deleteQuietly(file);
				}
			}
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param sPath 被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath)
	{
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists())
		{
			file.delete();
		}
		return true;
	}

	public static void copyFile(String srcPath, String dstPath)
	{
		try
		{
			File srcFile = new File(srcPath);
			if (null != srcFile && srcFile.exists() && srcFile.isFile())
			{
				org.apache.commons.io.FileUtils.copyFile(new File(srcPath), new File(dstPath));
			}
			else
			{
				logger.info("srcPath = " + srcPath + "，no exists!");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
