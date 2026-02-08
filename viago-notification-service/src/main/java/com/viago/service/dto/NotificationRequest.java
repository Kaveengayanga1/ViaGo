package com.viago.service.dto;

import com.viago.service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for notification requests containing recipient, type, and dynamic data.
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
     * Type of notification to send
     */
    private NotificationType type;

    /**
     * Dynamic data for template variables (e.g., name, resetLink, etc.)
     */
    private Map<String, Object> data;
}
