package com.viago.tripservice.service;

import com.viago.tripservice.dto.RideRequest;
import com.viago.tripservice.dto.RideUpdateDto;
import com.viago.tripservice.dto.UserDetailsDto;
import com.viago.tripservice.enums.RideStatus;
import com.viago.tripservice.model.Ride;
import com.viago.tripservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideManagementService {
    private static final Logger log = LoggerFactory.getLogger(RideManagementService.class);

    private final RideRepository rideRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserIntegrationService userIntegrationService;

    // 1. Create Ride - අපි මේක ඉක්මනට Commit කරන්න බල කරනවා (REQUIRES_NEW)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

        // saveAndFlush මගින් DB එකට බලෙන් ලියවනවා
        Ride savedRide = rideRepository.saveAndFlush(ride);

        log.info("✅ Ride created with ID: {}", savedRide.getRideId());
        return savedRide;
    }

    /**
     * 2. Assign Driver (The Fix)
     * Isolation.READ_UNCOMMITTED: මෙය දැම්මාම Commit වෙන්න තත්පරයක් පරක්කු වුනත්,
     * Database එකේ මෙම ඩේටා එක තිබුන ගමන් අපිට අරගන්න පුළුවන්.
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public boolean assignDriver(Long rideId, Long driverId) {
        log.info("🔄 Driver {} attempting to accept ride {}", driverId, rideId);

        // Retry Logic එකත් තියාගමු (ආරක්ෂාවට)
        Ride ride = findRideWithRetry(rideId);

        if (ride != null) {
            if (ride.getStatus() == RideStatus.SEARCHING) {

                ride.setStatus(RideStatus.ACCEPTED);
                ride.setDriverId(driverId);
                rideRepository.saveAndFlush(ride);

                UserDetailsDto driver = userIntegrationService.getUserDetails(driverId);

                if (driver == null) {
                    driver = new UserDetailsDto();
                    driver.setName("Unknown");
                }

                // Notification Logic
                RideUpdateDto updateMsg = new RideUpdateDto(
                        ride.getRideId(),
                        "ACCEPTED",
                        "Driver Found!",
                        driverId,
                        driver.getName(),
                        driver.getVehicleNo(),
                        driver.getPhone(),
                        ride.getPrice()
                );

                messagingTemplate.convertAndSend("/topic/ride/" + ride.getRiderId(), updateMsg);
                log.info("✅ SUCCESS: Driver assigned and Rider notified.");
                return true;
            } else {
                log.warn("⚠️ Ride {} taken. Status: {}", rideId, ride.getStatus());
                return false;
            }
        } else {
            log.error("❌ Ride {} NOT FOUND even with READ_UNCOMMITTED", rideId);
            return false;
        }
    }

    private Ride findRideWithRetry(Long rideId) {
        int maxRetries = 5; // Retries ගණන ටිකක් වැඩි කරමු
        int delayMs = 200;

        for (int i = 0; i < maxRetries; i++) {
            Optional<Ride> rideOpt = rideRepository.findById(rideId);
            if (rideOpt.isPresent()) return rideOpt.get();

            log.warn("⏳ Retry {}/{}: Ride {} not visible yet...", i + 1, maxRetries, rideId);
            try { Thread.sleep(delayMs); } catch (InterruptedException e) {}
        }
        return null;
    }

    // Other methods...
    public Long getRiderId(Long rideId) {
        return rideRepository.findById(rideId).map(Ride::getRiderId).orElse(null);
    }
}