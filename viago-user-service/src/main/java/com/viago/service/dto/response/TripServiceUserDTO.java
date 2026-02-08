package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Trip Service Integration
 * Contains all user information needed by viago-trip-service
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripServiceUserDTO {
    
    // Basic user information
    private Long id;
    private String name;          // Combined firstName + lastName
    private String phone;
    private String email;
    private String role;          // "DRIVER" or "RIDER"
    
    // Status fields
    private Boolean isActive;
    private Boolean isVerified;   // Email/Phone verification status
    
    // Driver-specific fields (null for riders)
    private String vehicleNo;     // Vehicle registration number
    private String vehicleModel;  // Vehicle model
    private String licenseNo;     // Driver's license number (optional)
    
    // Rating and trip statistics
    private Double rating;        // Average rating
    private Integer totalTrips;   // Total number of trips completed
}
