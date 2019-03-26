package com.spinfosec.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * @title IpUtils
 * @description <IP工具类>
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author DUSH
 * @version V1.5.0.1
 * @create 2015年5月29日 下午8:00:28   
 */
public class IpUtils
{

	/**
	  * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request)
	{
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
		{
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
		{
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1)
			{
				return ip.substring(0, index);
			}
			else
			{
				return ip;
			}
		}
		else
		{
			return request.getRemoteAddr();
		}
	}

	public static long ipToLong(String strIP)
	{
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIP.indexOf(".");
		int position2 = strIP.indexOf(".", position1 + 1);
		int position3 = strIP.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIP.substring(0, position1));
		ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIP.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	public static String longToBinaryString(long ipLong)
	{
		String s = Long.toBinaryString(ipLong);
		if (s.length() < 32)
		{
			for (int i = 0; i < 32 - s.length(); i++)
			{
				s = "0" + s;
			}
		}
		return s;
	}

	public static String getIpFromEth0File()
	{
		String ip = "";
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") != -1)
		{
			ip = getIpForWin();
		}
		else
		{
			String path = "/etc/sysconfig/network-scripts/ifcfg-eth0";
			File file = new File(path);
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null)
				{
					if (line.toLowerCase().startsWith("IPADDR".toLowerCase()))
					{
						Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
						Matcher matcher = pattern.matcher(line);
						boolean b = matcher.find();
						if (b)
						{
							ip = matcher.group();
							break;
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return ip;
	}

	public static String getIpForWin()
	{
		String ip = "";
		try
		{
			InetAddress localHost = InetAddress.getLocalHost();
			ip = localHost.getHostAddress();

		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 获取服务器IP地址
	 * @return
	 */
	public static String getLocalIp()
	{
		try
		{
			return getLocalHostLANAddress().getHostAddress().toString();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	private static InetAddress getLocalHostLANAddress() throws UnknownHostException
	{
		try
		{
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements())
			{
				NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
				Enumeration<InetAddress> nias = ni.getInetAddresses();
				while (nias.hasMoreElements())
				{
					InetAddress ia = (InetAddress) nias.nextElement();
					if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address)
					{
						return ia;
					}
				}
			}
		}
		catch (SocketException e)
		{
		}
		return null;
	}

}
