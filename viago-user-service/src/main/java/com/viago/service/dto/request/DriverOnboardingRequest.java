package com.viago.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverOnboardingRequest {

    @NotBlank(message = "Vehicle model is required")
    private String model;

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Color is required")
    private String color;
}
