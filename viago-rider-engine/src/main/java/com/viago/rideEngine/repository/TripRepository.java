package com.viago.rideEngine.repository;

import com.viago.rideEngine.entity.TripEntity;
import com.viago.rideEngine.enums.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, Long> {
    
    List<TripEntity> findByPassengerId(Long passengerId);
    
    Page<TripEntity> findByPassengerId(Long passengerId, Pageable pageable);
    
    List<TripEntity> findByPassengerIdAndStatus(Long passengerId, TripStatus status);
    
    Page<TripEntity> findByPassengerIdAndStatus(Long passengerId, TripStatus status, Pageable pageable);
    
    List<TripEntity> findByDriverId(Long driverId);
    
    Page<TripEntity> findByDriverId(Long driverId, Pageable pageable);
    
    List<TripEntity> findByDriverIdAndStatus(Long driverId, TripStatus status);
    
    Page<TripEntity> findByDriverIdAndStatus(Long driverId, TripStatus status, Pageable pageable);
    
    Optional<TripEntity> findByTripIdAndPassengerId(Long tripId, Long passengerId);
    
    Optional<TripEntity> findByTripIdAndDriverId(Long tripId, Long driverId);
    
    @Query("SELECT t FROM TripEntity t WHERE t.passengerId = :userId AND t.status IN :statuses")
    List<TripEntity> findActiveTripsByPassenger(@Param("userId") Long userId, @Param("statuses") List<TripStatus> statuses);
    
    @Query("SELECT t FROM TripEntity t WHERE t.driverId = :driverId AND t.status IN :statuses")
    Optional<TripEntity> findActiveTripByDriver(@Param("driverId") Long driverId, @Param("statuses") List<TripStatus> statuses);
}

