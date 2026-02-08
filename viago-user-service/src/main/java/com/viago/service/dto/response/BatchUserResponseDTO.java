package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Batch User Response
 * Wraps a list of users for batch operations
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchUserResponseDTO {
    
    private List<TripServiceUserDTO> users;
    private Integer totalCount;
}
