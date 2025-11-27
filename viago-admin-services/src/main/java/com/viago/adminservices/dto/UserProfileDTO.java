package com.viago.adminservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long authUserId;
    private String username;
    private String email;
    private String role;
    private String fullName;
    private String phoneNumber;
    private String address;
}

