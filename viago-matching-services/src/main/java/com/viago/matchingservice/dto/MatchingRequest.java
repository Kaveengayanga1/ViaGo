package com.viago.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingRequest {
    private String tripId;
    private Double pickupLat;
    private Double pickupLng;
    private String vehicleType;
}
