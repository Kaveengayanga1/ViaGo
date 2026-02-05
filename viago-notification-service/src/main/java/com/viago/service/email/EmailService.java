package com.viago.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Sends a simple plain text email.
     *
     * @param to      Recipient email address
     * @param subject Email subject
     * @param body    Plain text email body
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            log.info("Attempting to send email from: {} to: {}", fromEmail, to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Sends an HTML email.
     *
     * @param to       Recipient email address
     * @param subject  Email subject
     * @param htmlBody HTML content for email body
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            log.info("Attempting to send HTML email from: {} to: {}", fromEmail, to);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML content

            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }
}
