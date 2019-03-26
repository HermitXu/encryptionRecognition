package com.spinfosec.utils.whiteBoxTest;

import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.EncryptDecryptBean;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.system.RspCode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ank
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.utils.whiteBoxTest.DESUtilTest
 * @description [一句话描述]
 * @create 2018/12/6 19:44
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class DESUtilTest
{
    private Logger logger = LoggerFactory.getLogger(DESUtilTest.class);

    @Test
    public void encryptOrDecryptTest()
    {
        EncryptDecryptBean bean = new EncryptDecryptBean();
        bean.setAlgorithm("DES");
        bean.setCharset("UTF-8");
//        bean.setContent("hello world");
        bean.setContent("A4MwFMGoP9++kd56a7Fu4A==");
        bean.setIv("12345678");
        bean.setKey("Spinfo0123");
        bean.setMode("CBC");
        bean.setOut("base64");
//        bean.setPad("X9.23Padding");
        bean.setPad("ZeroBytePadding");
        bean.setType("2");

        ResponseBean responseBean = DESUtil.encryptOrDecrypt(bean);

        CodeRsp codeRsp = responseBean.getCodeRsp();
        if (codeRsp.getCode().equalsIgnoreCase(RspCode.SUCCESS.getCode()))
        {
            logger.info("操作成功");
            Object data = responseBean.getData();
            logger.info("data = " + data.toString());
        }
        else
        {
            logger.info(codeRsp.getCode() + ":" + codeRsp.getMsg());
        }
    }
}
