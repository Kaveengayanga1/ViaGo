package com.viago.tripservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TripRequest {
    private Double pickupLat;
    private Double pickupLng;

    private Double dropLat;
    private Double dropLng;

    private String vehicleType;
    private String paymentMethod;
}
