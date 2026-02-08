package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideOffer {
    private Long rideId;
    private String riderName;
    private String pickupAddress;
    private String dropAddress;
    private double price;
    private double pickupLat;
    private double pickupLng;
}
