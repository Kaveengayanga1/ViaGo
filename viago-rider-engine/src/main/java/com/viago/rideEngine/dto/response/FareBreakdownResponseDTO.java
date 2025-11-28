package com.viago.rideEngine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareBreakdownResponseDTO {
    private Long tripId;
    private Double baseFare;
    private Double distanceFare;
    private Double timeFare;
    private Double surgeMultiplier;
    private Double surgeAmount;
    private Double serviceFee;
    private Double tax;
    private Double discountAmount;
    private String promoCode;
    private Double totalFare;
}

