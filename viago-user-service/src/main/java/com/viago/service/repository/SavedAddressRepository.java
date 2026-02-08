package com.viago.service.repository;

import com.viago.service.entity.SavedAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedAddressRepository extends JpaRepository<SavedAddressEntity, Long> {

    List<SavedAddressEntity> findByUserId(Long userId);
}
