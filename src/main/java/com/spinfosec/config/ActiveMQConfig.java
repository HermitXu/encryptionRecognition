package com.spinfosec.config;

import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.utils.AESPython;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName JmsConfiguration
 * @Description: 〈ActiveMQ配置类〉
 * @date 2018/11/12
 * All rights Reserved, Designed By SPINFO
 */

@Configuration
public class ActiveMQConfig
{

	@Autowired
	ApplicationProperty property;

	@Bean
	public ActiveMQSslConnectionFactory connectionFactory() throws Exception
	{
		ClassLoader classLoader = ActiveMQConfig.class.getClassLoader();
		URL resources = classLoader.getResource("");
		String path = URLDecoder.decode(resources.getPath(), "utf-8").replaceFirst("/", "");

		ActiveMQSslConnectionFactory activeMQSslConnectionFactory = new ActiveMQSslConnectionFactory();

		activeMQSslConnectionFactory.setBrokerURL(property.getActivemqBrokerUrl());

		String mqUser = property.getActivemqUser();
		activeMQSslConnectionFactory.setUserName(mqUser);

		String mqpd = property.getActivemqPassword();
		activeMQSslConnectionFactory.setPassword(mqpd);
		activeMQSslConnectionFactory.setKeyStore("file:" + path + File.separatorChar + "client.ks");

		String mqKeyPd = property.getActivemqSslKeyPassword();
		activeMQSslConnectionFactory.setKeyStorePassword(mqKeyPd);
		activeMQSslConnectionFactory.setTrustStore("file:" + path + File.separatorChar + "client.ts");

		String mqTrustPd = property.getActivemqSslTrustPassword();
		activeMQSslConnectionFactory.setTrustStorePassword(mqTrustPd);

		activeMQSslConnectionFactory.setSendTimeout(120000);
		activeMQSslConnectionFactory.setCloseTimeout(120000);
		return activeMQSslConnectionFactory;
	}

}
