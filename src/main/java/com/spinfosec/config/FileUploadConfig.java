package com.spinfosec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName FileUploadConfig
 * @Description: 〈配置文件上传大小〉
 * @date 2018/12/25
 * All rights Reserved, Designed By SPINFO
 */

@Configuration
public class FileUploadConfig
{
	/**
	 * 文件上传配置
	 *
	 * @return MultipartConfigElement
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement(@Value("${multipart.maxFileSize}") String maxFileSize,
			@Value("${multipart.maxRequestSize}") String maxRequestSize)
	{
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(maxFileSize);
		// 设置总上传数据总大小
		factory.setMaxRequestSize(maxRequestSize);
		return factory.createMultipartConfig();
	}
}
