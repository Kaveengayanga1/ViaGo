package com.viago.rideEngine.repository;

import com.viago.rideEngine.entity.FareBreakdownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FareBreakdownRepository extends JpaRepository<FareBreakdownEntity, Long> {
    
    Optional<FareBreakdownEntity> findByTripId(Long tripId);
}

