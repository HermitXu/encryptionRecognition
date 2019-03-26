package com.spinfosec.utils;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @title [加密类]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-6-9 下午10:52:16
 */
public class EncUtil
{

	public static final String SYS_PRIVATE_KEY = "vJ3vYkXWB7wP";

	// 算法名称
	public static final String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式
	// DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

	private static final int RANDOM_PWD_LEN = 8;

	// 随机字符串
	private static final String RANDOM_STR = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	private static final String RANDOM_NUM = "23456789";
	private static final String RANDOM_SPECIAL_CHAR = "!@#$%^&()+*/|{}._=";
	private static final String RANDOM_BIG_LETTER = "ABCDEFGHJKMNPQRSTUVWXYZ";
	private static final String RANDOM_SMALL_LETTER = "abcdefghjkmnpqrstuvwxyz";
	private static final String RANDOM_LETTER = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	private static final String RANDOM_NUM_AND_SPECIAL_CHAR = "23456789!@#$%^&()+*/|{}._=";
	private static final String RANDOM_LETTER_AND_SPECIAL_CHAR = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz!@#$%^&()+*/|[]{}._=";
	private static final String RANDOM_ALL = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz!@#$%^&()+*/|{}._=";

	/**
	 * 转换密钥
	 */
	private static Key toKey(byte[] key) throws Exception
	{
		DESKeySpec dks = new DESKeySpec(key); // 实例化Des密钥
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM); // 实例化密钥工厂
		SecretKey secretKey = keyFactory.generateSecret(dks); // 生成密钥
		return secretKey;
	}

	/**
	 * 加密数据
	 * 
	 * @param data 待加密数据
	 * @param
	 * @return 加密后的数据
	 */
	public static String encrypt(String data) throws Exception
	{
		Key k = toKey(SYS_PRIVATE_KEY.getBytes()); // 还原密钥
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
		cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
		String encStr = Base64.encodeBase64String(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
		return toSha256(encStr);
	}

	/**
	 * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
	 * 
	 * @param strSrc 要加密的字符串
	 * @param
	 * @return
	 */
	public static String toSha256(String strSrc)
	{
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try
		{
			md = MessageDigest.getInstance("SHA-256");
			md.update(bt);
			strDes = byte2hex(md.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}
		return strDes;
	}

	public static String byte2hex(byte[] b)
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
			{
				hs = hs + "0" + stmp;
			}
			else
			{
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static String genLicense(String shaStr)
	{
		String license = "";
		char[] arrs = shaStr.toCharArray();
		for (int i = 0; i < arrs.length; i = i + 2)
		{
			license = license + arrs[i];
			if (license.replaceAll("-", "").length() % 5 == 0)
			{
				license = license + "-";
			}

			if (license.replaceAll("-", "").length() == 25)
			{
				break;
			}
		}
		return license.substring(0, 29).toUpperCase();
	}

	public static String getRandomPwd()
	{
		String password = "";
		for (int i = 0; i < RANDOM_PWD_LEN; i++)
		{
			Random rand = new Random();
			password += String.valueOf(RANDOM_STR.charAt(rand.nextInt(RANDOM_STR.length())));
		}
		return password;
	}

	public static String getRandomPwdBySecPasswordPolicy(int randomNum, List<String> spSecPasswordComplexityItemIdList)
	{
		String password = "";
		if (!spSecPasswordComplexityItemIdList.contains("1") && !spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_STR.charAt(rand.nextInt(RANDOM_STR.length())));
			}
		}
		else if (spSecPasswordComplexityItemIdList.contains("1") && !spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_NUM.charAt(rand.nextInt(RANDOM_NUM.length())));
			}
		}
		else if (!spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("3") && !spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_LETTER.charAt(rand.nextInt(RANDOM_LETTER.length())));
			}
		}
		else if (!spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& spSecPasswordComplexityItemIdList.contains("3") && !spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 1; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_LETTER.charAt(rand.nextInt(RANDOM_LETTER.length())));
			}
			if (isBigLetter(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_SMALL_LETTER.charAt(rand.nextInt(RANDOM_SMALL_LETTER.length())));
			}
			else if (isSmallLetter(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_BIG_LETTER.charAt(rand.nextInt(RANDOM_BIG_LETTER.length())));
			}
			else
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_LETTER.charAt(rand.nextInt(RANDOM_LETTER.length())));
			}
		}
		else if (!spSecPasswordComplexityItemIdList.contains("1") && !spSecPasswordComplexityItemIdList.contains("2")
				&& spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_SPECIAL_CHAR.length())));
			}
		}
		else if (spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("3") && !spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 1; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_STR.charAt(rand.nextInt(RANDOM_STR.length())));
			}
			if (isLetter(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_NUM.charAt(rand.nextInt(RANDOM_NUM.length())));
			}
			else if (isNumeric(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_LETTER.charAt(rand.nextInt(RANDOM_LETTER.length())));
			}
			else
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_STR.charAt(rand.nextInt(RANDOM_STR.length())));
			}
		}
		else if (spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& spSecPasswordComplexityItemIdList.contains("3") && !spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 3; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_STR.charAt(rand.nextInt(RANDOM_STR.length())));
			}
			Random rand = new Random();
			password += String.valueOf(RANDOM_BIG_LETTER.charAt(rand.nextInt(RANDOM_BIG_LETTER.length())));
			password += String.valueOf(RANDOM_NUM.charAt(rand.nextInt(RANDOM_NUM.length())));
			password += String.valueOf(RANDOM_SMALL_LETTER.charAt(rand.nextInt(RANDOM_SMALL_LETTER.length())));
		}
		else if (spSecPasswordComplexityItemIdList.contains("1") && !spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("3") && spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 1; i++)
			{
				Random rand = new Random();
				password += String.valueOf(
						RANDOM_NUM_AND_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_NUM_AND_SPECIAL_CHAR.length())));
			}
			if (isNumeric(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_SPECIAL_CHAR.length())));
			}
			else if (isSpecialChar(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_NUM.charAt(rand.nextInt(RANDOM_NUM.length())));
			}
			else
			{
				Random rand = new Random();
				password += String.valueOf(
						RANDOM_NUM_AND_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_NUM_AND_SPECIAL_CHAR.length())));
			}
		}
		else if (!spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("3") && spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 1; i++)
			{
				Random rand = new Random();
				password += String.valueOf(
						RANDOM_LETTER_AND_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_LETTER_AND_SPECIAL_CHAR.length())));
			}
			if (isLetter(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_SPECIAL_CHAR.length())));
			}
			else if (isSpecialChar(password))
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_LETTER.charAt(rand.nextInt(RANDOM_LETTER.length())));
			}
			else
			{
				Random rand = new Random();
				password += String.valueOf(
						RANDOM_LETTER_AND_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_LETTER_AND_SPECIAL_CHAR.length())));
			}
		}
		else if (spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& !spSecPasswordComplexityItemIdList.contains("3") && spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 3; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_ALL.charAt(rand.nextInt(RANDOM_ALL.length())));
			}
			Random rand = new Random();
			password += String.valueOf(RANDOM_BIG_LETTER.charAt(rand.nextInt(RANDOM_BIG_LETTER.length())));
			password += String.valueOf(RANDOM_NUM.charAt(rand.nextInt(RANDOM_NUM.length())));
			password += String.valueOf(RANDOM_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_SPECIAL_CHAR.length())));
		}
		else if (!spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& spSecPasswordComplexityItemIdList.contains("3") && spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 3; i++)
			{
				Random rand = new Random();
				password += String.valueOf(
						RANDOM_LETTER_AND_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_LETTER_AND_SPECIAL_CHAR.length())));
			}
			Random rand = new Random();
			password += String.valueOf(RANDOM_BIG_LETTER.charAt(rand.nextInt(RANDOM_BIG_LETTER.length())));
			password += String.valueOf(RANDOM_SMALL_LETTER.charAt(rand.nextInt(RANDOM_SMALL_LETTER.length())));
			password += String.valueOf(RANDOM_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_SPECIAL_CHAR.length())));
		}
		else if (spSecPasswordComplexityItemIdList.contains("1") && spSecPasswordComplexityItemIdList.contains("2")
				&& spSecPasswordComplexityItemIdList.contains("3") && spSecPasswordComplexityItemIdList.contains("4"))
		{
			for (int i = 0; i < randomNum - 4; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_ALL.charAt(rand.nextInt(RANDOM_ALL.length())));
			}
			Random rand = new Random();
			password += String.valueOf(RANDOM_BIG_LETTER.charAt(rand.nextInt(RANDOM_BIG_LETTER.length())));
			password += String.valueOf(RANDOM_SMALL_LETTER.charAt(rand.nextInt(RANDOM_SMALL_LETTER.length())));
			password += String.valueOf(RANDOM_SPECIAL_CHAR.charAt(rand.nextInt(RANDOM_SPECIAL_CHAR.length())));
			password += String.valueOf(RANDOM_NUM.charAt(rand.nextInt(RANDOM_NUM.length())));
		}
		else
		{
			for (int i = 0; i < randomNum; i++)
			{
				Random rand = new Random();
				password += String.valueOf(RANDOM_STR.charAt(rand.nextInt(RANDOM_STR.length())));
			}
		}
		return password;
	}

	public static boolean isNumeric(String str)
	{
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isLetter(String str)
	{
		Pattern pattern = Pattern.compile("[[a-zA-Z]]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isBigLetter(String str)
	{
		Pattern pattern = Pattern.compile("[[A-Z]]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isSmallLetter(String str)
	{
		Pattern pattern = Pattern.compile("[[a-z]]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isSpecialChar(String str)
	{
		Pattern pattern = Pattern.compile("[[!@#$%^&()+*/|{}._=]]*");
		return pattern.matcher(str).matches();
	}
}
