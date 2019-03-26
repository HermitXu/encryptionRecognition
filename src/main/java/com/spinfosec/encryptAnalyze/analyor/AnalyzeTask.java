package com.spinfosec.encryptAnalyze.analyor;

import com.spinfosec.dao.TaskCenterDao;
import com.spinfosec.dao.entity.SpTask;
import com.spinfosec.system.MemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ank
 * @version v 1.0
 * @title [解析器任务]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.AnalyzeTask
 * @description [解析器任务，负责处理比如一些清理的操作等等]
 * @create 2019/1/22 17:53
 * @copyright Copyright(C) 2019 SHIPING INFO Corporation. All rights reserved.
 */
public class AnalyzeTask
{
    private static Logger logger = LoggerFactory.getLogger(AnalyzeTask.class);

    public static Thread thread = null;

    public static void doRun()
    {
        if (null == thread)
        {
            thread = new Thread()
            {
                long hour = 60 * 60 * 1000;
                long during = 3 * 60 * 1000;
                Map<String, Integer> lastTotalCountForJob = new HashMap<String, Integer>();
                TaskCenterDao taskCenterDao = (TaskCenterDao) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("taskCenterDao");
                @Override
                public void run()
                {
                    try
                    {
                        while (true)
                        {
                            this.sleep(during);
                            logger.info("解析器清理任务执行");
                            // 清理FILE_SYSTEM_ANALYZE
                            Set<String> fileSystemJobIds = FileSystemAnalyze.jobFileListMap.keySet();
                            if (null != fileSystemJobIds && !fileSystemJobIds.isEmpty())
                            {
                                for (String jobId : fileSystemJobIds)
                                {
                                    SpTask task = taskCenterDao.getIsLastTaskByJobId(jobId);
                                    if (null != task)
                                    {
                                        if (null != task.getEndTime())
                                        {
                                            long jobFinishedTime = task.getEndTime().getTime();
                                            if (System.currentTimeMillis() - jobFinishedTime > 2 * hour)
                                            {
                                                logger.info("清理文件解析器下" + jobId + "的map数据");
                                                FileSystemAnalyze.clear(jobId);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        logger.info("清理文件解析器下" + jobId + "的map数据");
                                        FileSystemAnalyze.clear(jobId);
                                    }
                                }
                            }

                            // 清理DATABASE_ANALYZE
                            Set<String> databaseJobIds = DatabaseAnalyze.dropDataTableMap.keySet();
                            if (null != databaseJobIds && !databaseJobIds.isEmpty())
                            {
                                for (String databaseJobId : databaseJobIds)
                                {
                                    SpTask task = taskCenterDao.getIsLastTaskByJobId(databaseJobId);
                                    if (null != task)
                                    {
                                        if (null != task.getEndTime())
                                        {
                                            long jobFinishedTime = task.getEndTime().getTime();
                                            if (System.currentTimeMillis() - jobFinishedTime > 2 * hour)
                                            {
                                                logger.info("清理数据库解析器下" + databaseJobId + "的map数据");
                                                DatabaseAnalyze.dropDataTableMap.remove(databaseJobId);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        logger.info("清理数据库解析器下" + databaseJobId + "的map数据");
                                        DatabaseAnalyze.dropDataTableMap.remove(databaseJobId);
                                    }
                                }
                            }

                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                }
            };
            thread.start();
        }
    }
}
