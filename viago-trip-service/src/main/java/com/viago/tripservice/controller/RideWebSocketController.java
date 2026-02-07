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
    
    // In-memory cache to store ride-to-rider mapping (bypasses DB transaction issues)
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

    /**
     * Driver Location යාවත්කාලීන කිරීම (Frontend: /app/driver-update)
     */
    @MessageMapping("/driver-update")
    public void updateDriverLocation(@Payload LocationUpdateDto loc) {
        // Log එක අඩු කළා (Spam නොවීමට), අවශ්‍ය නම් uncomment කරන්න
        // log.info("📍 Driver location updated: driverId={}, lat={}, lng={}", loc.getDriverId(), loc.getLat(), loc.getLng());
        matchingService.updateLocation(loc.getDriverId(), loc.getLat(), loc.getLng());
    }

    /**
     * Rider ගෙන් Ride Request එකක් පැමිණීම (Frontend: /app/request-ride)
     */
    @MessageMapping("/request-ride")
    public void requestRide(@Payload RideRequest request) {
        log.info("🚕 Ride request received from rider: {}", request.getRiderId());

        // 1. Ride එක Database එකේ Save කිරීම (දැන් මෙය Reliable යි)
        Ride ride = rideService.createRide(request);
        Long tripId = ride.getRideId();
        
        // CRITICAL: Store in cache immediately (bypasses DB transaction issues)
        rideToRiderCache.put(tripId, request.getRiderId());
        log.info("💾 Cached ride {} -> rider {} mapping", tripId, request.getRiderId());
        
        log.info("✅ Ride created successfully: rideId={}, status={}", tripId, ride.getStatus());

        // 2. Rider ට දැනුම් දීම (SEARCHING Status)
        log.info("📤 Sending SEARCHING status to /topic/ride-status/{}", request.getRiderId());
        messagingTemplate.convertAndSend(
                "/topic/ride-status/" + request.getRiderId(),
                new TripUpdate("SEARCHING", tripId, null)
        );

        // 3. ළඟම ඉන්න Drivers ලා සෙවීම
        log.info("🔍 Finding nearby drivers for location: lat={}, lng={}", 
                 request.getPickupLat(), request.getPickupLng());
                 
        List<Long> nearbyDrivers = matchingService.findNearbyDrivers(request.getPickupLat(), request.getPickupLng());
        log.info("✅ Found {} nearby driver(s): {}", nearbyDrivers.size(), nearbyDrivers);

        if (nearbyDrivers.isEmpty()) {
            log.warn("⚠️ No nearby drivers found for ride: {}", tripId);
            // අවශ්‍ය නම් Rider ට NO_DRIVERS මැසේජ් එක යැවිය හැක
        }

        // 4. Drivers ලාට Offer එක යැවීම
        RideOffer offer = new RideOffer(
                tripId, 
                request.getRiderName(), 
                request.getPickupAddress(), 
                request.getDropAddress(), 
                request.getPrice(), 
                request.getPickupLat(), 
                request.getPickupLng()
        );

        for (Long driverId : nearbyDrivers) {
            log.info("📤 Sending ride offer to /topic/driver-offers/{} (rideId={})", driverId, tripId);
            messagingTemplate.convertAndSend(
                    "/topic/driver-offers/" + driverId,
                    offer
            );
        }
    }

    /**
     * Driver කෙනෙක් Ride එක Accept කිරීම (Frontend: /app/accept-ride)
     */
    @MessageMapping("/accept-ride")
    public void acceptRide(@Payload DriverAction action) {
        log.info("🤝 Driver {} attempting to accept ride: {}", action.getDriverId(), action.getRideId());

        // 1. Database එකේ Status Update කිරීම (Atomic Operation)
        boolean success = rideService.assignDriver(action.getRideId(), action.getDriverId());

        if (success) {
            log.info("✅ Ride {} successfully assigned to driver {}", action.getRideId(), action.getDriverId());

            // 2. Driver ගේ විස්තර Auth Service එකෙන් ලබා ගැනීම
            UserDetailsDto driverDetails = userIntegrationService.getDriverDetails(action.getDriverId());
            
            if (driverDetails != null) {
                log.info("📋 Driver details retrieved: name={}, vehicle={}, phone={}", 
                     driverDetails.getFullName(), driverDetails.getVehicleModel(), driverDetails.getPhoneNumber());
            } else {
                log.warn("⚠️ Driver details are NULL for driverId: {}", action.getDriverId());
            }

            // 3. Rider ගේ ID එක Cache එකෙන් ලබා ගැනීම (NO DATABASE LOOKUP!)
            Long riderId = rideToRiderCache.get(action.getRideId());
            
            if (riderId == null) {
                // Cache එකේ නැත්නම් Database එකෙන් try කරන්න (fallback)
                log.warn("⚠️ Ride {} not in cache, trying database lookup", action.getRideId());
                riderId = rideService.getRiderId(action.getRideId());
            }
            
            if (riderId == null) {
                log.error("❌ CRITICAL: Cannot find riderId for ride {}!", action.getRideId());
                return;
            }
            
            log.info("✅ Retrieved riderId {} from cache for ride {}", riderId, action.getRideId());
            
            // 4. Rider ට දැනුම් දීම (DRIVER_FOUND)
            TripUpdate update = new TripUpdate("DRIVER_FOUND", action.getRideId(), driverDetails);
            
            log.info("📤 Sending DRIVER_FOUND to /topic/ride-status/{} with driver details", riderId);
            log.info("📦 Payload: status=DRIVER_FOUND, rideId={}, driverName={}", 
                     action.getRideId(), (driverDetails != null ? driverDetails.getFullName() : "Unknown"));
            
            messagingTemplate.convertAndSend(
                    "/topic/ride-status/" + riderId,
                    update
            );
            
            log.info("✅ DRIVER_FOUND notification sent successfully to rider {}", riderId);

            // 5. Driver ට සාර්ථක බව දැනුම් දීම (Optional)
            messagingTemplate.convertAndSend(
                    "/topic/driver-notify/" + action.getDriverId(),
                    "SUCCESS"
            );
            
            // 6. Clean up cache after successful assignment
            rideToRiderCache.remove(action.getRideId());
            log.info("🗑️ Removed ride {} from cache", action.getRideId());

        } else {
            // වෙනත් Driver කෙනෙක් කලින් ගෙන තිබේ නම්
            log.warn("❌ Ride {} already taken or failed to assign, notifying /topic/driver-notify/{}", action.getRideId(), action.getDriverId());

            messagingTemplate.convertAndSend(
                    "/topic/driver-notify/" + action.getDriverId(),
                    "RIDE_TAKEN"
            );
            
            // Clean up cache even on failure
            rideToRiderCache.remove(action.getRideId());
        }
    }
}
