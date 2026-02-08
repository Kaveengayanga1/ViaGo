package com.viago.service.controller;

import com.viago.service.dto.request.BatchUserRequestDTO;
import com.viago.service.dto.request.UserUpdateDTO;
import com.viago.service.dto.response.*;
import com.viago.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ===== Existing Endpoints =====

    @GetMapping("/profile/{userId}")
    public UserResponse<UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

    @GetMapping("/email/{email}")
    public UserResponse<UserProfileDTO> getUserProfileByEmail(@PathVariable String email) {
        return userService.getUserProfileByEmail(email);
    }

    @PutMapping("/{userId}")
    public UserResponse<UserProfileDTO> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDTO dto) {
        return userService.updateUserProfile(userId, dto);
    }

    @GetMapping("/{userId}/reviews")
    public UserResponse<List<ReviewDTO>> getUserReviews(@PathVariable Long userId) {
        return userService.getUserReviews(userId);
    }

    @GetMapping("/{userId}/vehicle")
    public UserResponse<VehicleDTO> getDriverVehicle(@PathVariable Long userId) {
        return userService.getDriverVehicle(userId);
    }

    // ===== Trip Service Integration Endpoints =====

    /**
     * Get user details for trip service (any role)
     * Endpoint: GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<TripServiceUserDTO> getUserById(@PathVariable Long userId) {
        TripServiceUserDTO user = userService.getUserForTripService(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Get driver details for trip service
     * Endpoint: GET /api/users/drivers/{driverId}
     */
    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<TripServiceUserDTO> getDriverById(@PathVariable Long driverId) {
        TripServiceUserDTO driver = userService.getDriverForTripService(driverId);
        return ResponseEntity.ok(driver);
    }

    /**
     * Get rider details for trip service
     * Endpoint: GET /api/users/riders/{riderId}
     */
    @GetMapping("/riders/{riderId}")
    public ResponseEntity<TripServiceUserDTO> getRiderById(@PathVariable Long riderId) {
        TripServiceUserDTO rider = userService.getRiderForTripService(riderId);
        return ResponseEntity.ok(rider);
    }

    /**
     * Get user role for validation
     * Endpoint: GET /api/users/{userId}/role
     */
    @GetMapping("/{userId}/role")
    public ResponseEntity<UserRoleResponseDTO> getUserRole(@PathVariable Long userId) {
        UserRoleResponseDTO roleResponse = userService.getUserRole(userId);
        return ResponseEntity.ok(roleResponse);
    }

    /**
     * Batch get users
     * Endpoint: POST /api/users/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<BatchUserResponseDTO> getUsersBatch(@Valid @RequestBody BatchUserRequestDTO request) {
        List<TripServiceUserDTO> users = userService.getUsersBatch(request.getUserIds());
        
        BatchUserResponseDTO response = BatchUserResponseDTO.builder()
                .users(users)
                .totalCount(users.size())
                .build();
        
        return ResponseEntity.ok(response);
    }
}
