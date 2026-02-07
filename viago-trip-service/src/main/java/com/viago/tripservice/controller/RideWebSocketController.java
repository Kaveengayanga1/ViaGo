package com.viago.tripservice.controller;


import com.viago.tripservice.dto.*;
import com.viago.tripservice.model.Ride;
import com.viago.tripservice.service.DriverMatchingService;
import com.viago.tripservice.service.RideManagementService;
import com.viago.tripservice.service.UserIntegrationService;
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
    private final RideManagementService rideService;
    private final DriverMatchingService matchingService;
    private final UserIntegrationService userIntegrationService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/driver-update")
    public void updateDriverLocation(@Payload LocationUpdateDto loc) {
        matchingService.updateLocation(loc.getDriverId(), loc.getLat(), loc.getLng());
    }

    @MessageMapping("/request-ride")
    public void requestRide(@Payload RideRequest request) {

        Ride ride = rideService.createRide(request);
        Long tripId = ride.getRideId();

        // B. Rider ට Trip ID එක යවනවා ("Searching Drivers...")
        messagingTemplate.convertAndSendToUser(
                request.getRiderId().toString(),
                "/queue/ride-status",
                new TripUpdate("SEARCHING", tripId, null)
        );

        // C. Drivers ලා සොයනවා
        List<Long> nearbyDrivers = matchingService.findNearbyDrivers(request.getPickupLat(), request.getPickupLng());

        // D. Drivers ලාට Offer එක යවනවා (Trip ID එකත් එක්කම!)
        RideOffer offer = new RideOffer(tripId, request.getPickupAddress(), request.getPrice());
        for (Long driverId : nearbyDrivers) {
            messagingTemplate.convertAndSendToUser(
                    driverId.toString(),
                    "/queue/driver-offers",
                    offer
            );
        }
    }

@MessageMapping("/accept-ride")

public void acceptRide(@Payload DriverAction action) {
    // A. Ride එක තාම Free ද කියලා බලලා Assign කරනවා (Atomic Operation)
    boolean success = rideService.assignDriver(action.getRideId(), action.getDriverId());

    if (success) {
        // B. Auth Service එකෙන් Driver ගේ විස්තර (නම, වාහනය) ගන්නවා
        UserDetailsDto driverDetails = userIntegrationService.getDriverDetails(action.getDriverId());

        // C. Rider ට Driver ගේ විස්තර යවනවා
        messagingTemplate.convertAndSendToUser(
                rideService.getRiderId(action.getRideId()).toString(),
                "/queue/ride-status",
                new TripUpdate("DRIVER_FOUND", action.getRideId(), driverDetails)
        );
    } else {
        // පරක්කු වැඩි! වෙන කෙනෙක් ගත්තා
        messagingTemplate.convertAndSendToUser(
                action.getDriverId().toString(),
                "/queue/driver-notify",
                "RIDE_TAKEN"
        );
    }
}


}
