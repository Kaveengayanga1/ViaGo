package com.viago.tripservice.repository;

import com.viago.tripservice.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
}
