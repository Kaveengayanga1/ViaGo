package com.viago.rideEngine.repository;

import com.viago.rideEngine.entity.DriverEntity;
import com.viago.rideEngine.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    
    Optional<DriverEntity> findByUserId(Long userId);
    
    List<DriverEntity> findByStatus(DriverStatus status);
    
    List<DriverEntity> findByIsAvailable(Boolean isAvailable);
    
    @Query("SELECT d FROM DriverEntity d WHERE d.status = :status AND d.isAvailable = true")
    List<DriverEntity> findAvailableDriversByStatus(@Param("status") DriverStatus status);
    
    @Query(value = "SELECT * FROM drivers WHERE status = 'ONLINE' AND is_available = true " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(current_latitude)) * " +
            "cos(radians(current_longitude) - radians(:lng)) + sin(radians(:lat)) * " +
            "sin(radians(current_latitude)))) ASC LIMIT :limit",
            nativeQuery = true)
    List<DriverEntity> findNearbyDrivers(@Param("lat") Double latitude, 
                                         @Param("lng") Double longitude, 
                                         @Param("limit") Integer limit);
}

