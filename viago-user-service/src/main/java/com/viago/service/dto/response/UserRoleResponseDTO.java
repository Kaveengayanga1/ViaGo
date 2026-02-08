package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for User Role Validation
 * Used for quick role checks by trip service
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleResponseDTO {
    
    private Long userId;
    private String role;          // "DRIVER", "RIDER", "ADMIN", etc.
    private Boolean isActive;     // Account active status
    private Boolean isVerified;   // Email/Phone verification status
}
