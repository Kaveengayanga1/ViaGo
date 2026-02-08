package com.viago.service.controller;

import com.viago.service.dto.request.UserUpdateDTO;
import com.viago.service.dto.response.ReviewDTO;
import com.viago.service.dto.response.UserProfileDTO;
import com.viago.service.dto.response.UserResponse;
import com.viago.service.dto.response.VehicleDTO;
import com.viago.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
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
}
