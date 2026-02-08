package com.viago.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing different types of notifications that can be sent.
 */
public enum NotificationType {
    /**
     * Welcome email for new users
     */
    WELCOME,

    /**
     * Password reset email with verification link
     */
    PASSWORD_RESET,

    /**
     * Account deletion confirmation email
     */
    ACCOUNT_DELETION;

    /**
     * Custom deserializer for case-insensitive enum parsing.
     * Allows both "WELCOME" and "welcome" to work when receiving from other services.
     */
    @JsonCreator
    public static NotificationType fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return NotificationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Invalid notification type: '%s'. Valid values are: WELCOME, PASSWORD_RESET, ACCOUNT_DELETION", value)
            );
        }
    }

    /**
     * Custom serializer to ensure consistent JSON output.
     */
    @JsonValue
    public String toValue() {
        return this.name();
    }
}
