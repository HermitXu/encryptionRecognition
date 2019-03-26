package com.spinfosec.controller.common;

import com.spinfosec.dto.pojo.common.EncryptDecryptBean;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.utils.whiteBoxTest.AESAndCamelliaUtil;
import com.spinfosec.utils.whiteBoxTest.DESUtil;
import com.spinfosec.utils.whiteBoxTest.DESedeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName EncryptDecryptController
 * @Description: 〈加密解密控制层〉
 * @date 2018/11/14
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/system/encryptDecrypt")
public class EnDecryptController
{
	private static final Logger log = LoggerFactory.getLogger(EnDecryptController.class);

	@RequestMapping(value = "/encryptDecryptAnalysis", method = RequestMethod.POST)
	public ResponseBean decrypt(HttpServletRequest req, HttpServletResponse rsp, @RequestBody EncryptDecryptBean bean)
    {
        ResponseBean responseBean = null;
        // 得到加密算法
        String algorithm = bean.getAlgorithm();
        log.info("加密算法：" + algorithm);
        if (StringUtils.isNotEmpty(algorithm))
        {
            if ("AES".equalsIgnoreCase(algorithm) || "camellia".equalsIgnoreCase(algorithm))
            {
                responseBean = AESAndCamelliaUtil.encryptOrDecrypt(bean);
            }
            else if ("DES".equalsIgnoreCase(algorithm))
            {
                responseBean = DESUtil.encryptOrDecrypt(bean);
            }
            else if ("DesEde".equalsIgnoreCase(algorithm))
            {
                responseBean = DESedeUtil.encryptOrDecrypt(bean);
            }
        }

        return responseBean;
    }
}
