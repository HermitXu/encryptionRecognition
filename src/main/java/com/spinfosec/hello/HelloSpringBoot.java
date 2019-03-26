package com.spinfosec.hello;

import com.spinfosec.utils.Utils;
import com.spinfosec.utils.sm2.SMKeyPair;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author Administrator
 * @version v 1.0
 * @title [标题]
 * @ClassName: HelloSpringBoot
 * @description [一句话描述]
 * @create 2018/9/4 14:23
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@RestController
public class HelloSpringBoot
{
    Logger logger = LoggerFactory.getLogger(HelloSpringBoot.class);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(path = {"/sayHello"})
    public String sayHello(HttpServletRequest request)
    {
        /*String who = request.getParameter("who");
        request.getSession().setAttribute("who", who);
        logger.info("who = " + request.getSession().getAttribute("who"));
        logger.info(String.valueOf(request.getSession().getMaxInactiveInterval()));
        System.out.println("hello SpringBoot!");

        redisTemplate.opsForValue().set("aaa", "aaa");

        String aaa = (String) redisTemplate.opsForValue().get("aaa");
        System.out.println("aaa = " + aaa);

        return "test hello springboot!";*/

        // 生成密钥对
        SMKeyPair smKeyPair = Sm2Utils.generateKeyPair();

        String plainText = "你好www.shipinginfo.com世界！";
        byte[] sourceData = plainText.getBytes();

        // 下面的密钥可以使用generateKeyPair()生成的密钥内容
        // 国密规范正式私钥
        //String privateKey = "3690655E33D5EA3D9A4AE1A1ADD766FDEA045CDEAA43A9206FB8C430CEFE0D94";
        String privateKey = "630141d00aff363e7c42fdd00ede52a97802575e3cdd194ac0dbec07a9981b7b";
        // 国密规范正式公钥
//        String publicKey = "04F6E0C3345AE42B51E06BF50B98834988D54EBC7460FE135A48171BC0629EAE205EEDE253A530608178A98F1E19BB737302813BA39ED3FA3C51639D7A20C7391A";
        String publicKey = "04fc06033b32cfdd0ebf3582c00feee6ad7982acc95d356c88e27d3fe4da55a11b2339840976b38813f1236bf00f11fc376a08aea56e7ed38b44d661eae44912e1";

        //String publicKey = smKeyPair.getPublicKey();
        //String privateKey = smKeyPair.getPrivateKey();

        System.out.println("publics : " + Arrays.toString(Utils.hexToByte(publicKey)));
        System.out.println("privates : " + Arrays.toString(Utils.hexToByte(privateKey)));

        System.out.println("加密（返回十六进制字符串）: ");
        String cipherText = Sm2Utils.encryptString(publicKey, sourceData);
        System.out.println(cipherText);
        System.out.println();

//        System.out.println("加密（返回字节数组）：");
//        System.out.println(Arrays.toString(Sm2Utils.encrypt(publicKey, sourceData)));
//        System.out.println();
//
//        System.out.println("解密: ");
//        plainText = new String(Sm2Utils.decrypt(privateKey, cipherText));
//        System.out.println(plainText);
        return cipherText;
    }
}
