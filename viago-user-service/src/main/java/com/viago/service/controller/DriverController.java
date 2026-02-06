package com.viago.service.controller;

import com.viago.service.dto.request.DriverOnboardingRequest;
import com.viago.service.dto.response.UserProfileDTO;
import com.viago.service.dto.response.UserResponse;
import com.viago.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@CrossOrigin
@RequiredArgsConstructor
public class DriverController {

    private final UserService userService;

    @PostMapping("/{userId}/onboard")
    public UserResponse<UserProfileDTO> onboardDriver(
            @PathVariable Long userId,
            @Valid @RequestBody DriverOnboardingRequest request) {
        return userService.onboardDriver(userId, request);
    }

    @PutMapping("/{userId}/approve")
    public UserResponse<UserProfileDTO> approveDriver(@PathVariable Long userId) {
        return userService.approveDriver(userId);
    }
}
