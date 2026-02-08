package com.viago.auth.client;

import com.viago.auth.config.FeignClientConfig;
import com.viago.auth.dto.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for communicating with viago-notification-service via Eureka service discovery.
 */
@FeignClient(name = "viago-notification-service", configuration = FeignClientConfig.class)
public interface NotificationServiceClient {

    /**
     * Sends a notification email using the notification service.
     *
     * @param request NotificationRequest containing recipient, type, and data
     * @return ResponseEntity with status message
     */
    @PostMapping("/api/notify/send")
    ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request);
}
