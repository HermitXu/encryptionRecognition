package com.spinfosec.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.util.Arrays;
import java.util.UUID;

/**
 * 国密加解密算法 SM4 实现
 *
 * @author liuqianfei
 * @since 2018/6/25 11:50
 */
public class SM4Util
{

    public static final String KEY_TO_SM4 = "79756EEA071F4D2716EE240A242BA808";

    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String ALGORITHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    public static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 生成 SM4 加解密键
     *
     * @return 128 bit / 16 byte 数组
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     */
    public static byte[] generateKey() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        return generateKey(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成键
     *
     * @return {@code keySize} bit 数组
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     */
    public static byte[] generateKey(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * SM4 ECB padding encrypt
     *
     * @param key  加密建
     * @param data 数据
     * @return SM4 ECB padding encrypt result
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     */
    public static byte[] encryptEcbPadding(byte[] key, byte[] data) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException
    {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * SM4 ECB padding encrypt
     *
     * @param key  加密建
     * @param data 数据
     * @return SM4 ECB padding encrypt result
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     */
    public static String encryptEcbPaddingString(byte[] key, byte[] data) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException
    {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(data);
        return Base64.toBase64String(result);
    }

    /**
     * SM4 ECB padding decrypt
     *
     * @param key        加密建
     * @param cipherText 加密字符串，Base64编码
     * @return 解密数据序列
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     */
    public static byte[] decryptEcbPadding(byte[] key, String cipherText) throws IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException
    {
        byte[] cipherBytes = Base64.decode(cipherText);
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherBytes);
    }

    /**
     * SM4 ECB padding decrypt
     *
     * @param key        加密建
     * @param cipherText 加密序列
     * @return 解密数据序列
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     */
    public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException
    {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }



    /**
     * SM4 CBC padding encrypt
     * @param key  加密键
     * @param iv   IV
     * @param data 数据
     * @return SM4 CBC padding encrypt result
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     * @throws InvalidAlgorithmParameterException throw new {@link InvalidAlgorithmParameterException}
     */
    public static byte[] encryptCbcPadding(byte[] key, byte[] iv, byte[] data) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException
    {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    /**
     * SM4 CBC padding encrypt
     * @param key  加密键
     * @param iv   IV
     * @param data 数据
     * @return SM4 CBC padding encrypt result
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     * @throws InvalidAlgorithmParameterException throw new {@link InvalidAlgorithmParameterException}
     */
    public static String encryptCbcPaddingString(byte[] key, byte[] iv, byte[] data) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException
    {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key, iv);
        byte[] result = cipher.doFinal(data);
        return Base64.toBase64String(result);
    }

    /**
     * SM4 CBC padding decrypt
     *
     * @param key        加密键
     * @param iv         IV
     * @param cipherText 加密字符串，Base64编码
     * @return SM4 CBC padding decrypt result
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     * @throws InvalidAlgorithmParameterException throw new {@link InvalidAlgorithmParameterException}
     */
    public static byte[] decryptCbcPadding(byte[] key, byte[] iv, String cipherText)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException
    {
        byte[] cipherBytes = Base64.decode(cipherText);
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherBytes);
    }

    /**
     * SM4 CBC padding decrypt
     *
     * @param key        加密键
     * @param iv         IV
     * @param cipherText 加密序列
     * @return SM4 CBC padding decrypt result
     * @throws IllegalBlockSizeException throw new {@link IllegalBlockSizeException}
     * @throws BadPaddingException throw new {@link BadPaddingException}
     * @throws InvalidKeyException throw new {@link InvalidKeyException}
     * @throws NoSuchAlgorithmException throw new {@link NoSuchAlgorithmException}
     * @throws NoSuchProviderException throw new {@link NoSuchProviderException}
     * @throws NoSuchPaddingException throw new {@link NoSuchPaddingException}
     * @throws InvalidAlgorithmParameterException throw new {@link InvalidAlgorithmParameterException}
     */
    public static byte[] decryptCbcPadding(byte[] key, byte[] iv, byte[] cipherText)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException
    {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherText);
    }



    /**
     * generate SM4 ECB cipher
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * generate SM4 CBC cipher
     */
    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key, byte[] iv)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException
    {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(mode, sm4Key, ivParameterSpec);
        return cipher;
    }

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException
    {
        String plainText = System.currentTimeMillis() + "#" + UUID.randomUUID().toString();
        System.out.println("明文：" + plainText + "\n\n");

//        byte[] key = SM4Util.generateKey();
        String keyString = KEY_TO_SM4;
        System.out.println(keyString);
        byte[] key = Utils.hexStringToBytes(keyString);
        // ECB
        byte[] cipherBytes = SM4Util.encryptEcbPadding(key, plainText.getBytes());
        System.out.println("SM4 ECB Padding encrypt result bytes :\n" + Arrays.toString(cipherBytes) + "\n");

        String cipherText = SM4Util.encryptEcbPaddingString(key, plainText.getBytes());
        System.out.println("SM4 ECB Padding encrypt result base64:\n" + cipherText + "\n");
        System.out.println("SM4 ECB Padding encrypt result URLEncode:\n" + URLEncoder.encode(cipherText, "utf-8") + "\n");


        byte[] decryptedData = SM4Util.decryptEcbPadding(key, cipherBytes);
        System.out.println("SM4 ECB Padding decrypt result:\n" + new String(decryptedData) + "\n\n");

        System.out.println(new String(SM4Util.decryptEcbPadding(key, cipherText)));
    }
}
