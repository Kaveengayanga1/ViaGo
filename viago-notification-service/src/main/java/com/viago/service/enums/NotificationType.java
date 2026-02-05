package com.viago.service.enums;

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
    ACCOUNT_DELETION
}
