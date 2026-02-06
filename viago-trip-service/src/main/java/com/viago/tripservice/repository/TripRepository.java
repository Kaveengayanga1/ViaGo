package com.viago.tripservice.repository;


import com.viago.tripservice.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {
    List<Trip> findByRiderId(String riderId);
    List<Trip> findByDriverId(String driverId);
}
