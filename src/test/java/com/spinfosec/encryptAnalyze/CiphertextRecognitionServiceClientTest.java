package com.spinfosec.encryptAnalyze;

import com.spinfosec.encryptAnalyze.thrift.service.CiphertextRecognitionService;
import org.apache.commons.io.FileUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ank
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.encryptAnalyze.CiphertextRecognitionServiceClientTest
 * @description [一句话描述]
 * @create 2018/11/29 10:56
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class CiphertextRecognitionServiceClientTest
{
    private Logger logger = LoggerFactory.getLogger(CiphertextRecognitionServiceClientTest.class);

    @Test
    public void ciphertextRecognitionTest()
    {
        TTransport tTransport = null;
        try
        {
            tTransport = new TSocket("192.190.10.124", 9090, 3 * 60 * 1000);
            TProtocol tProtocol = new TBinaryProtocol(tTransport);
            CiphertextRecognitionService.Iface client = new CiphertextRecognitionService.Client(tProtocol);
            tTransport.open();

            List<String> list = new ArrayList<String>();

            File testDir = new File("D:\\share\\CIPHERTEXT");
            File[] listFiles = testDir.listFiles();

            for (File listFile : listFiles)
            {
                String s = FileUtils.readFileToString(listFile, "utf-8");
                list.add(s);
            }

            if (null != list && !list.isEmpty())
            {
                Map<String, Double> stringDoubleMap = client.ciphertextRecognition(list);
                Set<Map.Entry<String, Double>> entries = stringDoubleMap.entrySet();
                for (Map.Entry<String, Double> entry : entries)
                {
                    String key = entry.getKey();
                    Double value = entry.getValue();
                    logger.info("key:" + key);
                    logger.info("value:" + value);
                }
            }
        }
        catch (TTransportException e)
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
