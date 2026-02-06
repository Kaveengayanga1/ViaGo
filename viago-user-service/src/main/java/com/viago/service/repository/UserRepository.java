package com.viago.service.repository;

import com.viago.service.dto.DriverStatus;
import com.viago.service.dto.Role;
import com.viago.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRole(Role role);

    List<UserEntity> findByDriverStatus(DriverStatus status);
}
