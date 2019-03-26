package com.spinfosec;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@EnableEncryptableProperties
@EnableSwagger2
@MapperScan("com.spinfosec.dao")
public class Application extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return builder.sources(Application.class);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

}
