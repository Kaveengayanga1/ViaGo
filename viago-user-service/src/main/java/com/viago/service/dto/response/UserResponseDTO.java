package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureUrl;
    private String preferredLanguage;
    private String timezone;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
