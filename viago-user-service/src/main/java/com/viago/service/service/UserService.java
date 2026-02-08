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
    
    // ===== Trip Service Integration Methods =====
    
    /**
     * Get user details for trip service (any role)
     * @param userId the user ID
     * @return TripServiceUserDTO with complete user information
     */
    TripServiceUserDTO getUserForTripService(Long userId);
    
    /**
     * Get driver details for trip service (drivers only)
     * @param driverId the driver's user ID
     * @return TripServiceUserDTO with driver information
     */
    TripServiceUserDTO getDriverForTripService(Long driverId);
    
    /**
     * Get rider details for trip service (riders only)
     * @param riderId the rider's user ID
     * @return TripServiceUserDTO with rider information
     */
    TripServiceUserDTO getRiderForTripService(Long riderId);
    
    /**
     * Get user role for validation
     * @param userId the user ID
     * @return UserRoleResponseDTO with role and status information
     */
    UserRoleResponseDTO getUserRole(Long userId);
    
    /**
     * Get multiple users in batch
     * @param userIds list of user IDs to fetch
     * @return List of TripServiceUserDTO
     */
    List<TripServiceUserDTO> getUsersBatch(List<Long> userIds);
}
