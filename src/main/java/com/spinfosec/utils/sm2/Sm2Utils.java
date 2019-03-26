package com.spinfosec.utils.sm2;

import com.spinfosec.utils.Utils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;

import java.math.BigInteger;

/**
 * 国密算法 Sm2 实现
 *
 * @author liuqianfei
 * @since 2018/6/24 13:40
 */
public class Sm2Utils
{
	public static final String PUBLIC_KEY = "04fc06033b32cfdd0ebf3582c00feee6ad7982acc95d356c88e27d3fe4da55a11b2339840976b38813f1236bf00f11fc376a08aea56e7ed38b44d661eae44912e1";

	public static final String PRIVATE_KEY = "630141d00aff363e7c42fdd00ede52a97802575e3cdd194ac0dbec07a9981b7b";
    /**
     * 生成随机密钥对
     */
    public static SMKeyPair generateKeyPair()
    {
        return generateKeyPair(false);
    }

    /**
     * 生成随机密钥对
     */
    public static SMKeyPair generateKeyPair(boolean compressPublicKey)
    {
        Sm2 sm2 = Sm2.instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();

        byte[] publicKeyBytes = publicKey.getEncoded(compressPublicKey);
        byte[] privateKeyBytes = privateKey.toByteArray();

        String publicKeyHex = Utils.byteToHex(publicKeyBytes);
        String privateKeyHex = Utils.byteToHex(privateKeyBytes);

        return new SMKeyPair(publicKeyHex, privateKeyHex);
    }

    /**
     * 数据加密，返回十六进制形式字符串
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密字符串
     */
    public static String encryptString(String publicKey, byte[] data)
    {
        byte[] publicKeys = Utils.hexToByte(publicKey);
        return encryptString(publicKeys, data);
    }

    /**
     * 数据加密，返回十六进制形式字符串
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密字符串
     */
    public static String encryptString(byte[] publicKey, byte[] data)
    {
        if (publicKey == null || publicKey.length == 0)
        {
            return null;
        }

        if (data == null || data.length == 0)
        {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        Sm2 sm2 = Sm2.instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.initEnc(sm2, userKey);
        cipher.encrypt(source);
        byte[] c3 = new byte[32];
        cipher.dofinal(c3);

        // C1 C2 C3拼装成加密字串
        return Utils.byteToHex(c1.getEncoded(false)) + Utils.byteToHex(source) + Utils.byteToHex(c3);

    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密序列
     */
    public static byte[] encrypt(String publicKey, byte[] data)
    {
        byte[] publicKeys = Utils.hexToByte(publicKey);
        return encrypt(publicKeys, data);
    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密序列
     */
    public static byte[] encrypt(byte[] publicKey, byte[] data)
    {
        if (publicKey == null || publicKey.length == 0)
        {
            return null;
        }

        if (data == null || data.length == 0)
        {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        Sm2 sm2 = Sm2.instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.initEnc(sm2, userKey);
        cipher.encrypt(source);
        byte[] c3 = new byte[32];
        cipher.dofinal(c3);

        return Arrays.concatenate(c1.getEncoded(false), source, c3);
    }

    /**
     * 数据解密
     *
     * @param privateKey    私钥
     * @param encryptedData 数据
     * @return 明文序列
     */
    public static byte[] decrypt(String privateKey, String encryptedData)
    {
        byte[] privateKeys = Utils.hexToByte(privateKey);
        byte[] encryptedDatas = Utils.hexToByte(encryptedData);
        return decrypt(privateKeys, encryptedDatas);
    }

    /**
     * 数据解密
     *
     * @param privateKey    私钥
     * @param encryptedData 数据
     * @return 明文序列
     */
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData)
    {
        if (privateKey == null || privateKey.length == 0)
        {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0)
        {
            return null;
        }

        // 加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Utils.byteToHex(encryptedData);

        /*
         * 分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = Utils.hexToByte(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Utils.hexToByte(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = Utils.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        Sm2 sm2 = Sm2.instance();
        BigInteger userD = new BigInteger(1, privateKey);

        // 通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.initDec(userD, c1);
        cipher.decrypt(c2);
        cipher.dofinal(c3);

        // 返回解密结果
        return c2;
    }
}
