package com.spinfosec.encryptAnalyze;

import com.spinfosec.dao.entity.SpAlgorithmFileType;
import com.spinfosec.dao.entity.SpEncryptionAlgorithm;
import com.spinfosec.dto.pojo.system.tatic.DiscoveryTaskFormData;
import com.spinfosec.encryptAnalyze.analyor.AnalyzeBase;
import com.spinfosec.encryptAnalyze.analyor.AnalyzeFactory;
import com.spinfosec.service.srv.IAlgorithmSrv;
import com.spinfosec.service.srv.ITacticSrv;
import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.FileMetadata;
import com.spinfosec.thrift.dto.Result;
import com.spinfosec.thrift.dto.TransforDataInfo;
import com.spinfosec.thrift.service.EncryptParseService;
import com.spinfosec.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ank
 * @version v 1.0
 * @title [加密解析服务处理类，SE调用]
 * @ClassName: com.spinfosec.encryptAnalyze.EncryptParseServiceImpl
 * @description [加密解析服务处理类，SE调用]
 * @create 2018/11/13 9:47
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class EncryptParseServiceImpl implements EncryptParseService.Iface
{

    private ITacticSrv tacticSrv = (ITacticSrv) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("tacticSrv");

    private IAlgorithmSrv algorithmSrv = (IAlgorithmSrv) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("algorithmSrv");

    private ApplicationProperty property = (ApplicationProperty) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("applicationProperty");

    private Logger logger = LoggerFactory.getLogger(EncryptParseServiceImpl.class);

    /**
     * 解析服务接口，SE调用
     *
     * @param transforDataInfo 调用参数对象
     * @return Result 调用返回对象，包括code 和 msg
     * @throws TException 抛出通信异常
     */
    @Override
    public Result parseFile(TransforDataInfo transforDataInfo) throws TException
    {
        Result result = new Result();
        try
        {
            logger.info("parseFile start run");
            logger.debug("SE传递的参数为" + transforDataInfo.toString());

            // 定义解析器类型，根据传入的内容来获取不同的解析器
            String analyzeType = "";

            String jobId = transforDataInfo.getJobId();
            logger.info("传入的数据策略id为：" + jobId);
            DiscoveryTaskFormData discoveryTaskById = tacticSrv.getDiscoveryTaskById(jobId);
            if (null != discoveryTaskById)
            {
                logger.info("传入的数据的策略名称为：" + discoveryTaskById.getDiscoveryTasks().getName());
                // 查询策略中是否指定了算法类型数据，指定了则将相应的类型对应的是算法存入的map中，以策略为单位
                List<SpAlgorithmFileType> algorithmFileTypeList = discoveryTaskById.getAlgorithmFileTypeList();
                Map<String, String> fileTypeAlgorithmMap = null;
                if (!JobFileTypeAlgorithmHelper.getInstance().getJobFileTypeAlgorithmMap().containsKey(jobId))
                {
                    fileTypeAlgorithmMap = new HashMap<String, String>();
                }
                else
                {
                    fileTypeAlgorithmMap = JobFileTypeAlgorithmHelper.getInstance().getJobFileTypeAlgorithmMap().get(jobId);
                }
                for (SpAlgorithmFileType spAlgorithmFileType : algorithmFileTypeList)
                {
                    SpEncryptionAlgorithm algorithm = algorithmSrv.getAlgorithm(spAlgorithmFileType.getAlgorithmId());
                    if (null != algorithm)
                    {
                        String fileTypeStr = spAlgorithmFileType.getFileType();
                        logger.info("策略中指定的算法对应文件类型为：" + algorithm.getName() + "|" + fileTypeStr);
                        String[] fileTypes = fileTypeStr.split(";");  // 用户策略保存的算法和类型的格式为*.*;*.*
                        for (String fileType : fileTypes)
                        {
                            fileTypeAlgorithmMap.put(fileType.toLowerCase(), algorithm.getName());
                        }
                    }
                }
                if (!fileTypeAlgorithmMap.isEmpty())
                {
                    JobFileTypeAlgorithmHelper.getInstance().getJobFileTypeAlgorithmMap().put(jobId, fileTypeAlgorithmMap);
                }

                String fileType = "";
                FileMetadata fileMetadata = transforDataInfo.getFileMetadata();
                if (null != fileMetadata)
                {
                    String fileName = fileMetadata.getFileName();
                    if (StringUtils.isNotEmpty(fileName))
                    {
                        logger.info("传入的数据的FileMetadata中文件名为" + fileName);
                        if (fileName.contains(".") && fileName.length() - fileName.lastIndexOf(".") <= 10)
                        {
                            fileType = "*" + fileName.substring(fileName.lastIndexOf("."));
                            // 将fileType存入到对象中，方便解析器调用
                            fileMetadata.setFileType(fileType.toLowerCase());
                        }
                    }
                }

                if (null != JobFileTypeAlgorithmHelper.getInstance().getJobFileTypeAlgorithmMap().get(jobId) && null != JobFileTypeAlgorithmHelper.getInstance().getJobFileTypeAlgorithmMap().get(jobId).get(fileType))
                {
                    // 策略中指定的算法对应类型不为空，所以采用类型解析分析器
                    analyzeType = AnalyzeFactory.ANALYZE_FILE_TYPE;
                }
                else
                {
                    String discoveryTaskType = discoveryTaskById.getDiscoveryTasks().getDiscoveryTaskType();
                    String isAnalyzeByDirOfFileSystem = property.getIsAnalyzeByDirOfFileSystem();
                    if (isAnalyzeByDirOfFileSystem.equalsIgnoreCase("1") && (Contants.DISCOVERY_TASK_TYPE_FILE.equalsIgnoreCase(discoveryTaskType) || Contants.DISCOVERY_TASK_TYPE_FTP.equalsIgnoreCase(discoveryTaskType) || Contants.DISCOVERY_TASK_TYPE_LINUX.equalsIgnoreCase(discoveryTaskType)))
                    {
                        analyzeType = AnalyzeFactory.ANALYZE_FILE_SYSTEM;
                    }
                    else
                    {
                        if (StringUtils.isNotEmpty(transforDataInfo.getFileText()))
                        {
                            analyzeType = AnalyzeFactory.ANALYZE_TEXT;
                        }
                        else if (null != transforDataInfo.getFileContent())
                        {
                            analyzeType = AnalyzeFactory.ANALYZE_BINARY;
                        }
                        else if (null != transforDataInfo.getTableContent() || null != transforDataInfo.getTableBinaryContent())
                        {
                            analyzeType = AnalyzeFactory.ANALYZE_DATABASE;
                        }
                    }
                }

                logger.info("获取的解析器类型为：" + analyzeType);
                AnalyzeBase analyze = AnalyzeFactory.getInstance().getAnalyze(analyzeType, transforDataInfo, discoveryTaskById.getDiscoveryTasks(), discoveryTaskById.getTargetResFormData());
                RspCode rspCode = analyze.analyze();
                if (null != rspCode)
                {
                    result.setCode(rspCode.getCode());
                    result.setMsg(rspCode.getDescription());
                }
            }
            else
            {
                // 策略被删除
                logger.info("策略不存在，id=" + jobId);
                result.setCode(RspCode.INVOKE_BY_SE_NO_POLICY.getCode());
                result.setMsg(RspCode.INVOKE_BY_SE_NO_POLICY.getDescription());
            }
            logger.info(result.getCode() + " : " + result.getMsg());
            logger.info("SE调用完成");
        }
        catch (Exception e)
        {
            logger.error("发生错误！", e);
            result.setCode(RspCode.INVOKE_BY_SE_ERROR.getCode());
            result.setMsg(RspCode.INVOKE_BY_SE_ERROR.getDescription());
        }
        return result;
    }

    /**
     * 测试连接接口
     * @return true
     * @throws TException
     */
    @Override
    public boolean testConnect() throws TException
    {
        return true;
    }
}
