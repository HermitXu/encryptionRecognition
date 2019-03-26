package com.spinfosec.service.impl;

import com.spinfosec.dao.AdminDao;
import com.spinfosec.dao.DscvrFilesDao;
import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dto.pojo.system.DscvrFilesRsp;
import com.spinfosec.dto.pojo.system.ExportBusiDataDto;
import com.spinfosec.dto.pojo.system.ReportWordData;
import com.spinfosec.service.srv.ICensorshipReportSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName CensorshipReportSrvImpl
 * @Description: 〈审计报告管理业务类实现类〉
 * @date 2018/10/31
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("censorshipReportSrv")
public class CensorshipReportSrvImpl implements ICensorshipReportSrv
{

	@Autowired
	private DscvrFilesDao dscvrFilesDao;

	@Autowired
	private AdminDao adminDao;

	private static final Logger log = LoggerFactory.getLogger(CensorshipReportSrvImpl.class);

	/**
	 * 导出事件PDF
	 * @param busiDataDto
	 * @return
	 */
	@Override
	public String getPdfForEvent(ExportBusiDataDto busiDataDto, String path)
	{
		try
		{
			// 根据查询条件获取相应的检查事件数据
			List<DscvrFilesRsp> dscvrFilesRspList = geteDscvrFilesRspList(busiDataDto);

			if (dscvrFilesRspList.size() == 0)
			{
				throw new TMCException(RspCode.OBJECE_NOT_EXIST);
			}

			String eventPdfFile = FileUtils.createEventPdfFile(dscvrFilesRspList, path);
			return eventPdfFile;
		}
		catch (Exception e)
		{
			log.error("检查事件文件导出失败...", e);
		}

		return null;

	}

	/**
	 * 根据任务ID查询导出word所需数据
	 * @param id
	 * @return
	 */
	@Override
	public ReportWordData getReportWordByTaskId(String id)
	{
		try
		{
			ReportWordData wordData = new ReportWordData();
			List<ReportWordData> wordDatas = dscvrFilesDao.getReportWordByTaskId(id);
			wordData = wordDatas.get(0);

			// 统计多次任务的所有扫描数的和
			Long total = 0L;
			for (ReportWordData wordData1 : wordDatas)
			{
				total = total + Long.parseLong(wordData1.getCheckNum());
			}

			wordData.setCheckNum(total.toString());
			// 密文总数
			Long ciphertextNum = dscvrFilesDao.getCiphertextNum(id);
			// 明文总数
			Long plaintextNumNum = dscvrFilesDao.getPlaintextNumNum(id);

			wordData.setCiphertextNum(ciphertextNum);
			wordData.setPlaintextNum(plaintextNumNum);

			// 商用密文总数
			Long businessPassNum = dscvrFilesDao.getBusinessPassNum(id);
			// 非商用密文总数
			Long unBusinessPassNum = dscvrFilesDao.getUnBusinessPassNum(id);

			wordData.setBusinessPassNum(businessPassNum);
			wordData.setUnBusinessPassNum(unBusinessPassNum);

			return wordData;
		}
		catch (Exception e)
		{
			log.info("任务报告导出失败：失败ID" + id, e);
			throw new TMCException(RspCode.EXPORT_FILE_ERROR);
		}
	}

	/**
	 * 根据查询条件获取事件列表
	 * @param busiDataDto
	 * @return
	 */
	@Override
	public List<DscvrFilesRsp> geteDscvrFilesRspList(ExportBusiDataDto busiDataDto)
	{
		SpAdmins admin = adminDao.findSpAdminById(busiDataDto.getCreatedBy());
		// secadmin用户查询所有事件
		if (admin.getDefinitionType().equals(Contants.C_PRE_DEFINE))
		{
			busiDataDto.setCreatedBy(null);
		}

		// 加密算法 空字符是全部 “-1”为未知
		String algorithmType = busiDataDto.getAlgorithmType();
		if (StringUtils.isNotEmpty(algorithmType) && algorithmType.equals("-1"))
		{
			// 未知ALGORITHM_TYPE字段为空
			busiDataDto.setAlgorithmType("");
		}
		else if (StringUtils.isNotEmpty(algorithmType) && !algorithmType.equals("-1"))
		{
			// 加密算法 不为-1时 则查询指定算法
			busiDataDto.setAlgorithmType(algorithmType);
		}
		else if (StringUtils.isEmpty(algorithmType))
		{
			// 加密算法 为空时 说明查询全部
			busiDataDto.setAlgorithmType(null);
		}

		return dscvrFilesDao.getDscvrFilesByBusiDataDto(busiDataDto);
	}

	public static void main(String[] args) throws Exception
	{
		String pdfPath = "G:/encryptionRecognition/target/classes/templates/1541059293131.pdf";
		File pdfFile = new File(pdfPath);
		org.apache.commons.io.FileUtils.deleteQuietly(pdfFile);

	}

}
