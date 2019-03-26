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
 * @ClassName: com.spinfosec.utils.whiteBoxTest.AESUtilTest
 * @description [一句话描述]
 * @create 2018/12/6 16:44
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class AESUtilTest
{
    private static String ZeroBytePadding = "ZeroBytePadding";
    private static String PKCS5Padding = "PKCS5Padding";

    private Logger logger = LoggerFactory.getLogger(AESUtilTest.class);

    @Test
    public void encryptOrDecryptTest()
    {
        EncryptDecryptBean bean = new EncryptDecryptBean();
        bean.setAlgorithm("AES");
        bean.setCharset("UTF-8");
//        bean.setContent("邮件服务器是否设置正确");
        bean.setContent("YudJ9US2qhOddA8OT9SmglJhxn8nVmVwaVAHkGHrQzKbOR3tA7+DepRng2MvUmcO");
        bean.setIv("1234567812345678");
        bean.setKey("1234567812345678");
        bean.setMode("ECB");
        bean.setOut("base64");
        bean.setPad("X9.23Padding");
        bean.setType("2");
        bean.setLength("128");

        ResponseBean responseBean = AESAndCamelliaUtil.encryptOrDecrypt(bean);

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
