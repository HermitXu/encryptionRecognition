package com.spinfosec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.AdminDao;
import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.service.srv.ILicenseSrv;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.LicenseManagerHolder;
import com.spinfosec.utils.LicenseUtil;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName LicenseSrvImpl
 * @Description: 〈License许可管理业务处理实现类〉
 * @date 2018/11/8
 * All rights Reserved, Designed By SPINFO
 */
@Service("licenseSrv")
public class LicenseSrvImpl implements ILicenseSrv
{
	public static final long MONTH = 30 * 24 * 60 * 60 * 1000L;
	public static final long DAY = 24 * 60 * 60 * 1000L;

	private Logger log = LoggerFactory.getLogger(LicenseSrvImpl.class);

	@Autowired
	private AdminDao adminDao;

	@Override
	public JSONObject getCurrentLicenseInfo()
	{
		JSONObject licenseInfo = new JSONObject();
		try
		{
			LicenseManager licenseManager = LicenseManagerHolder.getInstance().getLicenseManager();
			LicenseContent content = licenseManager.verify();
			Date notAfter = content.getNotAfter();
			Date notBefore = content.getNotBefore();
			String extra = content.getExtra().toString();
			JSONObject jsonObject = JSON.parseObject(extra);
			String resLimitCount = null != jsonObject.get(Contants.RES_LIMIT_COUNT)
					? jsonObject.get(Contants.RES_LIMIT_COUNT).toString()
					: "";
			String subsystem = null != jsonObject.get(Contants.SUBSYSTEM)
					? jsonObject.get(Contants.SUBSYSTEM).toString()
					: "";
			String productName = null != jsonObject.get(Contants.PRODUCT_NAME)
					? jsonObject.get(Contants.PRODUCT_NAME).toString()
					: "";
			String model = null != jsonObject.get(Contants.MODEL) ? jsonObject.get(Contants.MODEL).toString() : "";
			String includeModule = null != jsonObject.get(Contants.INCLUDE_MODULE)
					? jsonObject.get(Contants.INCLUDE_MODULE).toString()
					: "";
			licenseInfo.put(Contants.NOT_BEFORE, DateUtil.dateToString(notBefore, DateUtil.DATE_FORMAT_PATTERN));
			licenseInfo.put(Contants.NOT_AFTER, DateUtil.dateToString(notAfter, DateUtil.DATE_FORMAT_PATTERN));
			licenseInfo.put(Contants.RES_LIMIT_COUNT, resLimitCount);
			licenseInfo.put(Contants.SUBSYSTEM, subsystem);
			licenseInfo.put(Contants.PRODUCT_NAME, productName);
			licenseInfo.put(Contants.MODEL, model);
			licenseInfo.put(Contants.INCLUDE_MODULE, includeModule);

		}
		catch (Exception e)
		{
			log.error("当前License许可获取失败！", e);
		}
		return licenseInfo;
	}

	@Override
	public CodeRsp installAndVerifyLicense()
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		/************** 证书使用者端执行 ******************/
		LicenseManager licenseManager = LicenseManagerHolder.getInstance().getLicenseManager();
		// 安装证书
		try
		{
			File licDir = new File(LicenseManagerHolder.getInstance().getLicPath());
			if (licDir.exists())
			{
				File[] licFileArr = licDir.listFiles();
				if (null != licFileArr && licFileArr.length != 0)
				{
					File licFile = licFileArr[0];
					if (null != licFile && licFile.isFile())
					{
						// 安装前先校验待安装的license是否有效合法
						LicenseContent licenseContent = licenseManager.verify(FileUtils.readFileToByteArray(licFile));
						codeRsp = isLicenseValid(licenseContent);
						if (codeRsp.getCode().equalsIgnoreCase(RspCode.SUCCESS.getCode()))
						{
							licenseManager.install(licFile);
							log.debug("install license ok!");
						}
					}
					else
					{
						log.debug("license not exist!");
						codeRsp = new CodeRsp(RspCode.LICENSE_NOT_EXIST);
						return codeRsp;
					}
				}
				else
				{
					log.debug("license not exist!");
					codeRsp = new CodeRsp(RspCode.LICENSE_NOT_EXIST);
					return codeRsp;
				}
			}
		}
		catch (TMCException e)
		{
			log.error("获取机器码失败！", e);
			codeRsp = new CodeRsp(RspCode.LICENSE_CANOTGET_MACHINE);
			return codeRsp;
		}
		catch (IOException e)
		{
			log.error("读取许可文失败！", e);
			codeRsp = new CodeRsp(RspCode.LICENSE_NOT_EXIST);
			return codeRsp;
		}
		catch (LicenseContentException e)
		{
			log.error("许可已过期！", e);
			codeRsp = new CodeRsp(RspCode.LICENSE_OUT_OF_DATE);
			return codeRsp;
		}
		catch (Exception e)
		{
			log.error("证书安装失败！", e);
			codeRsp = new CodeRsp(RspCode.LICENSE_INSTALL_FAIL);
			return codeRsp;
		}

