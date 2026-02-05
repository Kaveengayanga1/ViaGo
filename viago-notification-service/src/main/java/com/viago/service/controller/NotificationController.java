package com.viago.service.controller;

import com.viago.service.dto.NotificationRequest;
import com.viago.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling notification requests.
 */
@RestController
@RequestMapping("/api/notify")
@Slf4j
public class NotificationController {

    @Autowired
    private EmailService emailService;

    /**
     * Sends a notification email using templates.
     *
     * @param request NotificationRequest containing recipient, type, and data
     * @return ResponseEntity with status message
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        try {
            // Validate request
            if (request.getTo() == null || request.getTo().isEmpty()) {
                return ResponseEntity.badRequest().body("Recipient email is required");
            }

            if (request.getType() == null) {
                return ResponseEntity.badRequest().body("Notification type is required");
            }

            log.info("Received notification request: type={}, to={}", request.getType(), request.getTo());

            // Send notification asynchronously - returns immediately
            emailService.sendNotification(request);

            // Return success response immediately (email sending happens in background)
            return ResponseEntity.ok(String.format(
                    "%s notification queued for sending to: %s",
                    request.getType(),
                    request.getTo()));

        } catch (IllegalArgumentException e) {
            log.error("Invalid notification request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());

        } catch (Exception e) {
            log.error("Failed to queue notification: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to queue notification: " + e.getMessage());
        }
    }
}
