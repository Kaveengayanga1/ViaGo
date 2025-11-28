package com.viago.rideEngine.repository;

import com.viago.rideEngine.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    
    Optional<LocationEntity> findFirstByUserIdOrderByTimestampDesc(String userId);
    
    List<LocationEntity> findByUserId(String userId);
    
    List<LocationEntity> findByUserIdAndUserType(String userId, String userType);
    
    List<LocationEntity> findByTripId(String tripId);
    
    @Query("SELECT l FROM LocationEntity l WHERE l.userId = :userId AND l.timestamp BETWEEN :startTime AND :endTime ORDER BY l.timestamp DESC")
    List<LocationEntity> findLocationHistory(@Param("userId") String userId, 
                                            @Param("startTime") LocalDateTime startTime, 
                                            @Param("endTime") LocalDateTime endTime);
}

