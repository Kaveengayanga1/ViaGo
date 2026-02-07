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

        Ride ride = rideService.createRide(request);
        Long tripId = ride.getRideId();


        messagingTemplate.convertAndSendToUser(
                request.getRiderId().toString(),
                "/queue/ride-status",
                new TripUpdate("SEARCHING", tripId, null)
        );


        List<Long> nearbyDrivers = matchingService.findNearbyDrivers(request.getPickupLat(), request.getPickupLng());

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
            messagingTemplate.convertAndSendToUser(
                    driverId.toString(),
                    "/queue/driver-offers",
                    offer
            );
        }
    }

@MessageMapping("/accept-ride")

public void acceptRide(@Payload DriverAction action) {

    boolean success = rideService.assignDriver(action.getRideId(), action.getDriverId());

    if (success) {

        UserDetailsDto driverDetails = userIntegrationService.getDriverDetails(action.getDriverId());


        messagingTemplate.convertAndSendToUser(
                rideService.getRiderId(action.getRideId()).toString(),
                "/queue/ride-status",
                new TripUpdate("DRIVER_FOUND", action.getRideId(), driverDetails)
        );
    } else {

        messagingTemplate.convertAndSendToUser(
                action.getDriverId().toString(),
                "/queue/driver-notify",
                "RIDE_TAKEN"
        );
    }
}


}
