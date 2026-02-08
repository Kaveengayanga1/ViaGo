package com.viago.service.repository;

import com.viago.service.entity.UserStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for UserStatisticsEntity
 * Handles user statistics data access operations
 */
@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatisticsEntity, Long> {
    
    /**
     * Find statistics by user ID
     * @param userId the user ID
     * @return Optional containing UserStatisticsEntity if found
     */
    Optional<UserStatisticsEntity> findByUserId(Long userId);
    
    /**
     * Find statistics for multiple users
     * @param userIds list of user IDs
     * @return List of UserStatisticsEntity
     */
    List<UserStatisticsEntity> findByUserIdIn(List<Long> userIds);
}
