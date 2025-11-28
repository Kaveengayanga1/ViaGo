package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Process payment for a trip
     */
    @PostMapping("/trips/{tripId}/process")
    public ResponseEntity<?> processPayment(@PathVariable String tripId,
                                            @RequestBody Map<String, Object> paymentRequest) {
        try {
            Object result = paymentService.processPayment(tripId, paymentRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Add payment method
     */
    @PostMapping("/methods")
    public ResponseEntity<?> addPaymentMethod(@RequestBody Map<String, Object> paymentMethod) {
        try {
            Object result = paymentService.addPaymentMethod(paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user's payment methods
     */
    @GetMapping("/users/{userId}/methods")
    public ResponseEntity<?> getPaymentMethods(@PathVariable String userId) {
        try {
            Object methods = paymentService.getPaymentMethods(userId);
            return ResponseEntity.ok(methods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Set default payment method
     */
    @PatchMapping("/methods/{methodId}/default")
    public ResponseEntity<?> setDefaultPaymentMethod(@PathVariable String methodId,
                                                     @RequestParam String userId) {
        try {
            Object result = paymentService.setDefaultPaymentMethod(userId, methodId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Remove payment method
     */
    @DeleteMapping("/methods/{methodId}")
    public ResponseEntity<?> removePaymentMethod(@PathVariable String methodId,
                                                  @RequestParam String userId) {
        try {
            paymentService.removePaymentMethod(userId, methodId);
            return ResponseEntity.ok(Map.of("message", "Payment method removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get payment history for a user
     */
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<?> getPaymentHistory(@PathVariable String userId,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            Object history = paymentService.getPaymentHistory(userId, page, size);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get payment details for a trip
     */
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<?> getTripPayment(@PathVariable String tripId) {
        try {
            Object payment = paymentService.getTripPayment(tripId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Refund payment
     */
    @PostMapping("/trips/{tripId}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable String tripId,
                                           @RequestBody(required = false) Map<String, String> refundRequest) {
        try {
            Object result = paymentService.refundPayment(tripId, refundRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verify payment status
     */
    @GetMapping("/trips/{tripId}/status")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String tripId) {
        try {
            Object status = paymentService.getPaymentStatus(tripId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Handle payment gateway webhook
     */
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> webhookData,
                                           @RequestHeader(required = false) Map<String, String> headers) {
        try {
            Object result = paymentService.handleWebhook(webhookData, headers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

