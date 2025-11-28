package com.viago.rideEngine.dto.response;

import com.viago.rideEngine.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverResponseDTO {
    private Long driverId;
    private Long userId;
    private String fullName;
    private String licenseNumber;
    private String vehicleModel;
    private String vehiclePlateNumber;
    private String vehicleColor;
    private Integer vehicleYear;
    private DriverStatus status;
    private Double currentLatitude;
    private Double currentLongitude;
    private Boolean isAvailable;
    private Double rating;
    private Integer totalTrips;
    private LocalDateTime createdAt;
}

