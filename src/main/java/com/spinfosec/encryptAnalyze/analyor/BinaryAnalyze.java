package com.spinfosec.encryptAnalyze.analyor;

import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.TransforDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ank
 * @version v 1.0
 * @title [二进制解析器]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.BinaryAnalyze
 * @description [二进制解析器]
 * @create 2018/11/13 11:24
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class BinaryAnalyze extends AnalyzeBase
{
    private Logger logger = LoggerFactory.getLogger(BinaryAnalyze.class);

    /**
     * 二进制数据解析器
     * @param transforDataInfo
     * @param spDiscoveryTasks
     * @param targetResFormData
     */
    public BinaryAnalyze(TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        super(transforDataInfo, spDiscoveryTasks, targetResFormData);
    }

    /**
     * 二进制数据加密分析解析器
     */
    @Override
    public RspCode analyze()
    {
        logger.info("开始二进制数据解析：" + getTransforDataInfo().getFileMetadata().getFileName());
        return processParseBinaryToText(super.getTransforDataInfo().getFileContent(), getTransforDataInfo().getFileMetadata().getFileName(), null);
    }
}
