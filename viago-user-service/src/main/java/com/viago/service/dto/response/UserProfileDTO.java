package com.viago.service.dto.response;

import com.viago.service.dto.DriverStatus;
import com.viago.service.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
    private DriverStatus driverStatus;
    private Double averageRating;
    private Integer totalRatings;
}
