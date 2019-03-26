package com.spinfosec.config;

import com.spinfosec.intercept.AuthIntercept;
import com.spinfosec.intercept.LicenseValidIntercept;
import com.spinfosec.intercept.OperatorLogIntercept;
import com.spinfosec.intercept.ReplayAttackIntercept;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ank
 * @version v 1.0
 * @title [拦截器配置]
 * @ClassName: com.spinfosec.config.MainWebMvcConfig
 * @description [拦截器配置]
 * @create 2018/10/9 14:17
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@Configuration
public class MainWebMvcConfig implements WebMvcConfigurer
{

    @Bean
    public HandlerInterceptor getReplayAttackIntercept()
    {
        return new ReplayAttackIntercept();
    }

    @Bean
    public HandlerInterceptor getAuthIntercept()
    {
        return new AuthIntercept();
    }

    @Bean
    public HandlerInterceptor getOperatorLogIntercept()
    {
        return new OperatorLogIntercept();
    }

    @Bean
    public HandlerInterceptor getLicenseValidIntercept()
    {
        return new LicenseValidIntercept();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
		// 先进行会话超时校验
		// registry.addInterceptor(getAuthIntercept()).addPathPatterns("/**").excludePathPatterns();
		// 前端通过此isShowAuthCode接口来同步服务器时间戳，故此接口不列入重放攻击序列
		// registry.addInterceptor(getReplayAttackIntercept()).addPathPatterns("/**").excludePathPatterns();
		registry.addInterceptor(getLicenseValidIntercept()).addPathPatterns("/**").excludePathPatterns();
		registry.addInterceptor(getOperatorLogIntercept()).addPathPatterns("/**");
    }
}
