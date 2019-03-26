package com.spinfosec.encryptAnalyze;

import com.spinfosec.thrift.dto.FileMetadata;
import com.spinfosec.thrift.dto.Result;
import com.spinfosec.thrift.dto.TransforDataInfo;
import com.spinfosec.thrift.service.EncryptParseService;
import com.spinfosec.utils.DateUtil;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author ank
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.encryptAnalyze.EncryptParseServiceImplClientTest
 * @description [一句话描述]
 * @create 2018/11/20 14:08
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class EncryptParseServiceImplClientTest
{
    private Logger logger = LoggerFactory.getLogger(EncryptParseServiceImplClientTest.class);

    @Test
    public void testParseFile()
    {
        for (int i = 0; i < 100000; i++)
        {
            TTransport tTransport = null;
            try
            {
                tTransport = new TSocket("127.0.0.1", 9999, 30 * 1000);
                TProtocol tProtocol = new TBinaryProtocol(tTransport);
                EncryptParseService.Iface client = new EncryptParseService.Client(tProtocol);
                tTransport.open();

                TransforDataInfo transforDataInfo = new TransforDataInfo();
                transforDataInfo.setFileText("helloworld");
                transforDataInfo.setJobId("1111111111");
                FileMetadata fileMetadata = new FileMetadata();
                fileMetadata.setFileType("*.txt");
                fileMetadata.setCreateTime(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN));
                fileMetadata.setAccessTime(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN));
                fileMetadata.setFileName("aaa.txt");
                fileMetadata.setFilePath("d:\\aaa.txt");
                transforDataInfo.setFileMetadata(fileMetadata);

                Result result = client.parseFile(transforDataInfo);
                logger.info(result.getCode());
                logger.info(result.getMsg());

            }
            catch (TTransportException e)
            {
                e.printStackTrace();
            }
            catch (TException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (null != tTransport)
                {
                    tTransport.close();
                }
            }
        }
    }
}
