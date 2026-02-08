package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private Long id;
    private String name;       // fullName වෙනුවට name
    private String phone;      // phoneNumber වෙනුවට phone
    private String email;
    private String vehicleNo;  // වාහන අංකය
    private String vehicleModel; // වාහන වර්ගය (Prius, Alto etc.)
}