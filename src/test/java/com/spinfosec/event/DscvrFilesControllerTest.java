package com.spinfosec.event;

import com.spinfosec.dao.entity.SpDscvrFiles;
import com.spinfosec.service.srv.IEventSrv;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.GenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

/**
 * @author ank
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.event.DscvrFilesControllerTest
 * @description [一句话描述]
 * @create 2018/11/26 15:00
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DscvrFilesControllerTest
{
    @Autowired
    private IEventSrv eventSrv;
    @Test
    public void saveDscvirFilesTest()
    {
        String[] algorithmTypeArr = new String[]{"AES", "DES", "3DES", "camellia", ""};
        for (int i = 0; i < 1000; i++)
        {
            SpDscvrFiles spDscvrFiles = new SpDscvrFiles();
            String uuid = GenUtil.getUUID();
            spDscvrFiles.setId(uuid);
            spDscvrFiles.setSendTime(DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN), DateUtil.DATETIME_FORMAT_PATTERN));
            spDscvrFiles.setOrgName("默认组织单位");
            spDscvrFiles.setOrgId("a876514uj5i9efg52eklpbvne52s8fp8");
            spDscvrFiles.setJobName("test");
            spDscvrFiles.setIsAuthorized(0L);
            spDscvrFiles.setJobId("a876514uj5i9efg52eklpbvne52s8fp1");
            spDscvrFiles.setIp("192.190.10.110");
            spDscvrFiles.setIncExternalId(uuid);
            spDscvrFiles.setHostName("administrator");
            spDscvrFiles.setFilePath("d:\\share\\test" + i + ".txt");
            spDscvrFiles.setFileName("test" + i + ".txt");
            spDscvrFiles.setFileExtension("*.txt");
            spDscvrFiles.setFdateModifiedTs(DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN), DateUtil.DATETIME_FORMAT_PATTERN));
            spDscvrFiles.setFdateAccessedTs(DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN), DateUtil.DATETIME_FORMAT_PATTERN));
            spDscvrFiles.setFdateCreatedTs(DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN), DateUtil.DATETIME_FORMAT_PATTERN));
            int randomIndex = new Random().nextInt(5);
            if (randomIndex != 4)
            {
                spDscvrFiles.setAlgorithmType(algorithmTypeArr[randomIndex]);
                spDscvrFiles.setIsEncrypt(1L);
            }
            else
            {
                spDscvrFiles.setIsEncrypt(0L);
            }
            spDscvrFiles.setTaskType("FILE_SYSTEM");
            eventSrv.saveDscvrFiles(spDscvrFiles);
        }
    }
}
