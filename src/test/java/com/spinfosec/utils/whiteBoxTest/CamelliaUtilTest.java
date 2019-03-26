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
public class CamelliaUtilTest
{
    private Logger logger = LoggerFactory.getLogger(CamelliaUtilTest.class);

    @Test
    public void encryptOrDecryptTest()
    {
        EncryptDecryptBean bean = new EncryptDecryptBean();
        bean.setAlgorithm("Camellia");
        bean.setCharset("UTF-8");
//        bean.setContent("hello world");
        bean.setContent("pFQNFBtU5grzxcnXwqzJOg==");
        bean.setIv("1234567890123456");
        bean.setKey("Spinfo0123111111");
        bean.setMode("CBC");
        bean.setOut("base64");
        bean.setPad("PKCS5Padding");
        bean.setType("2");

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
