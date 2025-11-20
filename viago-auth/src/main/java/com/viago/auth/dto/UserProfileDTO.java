package com.viago.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long authUserId; // reference to Auth Service userId
    private String username;
    private String email;
    private String role;
    private String fullName; // example profile field
    private String phoneNumber;
    private String address;
}
