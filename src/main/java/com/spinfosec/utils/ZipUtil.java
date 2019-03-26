package com.spinfosec.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * @title [zip压缩解压工具]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-6-16 下午7:26:31
 */
public final class ZipUtil
{

	/**
	 * 
	 * @param src
	 * @param dest
	 * @throws Exception
	 */
	public static void zip(String src, String dest) throws Exception
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dest));
		File srcFile = new File(src);
		zip(out, srcFile, "");
		out.close();
	}

	/**
	 * 
	 * @param src
	 * @param dest
	 * @throws Exception
	 */
	public static void zipBackData(String src, String dest) throws Exception
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dest));
		File srcFile = new File(src);
		zip(out, srcFile, "backupData");
		out.close();
	}

	//
	// * @param out： ZipOutputStream
	// * @param srcFile： 要压缩的目录
	// * @param base： 根路径
	// * @throws Exception
	//
	public static void zip(ZipOutputStream out, File srcFile, String base) throws Exception
	{
		if (!srcFile.exists())
		{
			throw new Exception("the dir is not exist." + "dir=" + srcFile);
		}
		if (srcFile.isDirectory())
		{
			File[] files = srcFile.listFiles();
			base = base.length() == 0 ? "" : base + "/";
			if (base.length() > 0)
			{
				out.putNextEntry(new ZipEntry(base));
			}
			for (int i = 0; i < files.length; i++)
			{
				zip(out, files[i], base + files[i].getName());
			}
		}
		else
		{
			base = base.length() == 0 ? srcFile.getName() : base;
			out.putNextEntry(new ZipEntry(base));
			FileInputStream fis = new FileInputStream(srcFile);
			int length = 0;
			byte[] b = new byte[1024];
			while ((length = fis.read(b, 0, 1024)) != -1)
			{
				out.write(b, 0, length);
			}
			fis.close();
		}
	}

	/**
	 * 
	 * @param base
	 * @param list
	 * @return
	 */
	public boolean isExist(String base, List<String> list)
	{
		if (list != null && !list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (base.indexOf((String) list.get(i)) >= 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 解压
	 * 
	 * @param srcFile
	 * @param dest
	 * @param deleteFile
	 * @throws Exception
	 */
	public static List<File> unZip(String srcFile, String dest, boolean deleteFile) throws Exception
	{

		List<File> result = new ArrayList<File>();

		File file = new File(srcFile);
		if (!file.exists())
		{
			throw new Exception("the dir is not exist." + "dir=" + srcFile);
		}
		ZipFile zipFile = new ZipFile(file);
		Enumeration<?> e = zipFile.getEntries();
		while (e.hasMoreElements())
		{
			ZipEntry zipEntry = (ZipEntry) e.nextElement();
			if (zipEntry.isDirectory())
			{
				String name = zipEntry.getName();
				name = name.substring(0, name.length() - 1);
				File f = new File(dest + name);
				f.mkdirs();
			}
			else
			{
				File f = new File(dest + zipEntry.getName());
				f.getParentFile().mkdirs();
				f.createNewFile();
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(f);
				int length = 0;
				byte[] b = new byte[1024];
				while ((length = is.read(b, 0, 1024)) != -1)
				{
					fos.write(b, 0, length);
				}
				is.close();
				fos.close();
				result.add(f);
			}
		}

		if (zipFile != null)
		{
			zipFile.close();
		}

		if (deleteFile)
		{
			// file.deleteOnExit();
			file.delete();
		}

		return result;
	}

	/**
	 * 压缩整个文件夹中的所有文件，生成指定名称的zip压缩包
	 * @param filepath 文件所在目录
	 * @param zippath 压缩后zip文件名称
	 * @param dirFlag zip文件中第一层是否包含一级目录，true包含；false没有
	 */
	public static void zipMultiFile(String filepath, String zippath, boolean dirFlag)
	{
		try
		{
			File file = new File(filepath);// 要被压缩的文件夹
			File zipFile = new File(zippath);
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOut.setEncoding("utf-8");
			if (file.isDirectory())
			{
				File[] files = file.listFiles();
				for (File fileSec : files)
				{
					if (dirFlag)
					{
						recursionZip(zipOut, fileSec, file.getName() + File.separator);
					}
					else
					{
						recursionZip(zipOut, fileSec, "");
					}
				}
			}
			zipOut.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception
	{
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			for (File fileSec : files)
			{
				recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
			}
		}
		else
		{
			byte[] buf = new byte[1024];
			InputStream input = new FileInputStream(file);
			ZipEntry ze = new ZipEntry(baseDir + file.getName());
			ze.setUnixMode(755);
			zipOut.putNextEntry(ze);
			int len;
			while ((len = input.read(buf)) != -1)
			{
				zipOut.write(buf, 0, len);
			}
			input.close();
		}
	}

	/**
	 * 压缩指定文件到当前文件夹,解码格式GBK
	 *
	 * @param src 要压缩的指定文件
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipGBK(String src)
	{
		return zipGBK(src, null);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到当前目录，解码格式GBK
	 *
	 * @param src    要压缩的文件
	 * @param passwd 压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipGBK(String src, String passwd)
	{
		return zipGBK(src, null, passwd);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到当前目录，解码格式GBK
	 *
	 * @param src    要压缩的文件
	 * @param dest   压缩文件存放路径
	 * @param passwd 压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipGBK(String src, String dest, String passwd)
	{
		return zipGBK(src, dest, true, passwd);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到指定位置.解码格式GBK
	 * <p/>
	 * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
	 * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
	 * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
	 *
	 * @param src         要压缩的文件或文件夹路径
	 * @param dest        压缩文件存放路径
	 * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.<br />
	 *                    如果为false,将直接压缩目录下文件到压缩文件.
	 * @param passwd      压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipGBK(String src, String dest, boolean isCreateDir, String passwd)
	{
		File srcFile = new File(src);
		dest = buildDestinationZipFilePath(srcFile, dest);
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
		if (!StringUtils.isEmpty(passwd))
		{
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
			parameters.setPassword(passwd.toCharArray());
		}
		try
		{
			net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(dest);
			zipFile.setFileNameCharset("GBK");
			if (srcFile.isDirectory())
			{
				// 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
				if (!isCreateDir)
				{
					File[] subFiles = srcFile.listFiles();
					ArrayList<File> temp = new ArrayList<File>();
					Collections.addAll(temp, subFiles);
					zipFile.addFiles(temp, parameters);
					return dest;
				}
				zipFile.addFolder(srcFile, parameters);
			}
			else
			{
				zipFile.addFile(srcFile, parameters);
			}
			return dest;
		}
		catch (ZipException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建压缩文件存放路径,如果不存在将会创建
	 * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
	 *
	 * @param srcFile   源文件
	 * @param destParam 压缩目标路径
	 * @return 正确的压缩文件存放路径
	 */
	public static String buildDestinationZipFilePath(File srcFile, String destParam)
	{
		if (StringUtils.isEmpty(destParam))
		{
			if (srcFile.isDirectory())
			{
				destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
			}
			else
			{
				String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
				destParam = srcFile.getParent() + File.separator + fileName + ".zip";
			}
		}
		else
		{
			createDestDirectoryIfNecessary(destParam); // 在指定路径不存在的情况下将其创建出来
			if (destParam.endsWith(File.separator))
			{
				String fileName = "";
				if (srcFile.isDirectory())
				{
					fileName = srcFile.getName();
				}
				else
				{
					fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
				}
				destParam += fileName + ".zip";
			}
		}
		return destParam;
	}

	/**
	 * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
	 *
	 * @param destParam 指定的存放路径,有可能该路径并没有被创建
	 */
	private static void createDestDirectoryIfNecessary(String destParam)
	{
		File destDir = null;
		if (destParam.endsWith(File.separator))
		{
			destDir = new File(destParam);
		}
		else
		{
			destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));
		}
		if (!destDir.exists())
		{
			destDir.mkdirs();
		}
	}

	/**
	 * 压缩指定文件到当前文件夹,解码格式u8
	 *
	 * @param src 要压缩的指定文件
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipU8(String src)
	{
		return zipU8(src, null);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到当前目录，解码格式u8
	 *
	 * @param src    要压缩的文件
	 * @param passwd 压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipU8(String src, String passwd)
	{
		return zipU8(src, null, passwd);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到当前目录，解码格式u8
	 *
	 * @param src    要压缩的文件
	 * @param dest   压缩文件存放路径
	 * @param passwd 压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipU8(String src, String dest, String passwd)
	{
		return zipU8(src, dest, true, passwd);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到指定位置.解码格式u8
	 * <p/>
	 * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
	 * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
	 * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
	 *
	 * @param src         要压缩的文件或文件夹路径
	 * @param dest        压缩文件存放路径
	 * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.<br />
	 *                    如果为false,将直接压缩目录下文件到压缩文件.
	 * @param passwd      压缩使用的密码
	 * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
	 */
	public static String zipU8(String src, String dest, boolean isCreateDir, String passwd)
	{
		File srcFile = new File(src);
		dest = buildDestinationZipFilePath(srcFile, dest);
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
		if (!StringUtils.isEmpty(passwd))
		{
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
			parameters.setPassword(passwd.toCharArray());
		}
		try
		{
			net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(dest);
			zipFile.setFileNameCharset("utf-8");
			if (srcFile.isDirectory())
			{
				// 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
				if (!isCreateDir)
				{
					File[] subFiles = srcFile.listFiles();
					ArrayList<File> temp = new ArrayList<File>();
					Collections.addAll(temp, subFiles);
					zipFile.addFiles(temp, parameters);
					return dest;
				}
				zipFile.addFolder(srcFile, parameters);
			}
			else
			{
				zipFile.addFile(srcFile, parameters);
			}
			return dest;
		}
		catch (ZipException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
