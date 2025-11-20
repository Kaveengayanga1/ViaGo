package com.viago.auth.dto.response;

import com.viago.auth.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private String jwtToken;
    private String refreshToken;
    private String tokenType;       // e.g., "Bearer"
    private Long expiresIn;         // optional
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();
    private UserDTO data;

}
