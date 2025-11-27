package com.viago.adminservices.dto.response;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

