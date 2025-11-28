package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.UserService;
import com.viago.rideEngine.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TripService tripService;

    @Autowired
    public UserController(UserService userService, TripService tripService) {
        this.userService = userService;
        this.tripService = tripService;
    }

    /**
     * Get user profile
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
        try {
            Object profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update user profile
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable String userId,
                                               @RequestBody Map<String, Object> profileData) {
        try {
            Object profile = userService.updateUserProfile(userId, profileData);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user information
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
        try {
            Object user = userService.getUserInfo(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user's trip history
     */
    @GetMapping("/{userId}/trips")
    public ResponseEntity<?> getUserTrips(@PathVariable String userId,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            Object trips = tripService.getUserTrips(userId, status, page, size);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user statistics
     */
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<?> getUserStatistics(@PathVariable String userId) {
        try {
            Object stats = userService.getUserStatistics(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update user preferences
     */
    @PutMapping("/{userId}/preferences")
    public ResponseEntity<?> updateUserPreferences(@PathVariable String userId,
                                                   @RequestBody Map<String, Object> preferences) {
        try {
            Object result = userService.updateUserPreferences(userId, preferences);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user preferences
     */
    @GetMapping("/{userId}/preferences")
    public ResponseEntity<?> getUserPreferences(@PathVariable String userId) {
        try {
            Object preferences = userService.getUserPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user's saved addresses
     */
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<?> getSavedAddresses(@PathVariable String userId) {
        try {
            Object addresses = userService.getSavedAddresses(userId);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Add saved address
     */
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<?> addSavedAddress(@PathVariable String userId,
                                             @RequestBody Map<String, Object> address) {
        try {
            Object result = userService.addSavedAddress(userId, address);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete saved address
     */
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<?> deleteSavedAddress(@PathVariable String userId,
                                                @PathVariable String addressId) {
        try {
            userService.deleteSavedAddress(userId, addressId);
            return ResponseEntity.ok(Map.of("message", "Address deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user's active trip
     */
    @GetMapping("/{userId}/trips/active")
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
     * Update user's profile picture
     */
    @PutMapping("/{userId}/profile/picture")
    public ResponseEntity<?> updateProfilePicture(@PathVariable String userId,
                                                  @RequestBody Map<String, String> pictureData) {
        try {
            Object result = userService.updateProfilePicture(userId, pictureData.get("pictureUrl"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

