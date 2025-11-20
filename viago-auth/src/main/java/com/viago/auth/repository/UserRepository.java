package com.viago.auth.repository;

import com.viago.auth.dto.Role;
import com.viago.auth.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    public Boolean existsUserEntityByEmailContainingIgnoreCase(String email);
    public boolean existsByUserId(Long userId);
    boolean deleteByUserId(Long userId);

    List<UserEntity> findByRole(Role role);
    UserEntity findUserEntityByEmailIgnoreCase(String email);
    UserEntity findUserEntityByUsernameIgnoreCase(String username);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailContainingIgnoreCase(String email);


}
