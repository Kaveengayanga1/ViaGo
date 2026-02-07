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

@RestController
@CrossOrigin
@RequestMapping("api/trips")
public class RideWebSocketController {
    private static final Logger log = LoggerFactory.getLogger(RideWebSocketController.class);
    
    private final RideManagementService rideService;
    private final DriverMatchingService matchingService;
    private final UserIntegrationService userIntegrationService;
    private final SimpMessagingTemplate messagingTemplate;

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
        log.info("📍 Driver location updated: driverId={}, lat={}, lng={}", 
                 loc.getDriverId(), loc.getLat(), loc.getLng());
        matchingService.updateLocation(loc.getDriverId(), loc.getLat(), loc.getLng());
    }

    @MessageMapping("/request-ride")
    public void requestRide(@Payload RideRequest request) {
        log.info("🚕 Ride request received from rider: {}", request.getRiderId());

        Ride ride = rideService.createRide(request);
        Long tripId = ride.getRideId();
        log.info("✅ Ride created successfully: rideId={}", tripId);


        messagingTemplate.convertAndSend(
                "/topic/ride-status/" + request.getRiderId(),
                new TripUpdate("SEARCHING", tripId, null)
        );
        log.info("📤 Sending SEARCHING status to /topic/ride-status/{}", request.getRiderId());


        log.info("🔍 Finding nearby drivers for location: lat={}, lng={}", 
                 request.getPickupLat(), request.getPickupLng());
        List<Long> nearbyDrivers = matchingService.findNearbyDrivers(request.getPickupLat(), request.getPickupLng());
        log.info("✅ Found {} nearby driver(s): {}", nearbyDrivers.size(), nearbyDrivers);

        if (nearbyDrivers.isEmpty()) {
            log.warn("⚠️ No nearby drivers found for ride: {}", tripId);
        }

        // D. Drivers ලාට Offer එක යවනවා (Trip ID එකත් එක්කම!)
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

@MessageMapping("/accept-ride")

public void acceptRide(@Payload DriverAction action) {
    log.info("🤝 Driver {} attempting to accept ride: {}", action.getDriverId(), action.getRideId());

    boolean success = rideService.assignDriver(action.getRideId(), action.getDriverId());

    if (success) {
        log.info("✅ Ride {} successfully assigned to driver {}", action.getRideId(), action.getDriverId());

        // Get driver details from user service
        UserDetailsDto driverDetails = userIntegrationService.getDriverDetails(action.getDriverId());
        log.info("📋 Driver details retrieved: name={}, vehicle={}, phone={}", 
                 driverDetails.getFullName(), driverDetails.getVehicleModel(), driverDetails.getPhoneNumber());

        // Get rider ID
        Long riderId = rideService.getRiderId(action.getRideId());
        
        // Create trip update with driver details
        TripUpdate update = new TripUpdate("DRIVER_FOUND", action.getRideId(), driverDetails);
        
        log.info("📤 Sending DRIVER_FOUND to /topic/ride-status/{} with driver details", riderId);
        log.info("📦 Payload: status=DRIVER_FOUND, rideId={}, driverName={}", 
                 action.getRideId(), driverDetails.getFullName());
        
        messagingTemplate.convertAndSend(
                "/topic/ride-status/" + riderId,
                update
        );
        
        log.info("✅ DRIVER_FOUND notification sent successfully to rider {}", riderId);
    } else {
        log.warn("❌ Ride {} already taken, notifying /topic/driver-notify/{}", action.getRideId(), action.getDriverId());

        messagingTemplate.convertAndSend(
                "/topic/driver-notify/" + action.getDriverId(),
                "RIDE_TAKEN"
        );
    }
}


}
