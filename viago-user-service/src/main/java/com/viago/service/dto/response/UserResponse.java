package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse<T> {
    private boolean success;
    private String message;
    private T data;
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();
}
