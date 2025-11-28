package com.viago.rideEngine.service;

import java.util.Map;

public interface NotificationService {
    Object sendNotification(Map<String, Object> notificationRequest);
    Object sendTripNotification(String tripId, Map<String, Object> notificationRequest);
    Object registerDevice(Map<String, Object> deviceRequest);
    void unregisterDevice(String deviceId);
    Object getNotificationPreferences(String userId);
    Object updateNotificationPreferences(String userId, Map<String, Object> preferences);
    Object getNotificationHistory(String userId, int page, int size);
    Object markAsRead(String notificationId);
    Object markAllAsRead(String userId);
    Object sendBulkNotifications(Map<String, Object> bulkRequest);
}

