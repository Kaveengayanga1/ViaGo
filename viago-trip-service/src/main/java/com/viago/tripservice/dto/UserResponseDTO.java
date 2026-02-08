package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    // Basic user information
    private Long id;
    private String name; // Combined firstName + lastName
    private String phone;
    private String email;
    private String role; // "DRIVER" or "RIDER"

    // Status fields
    private Boolean isActive;
    private Boolean isVerified; // Email/Phone verification status

    // Driver-specific fields
    private String vehicleNo; // Vehicle registration number
    private String vehicleModel; // Vehicle model
    private String licenseNo; // Driver's license number

    // Statistics
    private Double rating; // Average rating
    private Integer totalTrips; // Total number of trips completed
}
