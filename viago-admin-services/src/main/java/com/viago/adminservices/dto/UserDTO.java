package com.viago.adminservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private Role role;
    private VehicleDTO vehicle;
    private Boolean enabled;
}

