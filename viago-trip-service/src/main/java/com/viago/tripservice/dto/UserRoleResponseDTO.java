package com.viago.tripservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleResponseDTO {
    private Long userId;
    private String role;
    private Boolean isActive;
    private Boolean isVerified;
}
