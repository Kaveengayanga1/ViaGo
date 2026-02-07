package com.viago.tripservice.service;

import com.viago.tripservice.dto.RideRequest;
import com.viago.tripservice.enums.RideStatus;
import com.viago.tripservice.model.Ride;
import com.viago.tripservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideManagementService {
    private static final Logger log = LoggerFactory.getLogger(RideManagementService.class);
    private final RideRepository rideRepository;

    /**
     * අලුත් Ride එකක් සාදා Database එකට Save කිරීම.
     * saveAndFlush මගින් දත්ත වහාම DB එකට ලියවෙන බව සහතික කරයි.
     */
    @Transactional
    public Ride createRide(RideRequest request) {
        log.info("💾 Creating ride for rider: {}", request.getRiderId());

        Ride ride = new Ride();
        ride.setRiderId(request.getRiderId());
        ride.setPickupLat(request.getPickupLat());
        ride.setPickupLng(request.getPickupLng());
        ride.setPickupAddress(request.getPickupAddress());
        ride.setDropAddress(request.getDropAddress());
        ride.setPrice(request.getPrice());
        ride.setStatus(RideStatus.SEARCHING);

        // ⚠️ වැදගත්: saveAndFlush භාවිතා කරන්න. එවිට Transaction commit වීමට පෙරම SQL එක DB එකට යයි.
        Ride savedRide = rideRepository.saveAndFlush(ride);

        log.info("✅ Ride created successfully: rideId={}, status={}", savedRide.getRideId(), savedRide.getStatus());
        return savedRide;
    }

    /**
     * Driver කෙනෙක් Ride එක Accept කිරීම.
     * Transaction Isolation මට්ටම READ_COMMITTED ලෙස තැබීමෙන්,
     * createRide එකෙන් save වූ දත්ත කියවීමට හැකි බව සහතික කරයි.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean assignDriver(Long rideId, Long driverId) {
        log.info("🔄 Processing Driver Assignment: Driver {} -> Ride {}", driverId, rideId);

        // 1. Ride එක Database එකෙන් සොයන්න
        Optional<Ride> rideOpt = rideRepository.findById(rideId);

        if (rideOpt.isPresent()) {
            Ride ride = rideOpt.get();

            // 2. Status Check: තවමත් SEARCHING තත්ත්වයේ තිබේදැයි බලන්න
            if (ride.getStatus() == RideStatus.SEARCHING) {
                ride.setStatus(RideStatus.ACCEPTED);
                ride.setDriverId(driverId);

                // Update එක වහාම සිදු කරන්න
                rideRepository.saveAndFlush(ride);

                log.info("✅ SUCCESS: Driver {} assigned to Ride {}", driverId, rideId);
                return true;
            } else {
                log.warn("⚠️ FAILED: Ride {} is already taken. Current Status: {}", rideId, ride.getStatus());
                return false;
            }
        } else {
            // Ride එක සොයාගත නොහැකි නම් Database එකේ මුළු Rides ගණන බලන්න (Debug සඳහා)
            long count = rideRepository.count();
            log.error("❌ ERROR: Ride {} NOT FOUND! Total rides in DB: {}", rideId, count);
            return false;
        }
    }

    @Transactional
    public void updateStatus(Long rideId, RideStatus status) {
        rideRepository.findById(rideId).ifPresent(ride -> {
            log.info("📝 Updating Ride {} status to {}", rideId, status);
            ride.setStatus(status);
            rideRepository.saveAndFlush(ride);
        });
    }

    public Ride getRide(Long rideId) {
        return rideRepository.findById(rideId).orElse(null);
    }

    public Long getRiderId(Long rideId) {
        return rideRepository.findById(rideId)
                .map(Ride::getRiderId)
                .orElse(null);
    }
}
