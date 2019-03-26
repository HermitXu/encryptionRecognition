package com.spinfosec.encryptAnalyze;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ank
 * @version v 1.0
 * @title [单例类，用于缓存策略下每个文件类型对应的算法类型]
 * @ClassName: com.spinfosec.encryptAnalyze.JobFileTypeAlgorithmHelper
 * @description [单例类，用于缓存策略下每个文件类型对应的算法类型]
 * @create 2018/11/14 16:52
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class JobFileTypeAlgorithmHelper
{
    private static JobFileTypeAlgorithmHelper instance = new JobFileTypeAlgorithmHelper();

    // 保存策略下所选择的文件类型和算法的对应关系
    private Map<String, Map<String, String>> jobFileTypeAlgorithmMap = new HashMap<String, Map<String, String>>();

    private JobFileTypeAlgorithmHelper()
    {

    }

    public static JobFileTypeAlgorithmHelper getInstance()
    {
        if (null == instance)
        {
            instance = new JobFileTypeAlgorithmHelper();
        }
        return instance;
    }

    /**
     * 获取缓存对象
     * @return
     */
    public Map<String, Map<String, String>> getJobFileTypeAlgorithmMap()
    {
        return jobFileTypeAlgorithmMap;
    }

    /**
     * 根据策略id清楚缓存对象
     * @param jobId
     */
    public void clearJobFileTypeAlgorithmMap(String jobId)
    {
        if (jobFileTypeAlgorithmMap.containsKey(jobId))
        {
            jobFileTypeAlgorithmMap.get(jobId).clear();
            jobFileTypeAlgorithmMap.remove(jobId);
        }
    }
}
