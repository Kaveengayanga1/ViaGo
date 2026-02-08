package com.viago.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for notification requests containing recipient, type, and dynamic data.
 * Matches the structure used by viago-notification-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    /**
     * Recipient email address
     */
    private String to;

    /**
     * Type of notification to send (e.g., "WELCOME", "PASSWORD_RESET", "ACCOUNT_DELETION")
     */
    private String type;

    /**
     * Dynamic data for template variables (e.g., username, email, role, etc.)
     */
    private Map<String, Object> data;
}
