package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Send push notification to a user
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> notificationRequest) {
        try {
            Object result = notificationService.sendNotification(notificationRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Send trip-related notification
     */
    @PostMapping("/trips/{tripId}/send")
    public ResponseEntity<?> sendTripNotification(@PathVariable String tripId,
                                                   @RequestBody Map<String, Object> notificationRequest) {
        try {
            Object result = notificationService.sendTripNotification(tripId, notificationRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Register device token for push notifications
     */
    @PostMapping("/devices/register")
    public ResponseEntity<?> registerDevice(@RequestBody Map<String, Object> deviceRequest) {
        try {
            Object result = notificationService.registerDevice(deviceRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Unregister device token
     */
    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<?> unregisterDevice(@PathVariable String deviceId) {
        try {
            notificationService.unregisterDevice(deviceId);
            return ResponseEntity.ok(Map.of("message", "Device unregistered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user's notification preferences
     */
    @GetMapping("/users/{userId}/preferences")
    public ResponseEntity<?> getNotificationPreferences(@PathVariable String userId) {
        try {
            Object preferences = notificationService.getNotificationPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update user's notification preferences
     */
    @PutMapping("/users/{userId}/preferences")
    public ResponseEntity<?> updateNotificationPreferences(@PathVariable String userId,
                                                           @RequestBody Map<String, Object> preferences) {
        try {
            Object result = notificationService.updateNotificationPreferences(userId, preferences);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get notification history for a user
     */
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<?> getNotificationHistory(@PathVariable String userId,
                                                     @RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            Object history = notificationService.getNotificationHistory(userId, page, size);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Mark notification as read
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String notificationId) {
        try {
            Object result = notificationService.markAsRead(notificationId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Mark all notifications as read for a user
     */
    @PatchMapping("/users/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable String userId) {
        try {
            Object result = notificationService.markAllAsRead(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Send bulk notifications
     */
    @PostMapping("/bulk")
    public ResponseEntity<?> sendBulkNotifications(@RequestBody Map<String, Object> bulkRequest) {
        try {
            Object result = notificationService.sendBulkNotifications(bulkRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

