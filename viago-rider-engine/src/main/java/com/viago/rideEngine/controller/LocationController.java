package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Update driver's real-time location
     */
    @PutMapping("/drivers/{driverId}")
    public ResponseEntity<?> updateDriverLocation(@PathVariable String driverId,
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
     * Update passenger's real-time location
     */
    @PutMapping("/passengers/{passengerId}")
    public ResponseEntity<?> updatePassengerLocation(@PathVariable String passengerId,
                                                      @RequestBody Map<String, Object> location) {
        try {
            Object result = locationService.updatePassengerLocation(passengerId, location);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get driver's current location
     */
    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<?> getDriverLocation(@PathVariable String driverId) {
        try {
            Object location = locationService.getDriverLocation(driverId);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get passenger's current location
     */
    @GetMapping("/passengers/{passengerId}")
    public ResponseEntity<?> getPassengerLocation(@PathVariable String passengerId) {
        try {
            Object location = locationService.getPassengerLocation(passengerId);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get location for a trip (driver and passenger locations)
     */
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<?> getTripLocations(@PathVariable String tripId) {
        try {
            Object locations = locationService.getTripLocations(tripId);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get nearby drivers
     */
    @GetMapping("/drivers/nearby")
    public ResponseEntity<?> getNearbyDrivers(@RequestParam double latitude,
                                              @RequestParam double longitude,
                                              @RequestParam(required = false, defaultValue = "5.0") double radiusKm) {
        try {
            Object drivers = locationService.getNearbyDrivers(latitude, longitude, radiusKm);
            return ResponseEntity.ok(drivers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get location history for a driver
     */
    @GetMapping("/drivers/{driverId}/history")
    public ResponseEntity<?> getDriverLocationHistory(@PathVariable String driverId,
                                                       @RequestParam(required = false) String startTime,
                                                       @RequestParam(required = false) String endTime) {
        try {
            Object history = locationService.getDriverLocationHistory(driverId, startTime, endTime);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Subscribe to location updates (WebSocket endpoint would be separate)
     */
    @PostMapping("/trips/{tripId}/subscribe")
    public ResponseEntity<?> subscribeToLocationUpdates(@PathVariable String tripId,
                                                         @RequestBody Map<String, String> subscription) {
        try {
            Object result = locationService.subscribeToLocationUpdates(tripId, subscription.get("userId"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

