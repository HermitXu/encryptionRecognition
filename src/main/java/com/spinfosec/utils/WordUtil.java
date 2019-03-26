package com.spinfosec.utils;

import com.aspose.words.License;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName WordUtil
 * @Description: 〈Word导出工具类〉
 * @date 2018/11/14
 * All rights Reserved, Designed By SPINFO
 */
public class WordUtil
{
	/**
	 * 破解去除水印 Evaluation Only. Created with Aspose.Words. Copyright 2003-2014 Aspose Pty Ltd.
	 * @return
	 */
	public static boolean getLicense()
	{
        boolean result = false;
        try
		{
            ClassLoader classLoader = WordUtil.class.getClassLoader();
            URL url = classLoader.getResource("");
            String resources = URLDecoder.decode(url.getPath(), "utf-8").replaceFirst("/", "");
            String filePath = resources + File.separatorChar + "asposeLicense.xml";

			InputStream is = new FileInputStream(filePath);
			License aposeLic = new License();
			aposeLic.setLicense(is);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
