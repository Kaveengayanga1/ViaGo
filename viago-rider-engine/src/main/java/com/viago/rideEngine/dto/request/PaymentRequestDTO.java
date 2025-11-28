package com.viago.rideEngine.dto.request;

import com.viago.rideEngine.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long tripId;
    private Long userId;
    private PaymentMethod paymentMethod;
    private String paymentMethodId; // ID of saved payment method
    private String promoCode;
}

