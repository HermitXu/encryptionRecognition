package com.spinfosec.utils.whiteBoxTest;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.EncryptDecryptBean;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.ResultUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * @author ank
 * @version v 1.0
 * @title [3DES加密解密工具]
 * @ClassName: com.spinfosec.utils.whiteBoxTest.DESedeUtil
 * @description [3DES加密解密工具]
 * @create 2018/12/5 15:50
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class DESedeUtil
{
    private static Logger logger = LoggerFactory.getLogger(DESedeUtil.class);

    public static ResponseBean encryptOrDecrypt(EncryptDecryptBean bean)
    {
        CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
        String result = "";
        try
        {
            logger.info("编码方式：" + bean.getCharset());
            logger.info("获取密码：" + bean.getKey());
            logger.info("获取算法：" + bean.getAlgorithm());
            logger.info("获取模式：" + bean.getMode());
            logger.info("获取填充：" + bean.getPad());
            logger.info("加解密方式：" + bean.getType() + "（加密 1/解密 2）");
            logger.info("IV值：" + bean.getIv());
            if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME))
            {
                Security.addProvider(new BouncyCastleProvider());
            }
            //生成密钥
            DESedeKeySpec desEdeKeySpec = new DESedeKeySpec(bean.getKey().getBytes(bean.getCharset()));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(bean.getAlgorithm());
            SecretKey secretKey = keyFactory.generateSecret(desEdeKeySpec);
            Cipher cipher = Cipher.getInstance(bean.getAlgorithm() + "/" + bean.getMode() + "/" + bean.getPad(), "BC");
            if (!"ECB".equalsIgnoreCase(bean.getMode()))  // ecb模式没有IV
            {
                cipher.init(Integer.parseInt(bean.getType()), secretKey, new IvParameterSpec(bean.getIv().getBytes(bean.getCharset())));
            }
            else
            {
                cipher.init(Integer.parseInt(bean.getType()), secretKey);
            }
            // 待加解密的内容的处理
            byte[] contentBytes = TypeConvert.inputConvert(bean);
            byte[] resultByte = cipher.doFinal(contentBytes);
            result = TypeConvert.outputConvert(bean, resultByte);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.error("不支持的算法类型：" + bean.getAlgorithm(), e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_NO_SUCH_ALGORITHM);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }
        catch (NoSuchPaddingException e)
        {
            logger.error("不支持的填充方式：" + bean.getPad(), e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_NO_SUCH_PADDING);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }
        catch (InvalidKeyException e)
        {
            logger.error("无效的key值：" + bean.getKey(), e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_INVALID_KEY);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("不支持的编码方式：" + bean.getCharset(), e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_UNSUPPORTED_ENCODING);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }
        catch (InvalidAlgorithmParameterException e)
        {
            logger.error("无效的IV偏移量值：" + bean.getIv(), e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_INVALID_ALGORITHM_PARAMETER);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }
        catch (BadPaddingException e)
        {
            logger.error("错误的填充方式：" + bean.getPad(), e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_BAD_PADDING);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }
        catch (Exception e)
        {
            logger.error("数据加解密失败！", e);
            codeRsp = new CodeRsp(RspCode.WHITE_TEST_STATE_ERROR);
            codeRsp.setMsg(codeRsp.getMsg() + "(" + e.getMessage() + ")");
        }

        JSONObject jsonObject = new JSONObject();
        if (RspCode.SUCCESS.getCode().equalsIgnoreCase(codeRsp.getCode()))
        {
            jsonObject.put("data", result);
        }

        return ResultUtil.getDefinedCodeResult(codeRsp, jsonObject);
    }

}
