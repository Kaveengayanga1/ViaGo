package com.viago.rideEngine.service;

import java.util.Map;

public interface PaymentService {
    Object processPayment(String tripId, Map<String, Object> paymentRequest);
    Object addPaymentMethod(Map<String, Object> paymentMethod);
    Object getPaymentMethods(String userId);
    Object setDefaultPaymentMethod(String userId, String methodId);
    void removePaymentMethod(String userId, String methodId);
    Object getPaymentHistory(String userId, int page, int size);
    Object getTripPayment(String tripId);
    Object refundPayment(String tripId, Map<String, String> refundRequest);
    Object getPaymentStatus(String tripId);
    Object handleWebhook(Map<String, Object> webhookData, Map<String, String> headers);
}

