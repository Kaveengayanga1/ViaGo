package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RideRequest {
    private Long riderId;
    private String riderName;
    private double pickupLat;
    private double pickupLng;
    private String pickupAddress;
    private String dropAddress;
    private double price;
}
