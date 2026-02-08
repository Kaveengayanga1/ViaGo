package com.viago.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class Main {
    public static void main(String[] args) {
        // Load environment variables from dev.env file
        // This must happen BEFORE SpringApplication.run() so Spring Boot can access them
        Dotenv dotenv = null;
        String[] possiblePaths = {
            "./dev.env",                                    // Current directory (when running from service root)
            "../dev.env",                                   // Parent directory
            "viago-notification-service/dev.env"            // From backend root
        };
        
        // Try to load from different possible locations
        for (String path : possiblePaths) {
            try {
                java.io.File file = new java.io.File(path);
                if (file.exists() && file.isFile()) {
                    String dir = file.getParent();
                    dotenv = Dotenv.configure()
                            .directory(dir != null ? dir : "./")
                            .filename("dev.env")
                            .ignoreIfMissing()
                            .load();
                    if (dotenv != null && !dotenv.entries().isEmpty()) {
                        System.out.println("Loaded dev.env from: " + file.getAbsolutePath());
                        break;
                    }
                }
            } catch (Exception e) {
                // Try next path
                continue;
            }
        }
        
        // If still not found, try default location (current working directory)
        if (dotenv == null || dotenv.entries().isEmpty()) {
            try {
                dotenv = Dotenv.configure()
                        .filename("dev.env")
                        .ignoreIfMissing()
                        .load();
                if (dotenv != null && !dotenv.entries().isEmpty()) {
                    System.out.println("Loaded dev.env from current directory");
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // Set system properties from dotenv so Spring Boot can access them via ${VAR_NAME}
        if (dotenv != null && !dotenv.entries().isEmpty()) {
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    System.setProperty(key, entry.getValue());
                }
            });
            System.out.println("Environment variables loaded from dev.env");
        } else {
            System.err.println("Warning: Could not load dev.env file. Make sure it exists in the viago-notification-service directory.");
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
        }
        
        SpringApplication.run(Main.class, args);
    }
}