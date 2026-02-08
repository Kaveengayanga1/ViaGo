package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TripUpdate {
    private String status;
    private Long rideId;
    private Object data;
}
