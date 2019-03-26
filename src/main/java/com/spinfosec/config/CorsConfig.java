package com.spinfosec.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ank
 * @version v 1.0
 * @title [跨域配置]
 * @ClassName: com.spinfosec.config.CorsConfig
 * @description [跨域配置]
 * @create 2018/10/15 11:52
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@Configuration
public class CorsConfig
{

    @Bean
    public FilterRegistrationBean corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        List<String> headers = new ArrayList<String>();
        headers.add("access-control-allow-headers");
        headers.add("noncetimestamp");
        headers.add("appId");
        headers.add("authorization");
//        headers.add("access-control-allow-methods");
//        headers.add("access-control-allow-origin");
//        headers.add("access-control-max-age");
//        headers.add("X-Frame-Options");
        headers.add("Date");
        config.setExposedHeaders(headers);

        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0); // filter排序
        return bean;

    }
}
