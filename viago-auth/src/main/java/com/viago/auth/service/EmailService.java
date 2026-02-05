package com.viago.auth.service;

public interface EmailService {

    /**
     * Send password reset email with token
     * 
     * @param toEmail    Recipient email address
     * @param resetToken Password reset token
     * @param username   User's name for personalization
     */
    void sendPasswordResetEmail(String toEmail, String resetToken, String username);

    /**
     * Send email verification email
     * 
     * @param toEmail           Recipient email address
     * @param verificationToken Email verification token
     * @param username          User's name for personalization
     */
    void sendEmailVerification(String toEmail, String verificationToken, String username);
}
