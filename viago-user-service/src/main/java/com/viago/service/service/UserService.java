package com.viago.service.service;

import com.viago.service.dto.request.DriverOnboardingRequest;
import com.viago.service.dto.request.SubmitRatingRequest;
import com.viago.service.dto.request.UserUpdateDTO;
import com.viago.service.dto.response.*;

import java.util.List;

public interface UserService {

    UserResponse<UserProfileDTO> getUserProfile(Long userId);

    UserResponse<UserProfileDTO> getUserProfileByEmail(String email);

    UserResponse<UserProfileDTO> updateUserProfile(Long userId, UserUpdateDTO dto);

    UserResponse<UserProfileDTO> onboardDriver(Long userId, DriverOnboardingRequest request);

    UserResponse<UserProfileDTO> approveDriver(Long userId);

    UserResponse<ReviewDTO> submitRating(Long raterUserId, SubmitRatingRequest request);

    UserResponse<List<ReviewDTO>> getUserReviews(Long userId);

    UserResponse<VehicleDTO> getDriverVehicle(Long userId);
}
