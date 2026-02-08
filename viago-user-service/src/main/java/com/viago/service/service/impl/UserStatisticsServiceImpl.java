package com.viago.service.service.impl;

import com.viago.service.dto.response.UserStatisticsResponseDTO;
import com.viago.service.entity.UserStatisticsEntity;
import com.viago.service.exception.ResourceNotFoundException;
import com.viago.service.repository.UserStatisticsRepository;
import com.viago.service.service.UserStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatisticsServiceImpl implements UserStatisticsService {

    private final UserStatisticsRepository userStatisticsRepository;

    @Override
    public UserStatisticsResponseDTO getUserStatistics(Long userId) {
        UserStatisticsEntity statistics = userStatisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistics not found for user id: " + userId));

        return mapToDTO(statistics);
    }

    @Override
    @Transactional
    public UserStatisticsResponseDTO updateTripCount(Long userId, Integer tripCount) {
        UserStatisticsEntity statistics = userStatisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistics not found for user id: " + userId));

        statistics.setTotalTrips(tripCount);
        UserStatisticsEntity savedStats = userStatisticsRepository.save(statistics);

        return mapToDTO(savedStats);
    }

    @Override
    @Transactional
    public UserStatisticsResponseDTO updateTotalDistance(Long userId, Double distance) {
        UserStatisticsEntity statistics = userStatisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistics not found for user id: " + userId));

        statistics.setTotalDistanceTraveled(distance);
        UserStatisticsEntity savedStats = userStatisticsRepository.save(statistics);

        return mapToDTO(savedStats);
    }

    @Override
    @Transactional
    public UserStatisticsResponseDTO updateTotalSpent(Long userId, Double amount) {
        UserStatisticsEntity statistics = userStatisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistics not found for user id: " + userId));

        statistics.setTotalAmountSpent(amount);
        UserStatisticsEntity savedStats = userStatisticsRepository.save(statistics);

        return mapToDTO(savedStats);
    }

    @Override
    @Transactional
    public UserStatisticsResponseDTO updateRating(Long userId, Double rating) {
        UserStatisticsEntity statistics = userStatisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistics not found for user id: " + userId));

        statistics.setAverageRating(rating);
        // Also update total ratings count if provided, but method signature only has
        // rating
        // For now, we assume user entity handles the source of truth for ratings,
        // but we sync it here for read performance if needed.

        UserStatisticsEntity savedStats = userStatisticsRepository.save(statistics);

        return mapToDTO(savedStats);
    }

    @Override
    @Transactional
    public UserStatisticsResponseDTO createDefaultStatistics(Long userId) {
        if (userStatisticsRepository.findByUserId(userId).isPresent()) {
            log.warn("Statistics already exist for user: {}", userId);
            return getUserStatistics(userId);
        }

        UserStatisticsEntity statistics = UserStatisticsEntity.builder()
                .userId(userId)
                .totalTrips(0)
                .totalDistanceTraveled(0.0)
                .totalAmountSpent(0.0)
                .averageRating(0.0)
                .totalRatingsReceived(0)
                .build();

        UserStatisticsEntity savedStats = userStatisticsRepository.save(statistics);
        log.info("Default statistics created for user: {}", userId);

        return mapToDTO(savedStats);
    }

    private UserStatisticsResponseDTO mapToDTO(UserStatisticsEntity entity) {
        return UserStatisticsResponseDTO.builder()
                .userId(entity.getUserId())
                .totalTrips(entity.getTotalTrips())
                .totalDistanceTraveled(entity.getTotalDistanceTraveled())
                .totalAmountSpent(entity.getTotalAmountSpent())
                .averageRating(entity.getAverageRating())
                .totalRatingsReceived(entity.getTotalRatingsReceived())
                .build();
    }
}
