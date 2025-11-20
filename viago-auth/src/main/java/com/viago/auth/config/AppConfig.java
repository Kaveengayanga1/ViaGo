package com.viago.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    //@LoadBalanced //Spring cloud+Eureka
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
