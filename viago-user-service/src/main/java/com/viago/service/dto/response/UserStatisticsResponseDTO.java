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
public class UserStatisticsResponseDTO {
    private Long statisticId;
    private Long userId;
    private Integer totalTrips;
    private Double totalDistanceTraveled;
    private Double totalAmountSpent;
    private Double averageRating;
    private Integer totalRatingsReceived;
    private LocalDateTime updatedAt;
}
