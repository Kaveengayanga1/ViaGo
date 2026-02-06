package com.viago.tripservice.dto.response;

import com.viago.tripservice.enums.TripStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TripResponse {
    private String tripId;
    private TripStatus status;
    private BigDecimal estimatedFare;
    private LocalDateTime requestedAt;
}
