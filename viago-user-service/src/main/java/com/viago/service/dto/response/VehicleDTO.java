package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long id;
    private String model;
    private String plateNumber;
    private String color;
    private Long userId;
}
