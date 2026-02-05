package com.viago.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetResponse {
    private Boolean success;
    private String message;
    private OffsetDateTime timestamp;
}
