package com.viago.rideEngine.dto.response;

import com.viago.rideEngine.enums.PaymentMethod;
import com.viago.rideEngine.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long paymentId;
    private Long tripId;
    private Long userId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private Double amount;
    private String currency;
    private String transactionId;
    private String promoCode;
    private Double discountAmount;
    private Double finalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

