package com.viago.tripservice.service;

import com.viago.tripservice.dto.RideRequest;
import com.viago.tripservice.enums.RideStatus;
import com.viago.tripservice.model.Ride;
import com.viago.tripservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideManagementService {
    private final RideRepository rideRepository;

    public Ride createRide(RideRequest request) {
        Ride ride = new Ride();
        ride.setRiderId(request.getRiderId());
        ride.setPickupLat(request.getPickupLat());
        ride.setPickupLng(request.getPickupLng());
        ride.setPickupAddress(request.getPickupAddress());
        ride.setDropAddress(request.getDropAddress());
        ride.setPrice(request.getPrice());
        ride.setStatus(RideStatus.SEARCHING);
        return rideRepository.save(ride);
    }

    @Transactional
    public boolean assignDriver(Long rideId, Long driverId) {
        Optional<Ride> rideOpt = rideRepository.findById(rideId);
        if (rideOpt.isPresent()) {
            Ride ride = rideOpt.get();
            if (ride.getStatus() == RideStatus.SEARCHING) {
                ride.setStatus(RideStatus.ACCEPTED);
                ride.setDriverId(driverId);
                rideRepository.save(ride);
                return true; // Success
            }
        }
        return false;
    }

    public void updateStatus(Long rideId, RideStatus status) {
        rideRepository.findById(rideId).ifPresent(ride -> {
            ride.setStatus(status);
            rideRepository.save(ride);
        });
    }

    public Ride getRide(Long rideId) {
        return rideRepository.findById(rideId).orElse(null);
    }

    public Long getRiderId(Long rideId) {
        Ride ride = getRide(rideId);
        return ride != null ? ride.getRiderId() : null;
    }

}
