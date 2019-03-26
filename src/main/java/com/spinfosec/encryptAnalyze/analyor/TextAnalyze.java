package com.spinfosec.encryptAnalyze.analyor;

import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.TransforDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ank
 * @version v 1.0
 * @title [文本解析器]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.TextAnalyze
 * @description [文本解析器]
 * @create 2018/11/13 11:23
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class TextAnalyze extends AnalyzeBase
{
    private Logger logger = LoggerFactory.getLogger(TextAnalyze.class);

    public TextAnalyze(TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        super(transforDataInfo, spDiscoveryTasks, targetResFormData);
    }

    /**
     * 文本加密分析解析器
     */
    @Override
    public RspCode analyze()
    {
        String fileText = super.getTransforDataInfo().getFileText();
        logger.info("fileText = " + fileText);
        List<String> fileTextList = new ArrayList<String>();
        fileTextList.add(fileText);
        // 调用密文解析算法
        Map<String, String> resultMap = null;
        try
        {
            resultMap = ciphertextRecognition(fileTextList);
        }
        catch (Exception e)
        {
            logger.error("调用密文识别算法发生错误", e);
            return RspCode.INVOKE_BY_SE_CIPHERTEXT_RECOGNITION_ERROR;
        }
        generalEvent(resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY), resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY), null, getTransforDataInfo().getFileMetadata().getFilePath(), getTransforDataInfo().getFileMetadata().getFileName(), getTransforDataInfo().getFileMetadata().getFileType());
        return RspCode.INVOKE_BY_SE_OK;
    }
}
