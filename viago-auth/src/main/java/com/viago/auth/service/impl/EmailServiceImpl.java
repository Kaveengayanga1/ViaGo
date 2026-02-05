package com.viago.auth.service.impl;

import com.viago.auth.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    // TODO: Integrate with actual email service (SMTP, SendGrid, AWS SES, etc.)
    // For now, this is a stub implementation that logs emails

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken, String username) {
        log.info("=== PASSWORD RESET EMAIL ===");
        log.info("To: {}", toEmail);
        log.info("Username: {}", username);
        log.info("Reset Token: {}", resetToken);
        log.info("Reset Link: http://localhost:3000/reset-password?token={}", resetToken);
        log.info("===========================");

        // TODO: Replace with actual email sending logic
        // Example with JavaMailSender:
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(toEmail);
        // message.setSubject("Password Reset Request");
        // message.setText("Click here to reset:
        // http://localhost:3000/reset-password?token=" + resetToken);
        // mailSender.send(message);
    }

    @Override
    public void sendEmailVerification(String toEmail, String verificationToken, String username) {
        log.info("=== EMAIL VERIFICATION ===");
        log.info("To: {}", toEmail);
        log.info("Username: {}", username);
        log.info("Verification Token: {}", verificationToken);
        log.info("Verification Link: http://localhost:3000/verify-email?token={}", verificationToken);
        log.info("==========================");

        // TODO: Replace with actual email sending logic
    }
}
