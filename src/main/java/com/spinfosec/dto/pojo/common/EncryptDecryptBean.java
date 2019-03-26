package com.spinfosec.dto.pojo.common;

/**
 * @author ank
 * @version v 1.0
 * @title [白盒测试请求bean]
 * @ClassName: com.spinfosec.dto.pojo.common.EncryptDecryptBean
 * @description [白盒测试请求bean]
 * @create 2018/12/5 15:01
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class EncryptDecryptBean
{

    /**
     * 待加解密的内容
     */
    private String content;

    /**
     * 算法（AES/CAMELLIA/DES/DESede）
     */
    private String algorithm;

    /**
     * 类型（加密 ENCRYPT  1  /解密 DECRYPT  2）
     */
    private String type;

    /**
     * 模式（ECB/CBC/CTR/OFB/CFB）
     */
    private String mode;

    /**
     * 填充（NoPadding/PKCS5Padding/PKCS7Padding/ISO10126Padding/X9.23Padding/ZeroBytePadding）
     */
    private String pad;

    /**
     * 偏移量（ecb模式没有iv）
     */
    private String iv;

    /**
     * 密钥
     */
    private String key;

    /**
     * 输出（base64/hex）
     */
    private String out;

    /**
     * 字符集（gb2312/gbk/gb18030/utf-8/）
     * gb2312编码：简体
     * gbk编码：简繁体
     * gb18030编码：中日韩
     * utf8编码：unicode编码
     */
    private String charset;

    /**
     * 长度（AES:128/192/256位   DES:无）
     */
    private String length;


    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public String getPad()
    {
        return pad;
    }

    public void setPad(String pad)
    {
        this.pad = pad;
    }

    public String getIv()
    {
        return iv;
    }

    public void setIv(String iv)
    {
        this.iv = iv;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getOut()
    {
        return out;
    }

    public void setOut(String out)
    {
        this.out = out;
    }

    public String getCharset()
    {
        return charset;
    }

    public void setCharset(String charset)
    {
        this.charset = charset;
    }

    public String getLength()
    {
        return length;
    }

    public void setLength(String length)
    {
        this.length = length;
    }
}
