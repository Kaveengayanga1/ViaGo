package com.viago.rideEngine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripRequestDTO {
    private Long passengerId;
    private Double originLatitude;
    private Double originLongitude;
    private String originAddress;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String destinationAddress;
    private java.time.LocalDateTime scheduledTime;
}

