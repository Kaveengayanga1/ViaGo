package com.viago.tripservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TripRequest {
    private String pickupAddress;
    private Double pickupLat;
    private Double pickupLng;

    private String dropAddress;
    private Double dropLat;
    private Double dropLng;

    private String vehicleType;
    private String paymentMethod;
}
