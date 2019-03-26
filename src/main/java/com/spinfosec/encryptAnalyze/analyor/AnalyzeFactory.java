package com.spinfosec.encryptAnalyze.analyor;

import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dao.entity.SpTargetRes;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.thrift.dto.TransforDataInfo;

/**
 * @author ank
 * @version v 1.0
 * @title [解析器工厂]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.AnalyzeFactory
 * @description [解析器工厂]
 * @create 2018/11/27 19:56
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class AnalyzeFactory
{
    public static final String ANALYZE_FILE_TYPE = "ANALYZE_FILE_TYPE";
    public static final String ANALYZE_BINARY = "ANALYZE_BINARY";
    public static final String ANALYZE_TEXT = "ANALYZE_TEXT";
    public static final String ANALYZE_DATABASE = "ANALYZE_DATABASE";
    public static final String ANALYZE_FILE_SYSTEM = "ANALYZE_FILE_SYSTEM";

    private static AnalyzeFactory instance = null;

    private AnalyzeFactory()
    {
    }

    public static AnalyzeFactory getInstance()
    {
        if (null == instance)
        {
            instance = new AnalyzeFactory();
        }
        return instance;
    }

    /**
     * 根据解析器类型获取相应解析器
     * @param analyzeType 解析器类型
     * @param transforDataInfo 数据信息
     * @param spDiscoveryTasks 策略信息
     * @param targetResFormData 目标信息
     * @return 对应解析器
     */
    public AnalyzeBase getAnalyze(String analyzeType, TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        AnalyzeBase analyzeBase = null;
        if (ANALYZE_FILE_TYPE.equalsIgnoreCase(analyzeType))
        {
            analyzeBase = new FileTypeAnalyze(transforDataInfo, spDiscoveryTasks, targetResFormData);
        }
        else if (ANALYZE_TEXT.equalsIgnoreCase(analyzeType))
        {
            analyzeBase = new TextAnalyze(transforDataInfo, spDiscoveryTasks, targetResFormData);
        }
        else if (ANALYZE_BINARY.equalsIgnoreCase(analyzeType))
        {
            analyzeBase = new BinaryAnalyze(transforDataInfo, spDiscoveryTasks, targetResFormData);
        }
        else if (ANALYZE_DATABASE.equalsIgnoreCase(analyzeType))
        {
            analyzeBase = new DatabaseAnalyze(transforDataInfo, spDiscoveryTasks, targetResFormData);
        }
        else if (ANALYZE_FILE_SYSTEM.equalsIgnoreCase(analyzeType))
        {
            analyzeBase = new FileSystemAnalyze(transforDataInfo, spDiscoveryTasks, targetResFormData);
        }
        return analyzeBase;
    }

}
