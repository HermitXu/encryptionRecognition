package com.spinfosec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author ank
 * @version v 1.0
 * @title [API文档配置]
 * @ClassName: com.spinfosec.config.Swagger2Config
 * @description [API文档配置]
 * @create 2018/12/25 16:14
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@Configuration
public class Swagger2Config
{
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spinfosec.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("加密文件检查系统API文档")
                .description("加密文件检查系统API文档")
                .version("1.0")
                .build();
    }
}
