package com.viago.adminservices.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-service")
public class AuthServiceProperties {
    /**
     * Base URL of the viago-auth service (e.g. http://localhost:8080).
     */
    private String baseUrl;

    /**
     * Optional admin token shared between services for privileged calls.
     */
    private String adminToken;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }
}

