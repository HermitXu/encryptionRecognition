package com.spinfosec.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import de.schlichtherle.license.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName LicenseManagerHolder
 * @Description: 〈License处理〉
 * @date 2018/11/8
 * All rights Reserved, Designed By SPINFO
 */
public class LicenseManagerHolder
{
	private String PUBLICALIAS;
	private String STOREPWD;
	private String SUBJECT;
	private String licPath;
	private String pubPath;

	private LicenseManager licenseManager;

	private static LicenseManagerHolder licenseManagerHolder = null;

	private static Logger log = LoggerFactory.getLogger(LicenseManagerHolder.class);

	public static synchronized LicenseManagerHolder getInstance()
	{
		if (null == licenseManagerHolder)
		{
			licenseManagerHolder = new LicenseManagerHolder();
		}
		return licenseManagerHolder;
	}

	private LicenseManagerHolder()
	{
		initParam();
		licenseManager = new LicenseManager(initLicenseParams());
	}

	public LicenseManager getLicenseManager()
	{
		return this.licenseManager;
	}

	public String getLicPath()
	{
		return licPath;
	}

	/**
	 * 初始化参数
	 */
	private void initParam()
	{
		// 获取参数
		Properties prop = new Properties();
		String paramPath = getResourcesPath() + File.separator + "license.properties";
		log.info("paramPath = " + paramPath);
		try
		{
			FileReader reader = new FileReader(paramPath);
			prop.load(reader);
		}
		catch (IOException e)
		{
			log.error("加载param.properties文件失败！", e);
		}
		PUBLICALIAS = prop.getProperty("PUBLICALIAS");
		STOREPWD = prop.getProperty("STOREPWD");
		SUBJECT = prop.getProperty("SUBJECT");
		licPath = getResourcesPath() + File.separator + "document" + File.separator + "license";
		pubPath = prop.getProperty("pubPath");

	}

	// 返回验证证书需要的参数
	private LicenseParam initLicenseParams()
	{
		String os = System.getProperty("os.name");
		Preferences preference = null;
		if (os.equalsIgnoreCase("Linux"))
		{
			preference = Preferences.systemNodeForPackage(LicenseManagerHolder.class);
		}
		else
		{
			preference = Preferences.userNodeForPackage(LicenseManagerHolder.class);
		}
		CipherParam cipherParam = new DefaultCipherParam(STOREPWD);
		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(LicenseManagerHolder.class, pubPath, PUBLICALIAS,
				STOREPWD, null);
		LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT, preference, privateStoreParam, cipherParam);
		return licenseParams;
	}

	/**
	 * 获取license中的属性
	 * @param type
	 *          mcode   resLimitCount   subsystem
	 * @return String value 根据传入的type返回不同的值
	 */
	public String getLicenseExtraProperty(String type)
	{
		String value = null;
		try
		{
			LicenseContent licenseContent = this.licenseManager.verify();
			String extra = licenseContent.getExtra().toString();
			JSONObject jsonObject = JSON.parseObject(extra);
			if (null != jsonObject.get(type))
			{
				value = jsonObject.get(type).toString();
			}
		}
		catch (Exception e)
		{
			log.error("License验证失败", e);
		}
		return value;
	}

	public String getResourcesPath()
	{
		ClassLoader classLoader = LicenseManagerHolder.class.getClassLoader();
		URL resources = classLoader.getResource("");
        String path = null;
        try
        {
			path = URLDecoder.decode(resources.getPath(), "utf-8").replaceFirst("/", "");
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("编码转换异常：", e);
        }
        catch (Exception e)
        {
            log.error("路径获取异常：", e);
        }
        return path;
	}

	public static void main(String[] args)
	{
	}
}
