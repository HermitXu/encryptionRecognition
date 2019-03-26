package com.spinfosec.utils.whiteBoxTest;

import com.spinfosec.dto.pojo.common.EncryptDecryptBean;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;

/**
 * @author ank
 * @version v 1.0
 * @title [字符串转换]
 * @ClassName: com.spinfosec.utils.whiteBoxTest.TypeConvert
 * @description [字符串转换]
 * @create 2018/12/6 11:27
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class TypeConvert
{

    public static final String BASE64 = "base64";
    public static final String HEX = "hex";

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str)
    {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString)
    {
        if (hexString == null || hexString.equals(""))
        {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c)
    {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray byte[]
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray)
    {
        if (bArray == null || bArray.length == 0)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++)
        {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
            {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串转换成字符串
     *
     * @param hexStr
     * @return String
     */
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * @param hexString String str = "000AB"
     * @return
     */
    public static int hexString2Int(String hexString)
    {
        Integer num = Integer.valueOf(hexString, 16);
        return num;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBitString(byte b)
    {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 把byte转为字符串数组的bit
     */
    public static String[] byteToBitStrings(byte b)
    {
        String[] bit = new String[8];
        bit[0] = "" + (byte) ((b >> 7) & 0x1);
        bit[1] = "" + (byte) ((b >> 6) & 0x1);
        bit[2] = "" + (byte) ((b >> 5) & 0x1);
        bit[3] = "" + (byte) ((b >> 4) & 0x1);
        bit[4] = "" + (byte) ((b >> 3) & 0x1);
        bit[5] = "" + (byte) ((b >> 2) & 0x1);
        bit[6] = "" + (byte) ((b >> 1) & 0x1);
        bit[7] = "" + (byte) ((b >> 0) & 0x1);
        return bit;
    }


    public static byte[] inputConvert(EncryptDecryptBean bean) throws UnsupportedEncodingException
    {
        String content = bean.getContent();
        byte[] contentBytes = null;
        if (String.valueOf(Cipher.ENCRYPT_MODE).equalsIgnoreCase(bean.getType())) // 加密
        {
            contentBytes = content.getBytes(bean.getCharset());
        }
        else if (String.valueOf(Cipher.DECRYPT_MODE).equalsIgnoreCase(bean.getType())) // 解密
        {
            if (BASE64.equalsIgnoreCase(bean.getOut()))
            {
                contentBytes = Base64.decode(content.getBytes(bean.getCharset()));
            }
            else if (HEX.equalsIgnoreCase(bean.getOut()))
            {
                contentBytes = TypeConvert.hexStringToBytes(content);
            }
        }
        return contentBytes;
    }

    public static String outputConvert(EncryptDecryptBean bean, byte[] resultByte) throws UnsupportedEncodingException
    {
        String result = "";
        if (String.valueOf(Cipher.ENCRYPT_MODE).equalsIgnoreCase(bean.getType())) // 加密
        {
            if (BASE64.equalsIgnoreCase(bean.getOut()))
            {
                result = Base64.toBase64String(resultByte);
            }
            else if (HEX.equalsIgnoreCase(bean.getOut()))
            {
                result = TypeConvert.bytesToHexString(resultByte);
            }
        }
        else if (String.valueOf(Cipher.DECRYPT_MODE).equalsIgnoreCase(bean.getType())) // 解密
        {
            result = new String(resultByte, bean.getCharset());
        }
        return result;
    }

    public static void main(String[] args)
    {
        String hexString = "3A60432A5C01211F291E0F4E0C132825";
        byte[] result = hexStringToBytes(hexString);
        System.out.println(new String(result));
        System.out.println(bytesToHexString(result));
    }

}
