package com.viago.rideEngine.dto.response;

import com.viago.rideEngine.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripResponseDTO {
    private Long tripId;
    private Long passengerId;
    private Long driverId;
    private TripStatus status;
    private Double originLatitude;
    private Double originLongitude;
    private String originAddress;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String destinationAddress;
    private Double distance;
    private Integer estimatedDuration;
    private Double estimatedFare;
    private Double finalFare;
    private LocalDateTime scheduledTime;
    private LocalDateTime pickupTime;
    private LocalDateTime startTime;
    private LocalDateTime completedTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

