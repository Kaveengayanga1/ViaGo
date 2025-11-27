package com.viago.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long vehicleId;
    private String vehicleType;
    private String model;
    private Integer seatCount;
    private String registrationNumber;
}

