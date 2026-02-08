package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideUpdateDto {
    private Long rideId;
    private String status;      // ACCEPTED
    private String message;     // "Driver Found!"
    
    // Rider ට පෙන්වන Driver ගේ විස්තර
    private Long driverId;
    private String driverName;
    private String vehicleNo;
    private String driverPhone;
    private Double fare;
}
