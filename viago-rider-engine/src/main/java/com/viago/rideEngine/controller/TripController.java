package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.TripService;
import com.viago.rideEngine.service.DriverMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;
    private final DriverMatchingService driverMatchingService;

    @Autowired
    public TripController(TripService tripService, DriverMatchingService driverMatchingService) {
        this.tripService = tripService;
        this.driverMatchingService = driverMatchingService;
    }

    /**
     * Create a new trip request
     */
    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody Map<String, Object> tripRequest) {
        try {
            Object trip = tripService.createTrip(tripRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get trip by ID
     */
    @GetMapping("/{tripId}")
    public ResponseEntity<?> getTrip(@PathVariable String tripId) {
        try {
            Object trip = tripService.getTripById(tripId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get all trips for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserTrips(@PathVariable String userId,
                                          @RequestParam(required = false) String status) {
        try {
            Object trips = tripService.getUserTrips(userId, status);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update trip status
     */
    @PatchMapping("/{tripId}/status")
    public ResponseEntity<?> updateTripStatus(@PathVariable String tripId,
                                               @RequestBody Map<String, String> statusUpdate) {
        try {
            Object trip = tripService.updateTripStatus(tripId, statusUpdate.get("status"));
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Cancel a trip
     */
    @PostMapping("/{tripId}/cancel")
    public ResponseEntity<?> cancelTrip(@PathVariable String tripId,
                                         @RequestBody(required = false) Map<String, String> cancelReason) {
        try {
            Object trip = tripService.cancelTrip(tripId, cancelReason != null ? cancelReason.get("reason") : null);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Find and match driver for a trip
     */
    @PostMapping("/{tripId}/match-driver")
    public ResponseEntity<?> matchDriver(@PathVariable String tripId) {
        try {
            Object matchResult = driverMatchingService.matchDriverForTrip(tripId);
            return ResponseEntity.ok(matchResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get active trip for a user
     */
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<?> getActiveTrip(@PathVariable String userId) {
        try {
            Object trip = tripService.getActiveTrip(userId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Complete a trip
     */
    @PostMapping("/{tripId}/complete")
    public ResponseEntity<?> completeTrip(@PathVariable String tripId) {
        try {
            Object trip = tripService.completeTrip(tripId);
            return ResponseEntity.ok(trip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
