package com.viago.tripservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateDto {

    private Long driverId;
    private double lat;
    private double lng;
}
