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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${viago.mail.from-address}")
    private String senderEmail;

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendSimpleEmail(String to, String subject, String body) {
        try {
            log.info("[ASYNC] Sending simple email from: {} to: {} [Thread: {}]",
                    senderEmail, to, Thread.currentThread().getName());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
            log.info("[ASYNC] Simple email sent successfully to: {}", to);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            log.error("[ASYNC] Error sending simple email to: {}", to, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            log.info("[ASYNC] Sending HTML email from: {} to: {} [Thread: {}]",
                    senderEmail, to, Thread.currentThread().getName());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info("[ASYNC] HTML email sent successfully to: {}", to);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            log.error("[ASYNC] Failed to send HTML email to: {}", to, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Sends a notification email using Thymeleaf templates based on notification
     * type.
     *
     * @param request NotificationRequest containing recipient, type, and dynamic
     *                data
     */
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendNotification(NotificationRequest request) {
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
            log.info("[ASYNC] Sending {} notification to: {} [Thread: {}]",
                    request.getType(), request.getTo(), Thread.currentThread().getName());

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
            sendHtmlEmail(request.getTo(), subject, htmlContent).join();

            log.info("[ASYNC] {} notification sent successfully to: {}", request.getType(), request.getTo());
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("[ASYNC] Failed to send {} notification to: {}. Error: {}",
                    request.getType(), request.getTo(), e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
