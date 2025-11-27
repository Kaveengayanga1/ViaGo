package com.viago.adminservices.dto.response;

import com.viago.adminservices.dto.UserDTO;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private String jwtToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();
    private UserDTO data;
}

