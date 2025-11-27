package com.viago.auth.repository;

import com.viago.auth.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);
    Optional<VehicleEntity> findByDriverUserId(Long driverId);
    void deleteByDriverUserId(Long driverId);
}

