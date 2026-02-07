package com.viago.tripservice.dto;

import lombok.Data;

@Data
public class DriverAction {
    private Long rideId;
    private Long driverId;
    private String status;
}
