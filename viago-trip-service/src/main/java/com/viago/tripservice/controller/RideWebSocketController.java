package com.viago.tripservice.controller;

import com.viago.tripservice.dto.*;
import com.viago.tripservice.model.Ride;
import com.viago.tripservice.service.DriverMatchingService;
import com.viago.tripservice.service.RideManagementService;
import com.viago.tripservice.service.UserIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin
@RequestMapping("api/trips")
public class RideWebSocketController {
    private static final Logger log = LoggerFactory.getLogger(RideWebSocketController.class);

    private final RideManagementService rideService;
    private final DriverMatchingService matchingService;
    private final UserIntegrationService userIntegrationService;
    private final SimpMessagingTemplate messagingTemplate;

    // Cache
    private final ConcurrentHashMap<Long, Long> rideToRiderCache = new ConcurrentHashMap<>();

    public RideWebSocketController(RideManagementService rideService,
                                   DriverMatchingService matchingService,
                                   UserIntegrationService userIntegrationService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.rideService = rideService;
        this.matchingService = matchingService;
        this.userIntegrationService = userIntegrationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/driver-update")
    public void updateDriverLocation(@Payload LocationUpdateDto loc) {
        matchingService.updateLocation(loc.getDriverId(), loc.getLat(), loc.getLng());
    }


    @MessageMapping("/request-ride")
    public void requestRide(@Payload RideRequest request) {
        log.info("🚕 Ride request received from rider: {}", request.getRiderId());

        Ride ride = rideService.createRide(request);
        Long tripId = ride.getRideId();

        rideToRiderCache.put(tripId, request.getRiderId());
        log.info("💾 Ride {} cached and committed, ready for driver acceptance", tripId);

        // Notify Rider: SEARCHING
        messagingTemplate.convertAndSend("/topic/ride-status/" + request.getRiderId(),
                new TripUpdate("SEARCHING", tripId, null));

        // Find Drivers
        List<Long> nearbyDrivers = matchingService.findNearbyDrivers(request.getPickupLat(), request.getPickupLng());

        if (nearbyDrivers.isEmpty()) {
            log.warn("⚠️ No nearby drivers found for ride: {}", tripId);
        }

        // Notify Drivers
        RideOffer offer = new RideOffer(tripId, request.getRiderName(), request.getPickupAddress(),
                request.getDropAddress(), request.getPrice(), request.getPickupLat(), request.getPickupLng());

        for (Long driverId : nearbyDrivers) {
            messagingTemplate.convertAndSend("/topic/driver-offers/" + driverId, offer);
        }
    }

    @MessageMapping("/accept-ride")
    public void acceptRide(@Payload DriverAction action) {
        log.info("🤝 Driver {} attempting to accept ride: {}", action.getDriverId(), action.getRideId());
        
        // Check cache first
        Long cachedRiderId = rideToRiderCache.get(action.getRideId());
        if (cachedRiderId != null) {
            log.info("💾 Found ride {} in cache, mapped to rider {}", action.getRideId(), cachedRiderId);
        } else {
            log.warn("⚠️ Ride {} NOT found in cache", action.getRideId());
        }

        boolean success = rideService.assignDriver(action.getRideId(), action.getDriverId());

        if (success) {
            log.info("✅ assignDriver returned SUCCESS for ride {}", action.getRideId());
            
            // Get Driver Details (for controller-level notification if needed)
            UserDetailsDto driverDetails = userIntegrationService.getUserDetails(action.getDriverId());
            
            if (driverDetails != null) {
                log.info("📋 Controller: Driver details - name={}, vehicle={}, phone={}", 
                         driverDetails.getName(), driverDetails.getVehicleNo(), driverDetails.getPhone());
            }

            // Get Rider ID
            Long riderId = rideToRiderCache.get(action.getRideId());
            if (riderId == null) {
                log.warn("⚠️ Rider ID not in cache, fetching from database");
                riderId = rideService.getRiderId(action.getRideId());
            }

            if (riderId != null) {
                log.info("✅ Rider ID confirmed: {}", riderId);
                
                // Send TripUpdate (controller-level notification)
                TripUpdate update = new TripUpdate("DRIVER_FOUND", action.getRideId(), driverDetails);

                log.info("📤 Controller: Sending TripUpdate to /topic/ride-status/{}", riderId);
                log.info("📦 TripUpdate payload: status=DRIVER_FOUND, rideId={}, driverData={}", 
                         action.getRideId(), (driverDetails != null ? driverDetails.getName() : "null"));

                messagingTemplate.convertAndSend("/topic/ride-status/" + riderId, update);
                
                log.info("✅ Controller: TripUpdate sent successfully");

                // Notify Driver (Success)
                messagingTemplate.convertAndSend("/topic/driver-notify/" + action.getDriverId(), "SUCCESS");
                log.info("✅ Driver {} notified of success", action.getDriverId());

                rideToRiderCache.remove(action.getRideId());
                log.info("🗑️ Removed ride {} from cache", action.getRideId());
            } else {
                log.error("❌ CRITICAL: Could not determine rider ID for ride {}", action.getRideId());
            }
        } else {
            log.warn("❌ assignDriver returned FAILURE for ride {}", action.getRideId());
            messagingTemplate.convertAndSend("/topic/driver-notify/" + action.getDriverId(), "RIDE_TAKEN");
            rideToRiderCache.remove(action.getRideId());
        }
    }

    @MessageMapping("/trip-started")
    public void startTrip(@Payload DriverAction action) {
        log.info("🚗 Driver {} started trip: {}", action.getDriverId(), action.getRideId());
        
        // Get Rider ID for this ride
        Long riderId = rideService.getRiderId(action.getRideId());
        
        if (riderId != null) {
            log.info("📤 Sending TRIP_STARTED status to rider: {}", riderId);
            
            // Notify Rider: TRIP_STARTED
            TripUpdate update = new TripUpdate("TRIP_STARTED", action.getRideId(), null);
            messagingTemplate.convertAndSend("/topic/ride-status/" + riderId, update);
            
            log.info("✅ Trip started notification sent successfully");
        } else {
            log.error("❌ Could not find rider ID for ride: {}", action.getRideId());
        }
    }

    @MessageMapping("/trip-ended")
    public void endTrip(@Payload DriverAction action) {
        log.info("🏁 Driver {} ended trip: {}", action.getDriverId(), action.getRideId());
        
        // Get Rider ID for this ride
        Long riderId = rideService.getRiderId(action.getRideId());
        
        if (riderId != null) {
            log.info("📤 Sending TRIP_ENDED status to rider: {}", riderId);
            
            // Notify Rider: TRIP_ENDED
            TripUpdate update = new TripUpdate("TRIP_ENDED", action.getRideId(), null);
            messagingTemplate.convertAndSend("/topic/ride-status/" + riderId, update);
            
            log.info("✅ Trip ended notification sent successfully");
            
            // Optional: Update ride status in database to COMPLETED
            rideService.completeRide(action.getRideId());
        } else {
            log.error("❌ Could not find rider ID for ride: {}", action.getRideId());
        }
    }
}