package com.viago.auth;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableDiscoveryClient
public class AuthMain1 {
    public static void main(String[] args) {
        // Load environment variables from dev.env file
        // This must happen BEFORE SpringApplication.run() so Spring Boot can access them
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentDir);
        
        Dotenv dotenv = null;
        String[] possiblePaths = {
            "./dev.env",                                    // Current directory (when running from service root)
            "viago-auth/dev.env",                           // From backend root
            "../dev.env"                                    // Parent directory
        };
        
        // Try to load from different possible locations
        for (String path : possiblePaths) {
            try {
                java.io.File file = new java.io.File(path);
                System.out.println("Trying path: " + path + " -> " + file.getAbsolutePath() + " (exists: " + file.exists() + ")");
                if (file.exists() && file.isFile()) {
                    String dir = file.getParent();
                    System.out.println("Loading dev.env from directory: " + dir);
                    dotenv = Dotenv.configure()
                            .directory(dir != null ? dir : "./")
                            .filename("dev.env")
                            .ignoreIfMissing()
                            .load();
                    if (dotenv != null && !dotenv.entries().isEmpty()) {
                        System.out.println("✓ Loaded dev.env from: " + file.getAbsolutePath());
                        System.out.println("  Found " + dotenv.entries().size() + " environment variables");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to load from " + path + ": " + e.getMessage());
                // Try next path
                continue;
            }
        }
        
        // If still not found, try default location (current working directory)
        if (dotenv == null || dotenv.entries().isEmpty()) {
            try {
                System.out.println("Trying default dotenv location...");
                dotenv = Dotenv.configure()
                        .filename("dev.env")
                        .ignoreIfMissing()
                        .load();
                if (dotenv != null && !dotenv.entries().isEmpty()) {
                    System.out.println("✓ Loaded dev.env from default location");
                }
            } catch (Exception e) {
                System.err.println("Failed to load from default location: " + e.getMessage());
            }
        }
        
        // Set system properties from dotenv so Spring Boot can access them via ${VAR_NAME}
        if (dotenv != null && !dotenv.entries().isEmpty()) {
            int loadedCount = 0;
            for (var entry : dotenv.entries()) {
                String key = entry.getKey();
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    System.setProperty(key, entry.getValue());
                    loadedCount++;
                }
            }
            System.out.println("✓ Set " + loadedCount + " environment variables as system properties");
        } else {
            System.err.println("✗ WARNING: Could not load dev.env file!");
            System.err.println("  Current working directory: " + currentDir);
            System.err.println("  Make sure dev.env exists in viago-auth directory");
            System.err.println("  Application may fail to start if required environment variables are missing.");
        }
        
        SpringApplication.run(AuthMain1.class, args);
    }
}