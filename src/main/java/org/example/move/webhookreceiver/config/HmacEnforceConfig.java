package org.example.move.webhookreceiver.config;

import org.example.move.webhookreceiver.rest.hmac.HmacEnforceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HmacEnforceConfig implements WebMvcConfigurer {

    @Autowired
    HmacEnforceInterceptor interceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
