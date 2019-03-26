package com.spinfosec.encryptAnalyze.analyor;

import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.encryptAnalyze.JobFileTypeAlgorithmHelper;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.TransforDataInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author ank
 * @version v 1.0
 * @title [文件类型解析器]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.FileTypeAnalyze
 * @description [文件类型解析器]
 * @create 2018/11/13 11:19
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class FileTypeAnalyze extends AnalyzeBase
{

    private Logger logger = LoggerFactory.getLogger(FileTypeAnalyze.class);

    public FileTypeAnalyze(TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        super(transforDataInfo, spDiscoveryTasks, targetResFormData);
        logger.info("文件类型解析器创建！");
    }

    /**
     * 文件类型解析器
     */
    @Override
    public RspCode analyze()
    {
        logger.info("文件类型解析器开始执行！");
        TransforDataInfo transforDataInfo = getTransforDataInfo();
        String fileType = transforDataInfo.getFileMetadata().getFileType();
        String jobId = transforDataInfo.getJobId();
        Map<String, String> fileTypeAlgorithmMap = JobFileTypeAlgorithmHelper.getInstance().getJobFileTypeAlgorithmMap().get(jobId);
        // 判断此map不为空，则说明，策略中选择了算法对应的类型
        if (null != fileTypeAlgorithmMap && !fileTypeAlgorithmMap.isEmpty())
        {
            logger.info("扫描的文件为：" + getTransforDataInfo().getFileMetadata().getFilePath());
            String algorithm = fileTypeAlgorithmMap.get(fileType);
            String isEncrypt;
            if (StringUtils.isNotEmpty(algorithm))
            {
                isEncrypt = ENCRYPT_STATUS_YES;
            }
            else
            {
                isEncrypt = ENCRYPT_STATUS_NO;
                algorithm = ENCRYPT_ALGORITHM_RESULT_NO;
            }
            logger.info("匹配到的算法为：" + algorithm);
            generalEvent(isEncrypt, algorithm, null, getTransforDataInfo().getFileMetadata().getFilePath(), getTransforDataInfo().getFileMetadata().getFileName(), fileType);
        }
        logger.info("文件类型解析器执行完成！");
        return RspCode.INVOKE_BY_SE_OK;
    }
}
