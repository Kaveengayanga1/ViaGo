package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private String vehicleModel;
    private String vehicleNumber;

}
