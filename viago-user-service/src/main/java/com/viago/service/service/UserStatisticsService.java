package com.viago.service.service;

import com.viago.service.dto.response.UserStatisticsResponseDTO;

public interface UserStatisticsService {
    UserStatisticsResponseDTO getUserStatistics(Long userId);

    UserStatisticsResponseDTO updateTripCount(Long userId, Integer tripCount);

    UserStatisticsResponseDTO updateTotalDistance(Long userId, Double distance);

    UserStatisticsResponseDTO updateTotalSpent(Long userId, Double amount);

    UserStatisticsResponseDTO updateRating(Long userId, Double rating);

    UserStatisticsResponseDTO createDefaultStatistics(Long userId);
}
