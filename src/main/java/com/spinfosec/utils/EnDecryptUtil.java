package com.spinfosec.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName EncryptDecryptUtil
 * @Description: 〈加密解密工具类〉
 * @date 2018/11/14
 * All rights Reserved, Designed By SPINFO
 */
public class EnDecryptUtil
{
	private static final Logger log = LoggerFactory.getLogger(EnDecryptUtil.class);

	/**
	 * AES加密方式
	 */
	private static final String AES_ALGORITHM = "AES";

	/**
	 * DES加密方式
	 */
	private static final String DES_ALGORITHM = "DES";

	/**
	 * 3DES加密方式
	 */
	private static final String DES3_ALGORITHM = "DESede";

	/**
	 * AES加密算法
	 */
	private static final String AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * 编码方式
	 */
	public static final String CODE_TYPE = "UTF-8";

	/**
	 * AES解密
	 * @param content 待机密内容
	 * @param password 加密密码
	 * @return
	 */
	public static String AESEncrypt(String content, String password)
	{
		try
		{
			// 创建密码器
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			byte[] byteContent = content.getBytes(CODE_TYPE);
			// 初始化为加密模式的密码器
			cipher.init(Cipher.ENCRYPT_MODE, getAESSecretKey(password));
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			// 通过Base64转码返回
			return new String(new BASE64Encoder().encode(result));
		}
		catch (Exception e)
		{
			log.info("AES加密失败！", e);
		}

		return null;
	}

	/**
	 * AES解密
	 * @param content  待解密内容
	 * @param password 解密密码
	 * @return
	 */
	public static String AESDecrypt(String content, String password)
	{
		try
		{
			// 实例化
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getAESSecretKey(password));
			// 执行操作
			byte[] result = cipher.doFinal(Base64.decodeBase64(content));
			return new String(result, CODE_TYPE);
		}
		catch (Exception e)
		{
			log.info("AES解密失败！", e);
		}

		return null;
	}

	/**
	 * DES加密
	 * @param content 待加密内容
	 * @param password 加密密码
	 * @return
	 */
	public static String DESEnCrypt(String content, String password)
	{
		try
		{
			// 创建密码器
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			byte[] byteContent = content.getBytes(CODE_TYPE);
			// 初始化为加密模式的密码器
			cipher.init(Cipher.ENCRYPT_MODE, getDESSecretKey(password));
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			// 通过Base64转码返回
			return Base64.encodeBase64String(result);
		}
		catch (Exception e)
		{
			log.info("DES加密失败！", e);
		}

		return null;
	}

	/**
	 * DES解密
	 * @param content 待解密内容
	 * @param password 解密密码
	 * @return
	 */
	public static String DESDeCrypt(String content, String password)
	{
		try
		{
			// 实例化
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getDESSecretKey(password));
			// 执行操作
			byte[] result = cipher.doFinal(Base64.decodeBase64(content));
			return new String(result, CODE_TYPE);
		}
		catch (Exception e)
		{
			log.info("DES解密失败！", e);
		}

		return null;
	}

	/**
	 * DES3加密
	 * @param content 待加密内容
	 * @param password 加密密码
	 * @return
	 */
	public static String DES3EnCrypt(String content, String password)
	{
		try
		{
			// 创建密码器
			Cipher cipher = Cipher.getInstance(DES3_ALGORITHM);
			byte[] byteContent = content.getBytes(CODE_TYPE);
			// 初始化为加密模式的密码器
			cipher.init(Cipher.ENCRYPT_MODE, getDES3SecretKey(password));
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			// 通过Base64转码返回
			return Base64.encodeBase64String(result);
		}
		catch (Exception e)
		{
			log.info("DES3加密失败！", e);
		}

		return null;
	}

	/**
	 * DES3解密
	 * @param content 待解密内容
	 * @param password 解密密码
	 * @return
	 */
	public static String DES3DeCrypt(String content, String password)
	{
		try
		{
			// 实例化
			Cipher cipher = Cipher.getInstance(DES3_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getDES3SecretKey(password));
			// 执行操作
			byte[] result = cipher.doFinal(Base64.decodeBase64(content));
			return new String(result, CODE_TYPE);
		}
		catch (Exception e)
		{
			log.info("DES3解密失败！", e);
		}

		return null;

	}

	/**
	 * 生成加密密钥
	 * @param password
	 * @return
	 */
	private static SecretKeySpec getAESSecretKey(final String password)
	{
		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		try
		{
			KeyGenerator kg = KeyGenerator.getInstance(AES_ALGORITHM);
			//// 生成一个128位的随机源,根据传入的字节数组
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kg.init(128, random);
			// 产生原始对称密钥
			SecretKey secretKey = kg.generateKey();
			// 两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
			return new SecretKeySpec(secretKey.getEncoded(), AES_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.info("AES生成加密密钥失败！", e);
		}
		return null;
	}

	private static SecretKey getDESSecretKey(final String password) throws NoSuchAlgorithmException
	{
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		// 为我们选择的DES算法生成一个KeyGenerator对象
		KeyGenerator kg = null;
		try
		{
			kg = KeyGenerator.getInstance(DES_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.info("DES生成加密密钥失败！", e);
		}
		kg.init(secureRandom);
		return kg.generateKey();
	}

	private static SecretKey getDES3SecretKey(final String password) throws NoSuchAlgorithmException
	{
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		// 为我们选择的DES算法生成一个KeyGenerator对象
		KeyGenerator kg = null;
		try
		{
			kg = KeyGenerator.getInstance(DES3_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.info("DES3生成加密密钥失败！", e);
		}
		kg.init(secureRandom);
		return kg.generateKey();
	}

	/**
	 * 字符串转换成十六进制字符串
	 *
	 * @param str str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
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
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	/**
	 * 十六进制转换字符串
	 *
	 * @param  hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
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

	public static String AESEncrypt1(String content, String password) throws Exception
	{
		return "";
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println(AESEncrypt("123111111111111111", "123"));
		System.out.println(AESDecrypt("PXNdysWhHMCoe/6D7RqG2/oojvgGPUGefdeIFFXjBMk=", "123"));
		// System.out.println(str2HexStr("123"));
		// System.out.println(hexStr2Str(str2HexStr("123")));
		// System.out.println("AES开始加密：" + System.currentTimeMillis() / 1000);
		// System.out.println(AESEncrypt("123", "11111"));
		//
		// System.out.println("AES开始解密：" + System.currentTimeMillis() / 1000);
		// System.out.println(AESDecrypt(AESEncrypt("123", "11111"), "11111"));
		//
		// System.out.println("----------------------");
		//
		System.out.println("DES开始加密：" + System.currentTimeMillis() / 1000);
		System.out.println(DESEnCrypt("123", "123"));

		System.out.println("DES开始解密：" + System.currentTimeMillis() / 1000);
		System.out.println(DESDeCrypt(DESEnCrypt("123", "123"), "123"));
		//
		// System.out.println("----------------------");
		//
		// System.out.println("DES3开始加密：" + System.currentTimeMillis() / 1000);
		// System.out.println(DES3EnCrypt("123", "111111111111111"));
		//
		// System.out.println("DES3开始解密：" + System.currentTimeMillis() / 1000);
		// System.out.println(DES3DeCrypt(DES3EnCrypt("123", "111111111111111"), "111111111111111"));

	}

}
