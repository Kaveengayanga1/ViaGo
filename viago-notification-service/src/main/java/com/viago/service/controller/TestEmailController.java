package com.viago.service.controller;

import com.viago.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notify")
@Slf4j
public class TestEmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Test endpoint to send a test email.
     * 
     * @param email Target email address (query parameter)
     * @return ResponseEntity with status message
     */
    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestParam String email) {
        try {
            String subject = "Hello from Antigravity";
            String htmlBody = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                padding: 20px;
                            }
                            .container {
                                background-color: white;
                                padding: 30px;
                                border-radius: 10px;
                                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            }
                            h1 {
                                color: #333;
                            }
                            p {
                                color: #666;
                                line-height: 1.6;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h1>🚀 Hello from Antigravity</h1>
                            <p>This is a test email from the ViaGO notification service.</p>
                            <p>If you're reading this, your email configuration is working correctly!</p>
                            <hr>
                            <p style="font-size: 12px; color: #999;">Sent from viago-notification-service</p>
                        </div>
                    </body>
                    </html>
                    """;

            emailService.sendHtmlEmail(email, subject, htmlBody);

            log.info("Test email queued for: {}", email);
            return ResponseEntity.ok("Test email queued for sending to: " + email);
        } catch (Exception e) {
            log.error("Failed to send test email to: {}. Error: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}
