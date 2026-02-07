package com.viago.service.email;

import com.viago.service.dto.NotificationRequest;
import com.viago.service.enums.NotificationType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${viago.mail.from-address}")
    private String senderEmail;

    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            log.info("Sending simple email from: {} to: {}", senderEmail, to);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
            log.info("Simple email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Error sending simple email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            log.info("Sending HTML email from: {} to: {}", senderEmail, to);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Sends a notification email using Thymeleaf templates based on notification
     * type.
     *
     * @param request NotificationRequest containing recipient, type, and dynamic
     *                data
     */
    public void sendNotification(NotificationRequest request) {
        String templateName;
        String subject;

        // Select template and subject based on notification type
        switch (request.getType()) {
            case WELCOME:
                templateName = "welcome-email";
                subject = "Welcome to ViaGO! 🚗";
                break;
            case PASSWORD_RESET:
                templateName = "password-reset-email";
                subject = "Reset Your ViaGO Password 🔐";
                break;
            case ACCOUNT_DELETION:
                templateName = "account-deletion-email";
                subject = "Your ViaGO Account Has Been Deleted";
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + request.getType());
        }

        try {
            log.info("Sending {} notification to: {}", request.getType(), request.getTo());

            // Create Thymeleaf context with data
            Context context = new Context();
            if (request.getData() != null) {
                for (Map.Entry<String, Object> entry : request.getData().entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
            }

            // Process template to HTML string
            String htmlContent = templateEngine.process(templateName, context);

            // Send the email
            sendHtmlEmail(request.getTo(), subject, htmlContent);

            log.info("{} notification sent successfully to: {}", request.getType(), request.getTo());
        } catch (Exception e) {
            log.error("Failed to send {} notification to: {}. Error: {}",
                    request.getType(), request.getTo(), e.getMessage(), e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }
}
