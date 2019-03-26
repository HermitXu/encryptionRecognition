package com.spinfosec.utils;

import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName LicenseUtil
 * @Description: 〈License工具〉
 * @date 2018/11/8
 * All rights Reserved, Designed By SPINFO
 */
public class LicenseUtil
{
	/**
	 * 根据sha256串生成License
	 *
	 * @param shaStr
	 * @return
	 */
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

	/**
	 * 根据机器码和到期时间生成序列号
	 *
	 * @param mcode
	 * @param expDate
	 * @return
	 * @throws Exception
	 */
	public static String genLicense(String mcode, long expDate) throws Exception
	{

		String srcCode = mcode + expDate;
		// 加密字符串
		String encStr = AESPython.Encrypt(srcCode, AESPython.SKEY);
		String encSha = EncUtil.toSha256(encStr);
		String expStr = AESPython.Encrypt(String.valueOf(expDate), AESPython.SKEY);
		String license = (encSha + "-" + expStr).toUpperCase();
		return license;
	}

	/**
	 * 获取本机所有物理地址
	 *
	 * @return
	 */
	public static Set<String> getAllLocalMac()
	{
		Set<String> macSet = new HashSet<String>();
		Enumeration<NetworkInterface> netInterfaces;
		try
		{
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements())
			{
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				byte[] mac = ni.getHardwareAddress();
				StringBuilder sb = new StringBuilder();
				if (mac != null)
				{
					for (byte b : mac)
					{
						sb.append(toHex(b));
						// sb.append("-");
					}

					if (sb.length() > 1)
					{
						sb.deleteCharAt(sb.length() - 1);
					}

					if (null != sb.toString() && !sb.toString().isEmpty() && 11 == sb.toString().length())
					{
						macSet.add(sb.toString());
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}

		return macSet;
	}

	public static Set<String> getEth0Mac()
	{
		Set<String> macSet = new HashSet<String>();
		try
		{
			byte[] mac = null;
			String cmd = "ifconfig eth0";
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			InputStream inputStream = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder matcherSb = new StringBuilder();
			while ((line = br.readLine()) != null)
			{
				String reg = "([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}";
				Pattern pattern = Pattern.compile(reg);
				Matcher matcher = pattern.matcher(line);
				if (matcher.find())
				{
					String group = matcher.group();
					matcherSb.append(group);
					break;
				}
			}
			mac = matcherSb.toString().getBytes();
			if (null != mac)
			{
				StringBuilder sb = new StringBuilder();
				for (byte b : mac)
				{
					sb.append(toHex(b));
					// sb.append("-");
				}

				if (sb.length() > 1)
				{
					sb.deleteCharAt(sb.length() - 1);
				}

				if (null != sb.toString() && !sb.toString().isEmpty())
				{
					macSet.add(sb.toString());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return macSet;
	}

	/**
	 * 获取本机机器码
	 *
	 * @return
	 * @throws Exception
	 */
	public static String getLocalMcode() throws TMCException
	{
		StringBuffer sb = new StringBuffer();

		Set<String> macSet = null;
		String os = System.getProperty("os.name");
		if (os.equalsIgnoreCase("Linux"))
		{
			macSet = getEth0Mac();
		}
		else
		{
			macSet = getAllLocalMac();
		}

		if (null != macSet && macSet.size() > 0)
		{
			for (String macAdd : macSet)
			{
				sb.append(macAdd);
			}
		}

		String shaStr = EncUtil.toSha256(sb.toString());

		if (null != shaStr && !shaStr.isEmpty())
		{
			return shaStr;
		}
		else
		{
			throw new TMCException(RspCode.LICENSE_CANOTGET_MACHINE);
		}
	}

	public static StringBuffer toHex(byte b)
	{
		byte factor = 16;
		int v = b & 0xff;// 去掉byte转换之后的负数部分。
		byte high = (byte) (v / factor);
		byte low = (byte) (v % factor);
		StringBuffer buf = new StringBuffer();
		buf.append(toHexLow(high)).append(toHexLow(low));
		return buf;
	}

	private static char toHexLow(byte b)
	{
		if (b > 16 || b < 0)
		{
			throw new IllegalArgumentException("inpt parameter should less than 16 and greater than 0");
		}

		if (b < 10)
		{
			return (char) ('0' + (char) b);
		}
		else
		{
			return (char) ('A' + (b - 10));
		}
	}

}
