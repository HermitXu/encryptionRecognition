package com.spinfosec.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ank
 * @version v 1.0
 * @title [HTTP上8080端口的请求跳转到8443端口下的HTTPS]
 * @ClassName: SSLConfig
 * @description [HTTP上8080端口的请求跳转到8443端口下的HTTPS]
 * @create 2018/9/12 14:36
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@Configuration
public class SSLConfig
{
    @Bean
    public TomcatServletWebServerFactory servletContainer()
    {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory()
        {

            @Override
            protected void postProcessContext(Context context)
            {

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector()
    {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
}
