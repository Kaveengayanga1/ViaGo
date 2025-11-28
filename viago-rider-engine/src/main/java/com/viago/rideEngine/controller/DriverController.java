package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.DriverMatchingService;
import com.viago.rideEngine.service.TripService;
import com.viago.rideEngine.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverMatchingService driverMatchingService;
    private final TripService tripService;
    private final LocationService locationService;

    @Autowired
    public DriverController(DriverMatchingService driverMatchingService,
                           TripService tripService,
                           LocationService locationService) {
        this.driverMatchingService = driverMatchingService;
        this.tripService = tripService;
        this.locationService = locationService;
    }

    /**
     * Set driver availability status
     */
    @PostMapping("/{driverId}/availability")
    public ResponseEntity<?> setAvailability(@PathVariable String driverId,
                                              @RequestBody Map<String, Boolean> availability) {
        try {
            Object result = driverMatchingService.setDriverAvailability(driverId, availability.get("available"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Accept a trip request
     */
    @PostMapping("/{driverId}/trips/{tripId}/accept")
    public ResponseEntity<?> acceptTrip(@PathVariable String driverId,
                                        @PathVariable String tripId) {
        try {
            Object trip = driverMatchingService.acceptTrip(driverId, tripId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Reject a trip request
     */
    @PostMapping("/{driverId}/trips/{tripId}/reject")
    public ResponseEntity<?> rejectTrip(@PathVariable String driverId,
                                        @PathVariable String tripId,
                                        @RequestBody(required = false) Map<String, String> reason) {
        try {
            Object result = driverMatchingService.rejectTrip(driverId, tripId, 
                    reason != null ? reason.get("reason") : null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Start a trip (driver picks up passenger)
     */
    @PostMapping("/{driverId}/trips/{tripId}/start")
    public ResponseEntity<?> startTrip(@PathVariable String driverId,
                                       @PathVariable String tripId) {
        try {
            Object trip = tripService.startTrip(tripId, driverId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Complete a trip (driver drops off passenger)
     */
    @PostMapping("/{driverId}/trips/{tripId}/complete")
    public ResponseEntity<?> completeTrip(@PathVariable String driverId,
                                           @PathVariable String tripId) {
        try {
            Object trip = tripService.completeTrip(tripId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get driver's active trip
     */
    @GetMapping("/{driverId}/trips/active")
    public ResponseEntity<?> getActiveTrip(@PathVariable String driverId) {
        try {
            Object trip = tripService.getDriverActiveTrip(driverId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get driver's trip history
     */
    @GetMapping("/{driverId}/trips")
    public ResponseEntity<?> getDriverTrips(@PathVariable String driverId,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            Object trips = tripService.getDriverTrips(driverId, status, page, size);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update driver's current location
     */
    @PutMapping("/{driverId}/location")
    public ResponseEntity<?> updateLocation(@PathVariable String driverId,
                                            @RequestBody Map<String, Object> location) {
        try {
            Object result = locationService.updateDriverLocation(driverId, location);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get nearby trip requests for driver
     */
    @GetMapping("/{driverId}/nearby-trips")
    public ResponseEntity<?> getNearbyTrips(@PathVariable String driverId,
                                             @RequestParam(required = false, defaultValue = "5.0") double radiusKm) {
        try {
            Object trips = driverMatchingService.getNearbyTripRequests(driverId, radiusKm);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

