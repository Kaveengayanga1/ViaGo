package com.viago.service.repository;

import com.viago.service.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    Optional<VehicleEntity> findByUser_UserId(Long userId);

    Optional<VehicleEntity> findByPlateNumber(String plateNumber);
}
