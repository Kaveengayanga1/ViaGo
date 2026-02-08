package com.viago.service.repository;

import com.viago.service.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByRatedUserId(Long userId);

    List<ReviewEntity> findByRaterUserId(Long userId);
}
