package com.viago.rideEngine.repository;

import com.viago.rideEngine.entity.PaymentEntity;
import com.viago.rideEngine.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    
    Optional<PaymentEntity> findByTripId(Long tripId);
    
    List<PaymentEntity> findByUserId(Long userId);
    
    Page<PaymentEntity> findByUserId(Long userId, Pageable pageable);
    
    List<PaymentEntity> findByUserIdAndStatus(Long userId, PaymentStatus status);
    
    Optional<PaymentEntity> findByTransactionId(String transactionId);
}