		return codeRsp;
	}

	private CodeRsp isLicenseValid(LicenseContent content)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		if (null != MemInfo.getServletContext())
		{
			// 验证前先清除
			MemInfo.getServletContext().removeAttribute("licenseLive");
		}

		String extra = content.getExtra().toString();
		JSONObject jsonObject = JSON.parseObject(extra);
		String mcode = jsonObject.get(Contants.MCODE).toString();
		String subsystem = jsonObject.get(Contants.SUBSYSTEM).toString();
		String resLimitCount = jsonObject.get(Contants.RES_LIMIT_COUNT).toString();
        String productName = jsonObject.getString(Contants.PRODUCT_NAME);

        if (StringUtils.isEmpty(subsystem) || StringUtils.isEmpty(resLimitCount))
		{
			log.debug("verify license fail, the license is not completed!");
			codeRsp = new CodeRsp(RspCode.LICENSE_VERIFY_FAIL);
			return codeRsp;
		}
        if (!productName.equalsIgnoreCase(Contants.PRODUCT_NAME_SIMP_CRS))
        {
            log.debug("verify license fail, the license is not belong to SIMP-CRS!");
            codeRsp = new CodeRsp(RspCode.LICENSE_VERIFY_FAIL);
            return codeRsp;
        }
		if (!mcode.equals(LicenseUtil.getLocalMcode()))
		{
			log.debug("verify license fail, the license is not belong to this machine!");
			codeRsp = new CodeRsp(RspCode.LICENSE_NOT_MATCH_MACHINE);
		}
        else
        {
            Date notAfter = content.getNotAfter();
            Date notBefore = content.getNotBefore();
            if (notAfter.getTime() - System.currentTimeMillis() > 0
                    && System.currentTimeMillis() - notBefore.getTime() > 0)
            {
                if (null != MemInfo.getServletContext())
                {
                        long day = (notAfter.getTime() - System.currentTimeMillis()) / DAY;
                        MemInfo.getServletContext().setAttribute("licenseLive", day + 1);
                }
                log.debug("verify license ok!");

            }
            else
            {
                log.debug("license out of date!");
                codeRsp = new CodeRsp(RspCode.LICENSE_OUT_OF_DATE);
            }

        }
		return codeRsp;
	}

	/**
	 * 校验许可
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean checkLicense() throws Exception
	{
		boolean b = isModuleAndAuthOk();
		if (!b)
		{
			return false;
		}
		CodeRsp codeRsp = isLicenseValid(LicenseManagerHolder.getInstance().getLicenseManager().verify());
		if (RspCode.SUCCESS.getCode().equals(codeRsp.getCode()))
		{
			return true;
		}
		return false;
	}

	/**
	 * 查询是否存在权限
	 * @return
	 */
	private boolean isModuleAndAuthOk()
	{
		List<SpCodeDecodes> allCodeDecodes = adminDao.getAllCodeDecodes();
		return !allCodeDecodes.isEmpty();
	}
}
