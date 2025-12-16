package com.viago.adminservices.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                // 2. Retrieve the "Authorization" header (Bearer eyJhb...)
                String authHeader = attributes.getRequest().getHeader("Authorization");

                // 3. If present, forward it to the Feign call
                if (authHeader != null) {
                    requestTemplate.header("Authorization", authHeader);
                }
            }
        };
    }

}
