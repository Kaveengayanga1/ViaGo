package com.viago.auth.repository;

import com.viago.auth.dto.Role;
import com.viago.auth.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity,Long>, CrudRepository<UserEntity,Long> {
    public Boolean existsUserEntityByEmailContainingIgnoreCase(String email);
    public boolean existsByUserId(Long userId);
    boolean deleteByUserId(Long userId);

    List<UserEntity> findByRole(Role role);
    UserEntity findUserEntityByEmailIgnoreCase(String email);
    UserEntity findUserEntityByUsernameIgnoreCase(String username);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailContainingIgnoreCase(String email);
    
    // Optimized method to find user by email OR username in single query
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(:identifier) OR LOWER(u.username) = LOWER(:identifier)")
    Optional<UserEntity> findByEmailOrUsernameIgnoreCase(@Param("identifier") String identifier);
    
    // Method to find users by role with pagination
    Page<UserEntity> findByRole(Role role, Pageable pageable);
    
    // Method to delete user by email
    void deleteByEmailIgnoreCase(String email);
    
    // Method to check if user exists by email
    boolean existsByEmailIgnoreCase(String email);

}
