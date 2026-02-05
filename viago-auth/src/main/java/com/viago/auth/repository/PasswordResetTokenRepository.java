package com.viago.auth.repository;

import com.viago.auth.entity.PasswordResetTokenEntity;
import com.viago.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    Optional<PasswordResetTokenEntity> findByUserAndUsedFalseAndExpiryDateAfter(
            UserEntity user, LocalDateTime currentTime);

    void deleteByExpiryDateBefore(LocalDateTime currentTime);

    void deleteByUser(UserEntity user);
}
