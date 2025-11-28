package com.viago.rideEngine.repository;

import com.viago.rideEngine.entity.SavedAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedAddressRepository extends JpaRepository<SavedAddressEntity, Long> {
    
    List<SavedAddressEntity> findByUserId(Long userId);
    
    Optional<SavedAddressEntity> findByUserIdAndIsDefault(Long userId, Boolean isDefault);
    
    Optional<SavedAddressEntity> findByAddressIdAndUserId(Long addressId, Long userId);
}

