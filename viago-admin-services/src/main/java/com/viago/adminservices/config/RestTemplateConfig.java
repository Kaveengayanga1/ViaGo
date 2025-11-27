package com.viago.adminservices.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final AuthServiceProperties properties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            if (properties.getAdminToken() != null && !properties.getAdminToken().isBlank()) {
                request.getHeaders().setBearerAuth(properties.getAdminToken());
            }
            return execution.execute(request, body);
        };

        return builder
                .additionalInterceptors(authInterceptor)
                .build();
    }
}

