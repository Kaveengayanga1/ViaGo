package com.viago.auth.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Feign clients.
 * Currently minimal, but can be extended to add request interceptors
 * (e.g., for forwarding authentication tokens) if needed in the future.
 */
@Configuration
public class FeignClientConfig {

    /**
     * Optional request interceptor for adding headers to Feign requests.
     * Currently not adding any headers, but can be extended for future use.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Future: Add authentication headers or other interceptors if needed
            // For now, notification service calls don't require authentication
        };
    }
}
