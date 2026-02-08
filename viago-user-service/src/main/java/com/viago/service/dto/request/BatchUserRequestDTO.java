package com.viago.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Batch User Request
 * Used to fetch multiple users in a single request
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchUserRequestDTO {
    
    @NotEmpty(message = "User IDs list cannot be empty")
    private List<Long> userIds;
}
