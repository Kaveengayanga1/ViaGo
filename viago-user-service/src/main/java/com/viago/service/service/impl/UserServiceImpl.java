package com.viago.service.service.impl;

import com.viago.service.dto.DriverStatus;
import com.viago.service.dto.Role;
import com.viago.service.dto.request.DriverOnboardingRequest;
import com.viago.service.dto.request.SubmitRatingRequest;
import com.viago.service.dto.request.UserUpdateDTO;
import com.viago.service.dto.response.*;
import com.viago.service.entity.ReviewEntity;
import com.viago.service.entity.UserEntity;
import com.viago.service.entity.VehicleEntity;
import com.viago.service.exception.BadRequestException;
import com.viago.service.exception.ResourceNotFoundException;
import com.viago.service.repository.ReviewRepository;
import com.viago.service.repository.UserRepository;
import com.viago.service.repository.VehicleRepository;
import com.viago.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public UserResponse<UserProfileDTO> getUserProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserProfileDTO profileDTO = mapToUserProfileDTO(user);

        return UserResponse.<UserProfileDTO>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(profileDTO)
                .build();
    }

    @Override
    public UserResponse<UserProfileDTO> getUserProfileByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        UserProfileDTO profileDTO = mapToUserProfileDTO(user);

        return UserResponse.<UserProfileDTO>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(profileDTO)
                .build();
    }

    @Override
    @Transactional
    public UserResponse<UserProfileDTO> updateUserProfile(Long userId, UserUpdateDTO dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        UserEntity updatedUser = userRepository.save(user);
        UserProfileDTO profileDTO = mapToUserProfileDTO(updatedUser);

        return UserResponse.<UserProfileDTO>builder()
                .success(true)
                .message("User profile updated successfully")
                .data(profileDTO)
                .build();
    }

    @Override
    @Transactional
    public UserResponse<UserProfileDTO> onboardDriver(Long userId, DriverOnboardingRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if user is already a driver
        if (user.getRole() == Role.DRIVER) {
            throw new BadRequestException("User is already registered as a driver");
        }

        // Check if plate number already exists
        if (vehicleRepository.findByPlateNumber(request.getPlateNumber()).isPresent()) {
            throw new BadRequestException("Vehicle with plate number " + request.getPlateNumber() + " already exists");
        }

        // Update user role and status
        user.setRole(Role.DRIVER);
        user.setDriverStatus(DriverStatus.PENDING);
        userRepository.save(user);

        // Create vehicle
        VehicleEntity vehicle = VehicleEntity.builder()
                .model(request.getModel())
                .plateNumber(request.getPlateNumber())
                .color(request.getColor())
                .user(user)
                .build();
        vehicleRepository.save(vehicle);

        log.info("Driver onboarding initiated for user: {}", userId);

        UserProfileDTO profileDTO = mapToUserProfileDTO(user);

        return UserResponse.<UserProfileDTO>builder()
                .success(true)
                .message("Driver onboarding request submitted successfully. Status: PENDING")
                .data(profileDTO)
                .build();
    }

    @Override
    @Transactional
    public UserResponse<UserProfileDTO> approveDriver(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getRole() != Role.DRIVER) {
            throw new BadRequestException("User is not registered as a driver");
        }

        if (user.getDriverStatus() == DriverStatus.APPROVED) {
            throw new BadRequestException("Driver is already approved");
        }

        user.setDriverStatus(DriverStatus.APPROVED);
        UserEntity updatedUser = userRepository.save(user);

        log.info("Driver approved: {}", userId);

        UserProfileDTO profileDTO = mapToUserProfileDTO(updatedUser);

        return UserResponse.<UserProfileDTO>builder()
                .success(true)
                .message("Driver approved successfully")
                .data(profileDTO)
                .build();
    }

    @Override
    @Transactional
    public UserResponse<ReviewDTO> submitRating(Long raterUserId, SubmitRatingRequest request) {
        // Verify both users exist
        UserEntity rater = userRepository.findById(raterUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Rater user not found with id: " + raterUserId));

        UserEntity ratedUser = userRepository.findById(request.getRatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rated user not found with id: " + request.getRatedUserId()));

        if (raterUserId.equals(request.getRatedUserId())) {
            throw new BadRequestException("User cannot rate themselves");
        }

        // Create review
        ReviewEntity review = ReviewEntity.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .raterUserId(raterUserId)
                .ratedUserId(request.getRatedUserId())
                .build();
        ReviewEntity savedReview = reviewRepository.save(review);

        // Update user's average rating
        updateUserAverageRating(ratedUser);

        log.info("Rating submitted by user {} for user {}", raterUserId, request.getRatedUserId());

        ReviewDTO reviewDTO = mapToReviewDTO(savedReview);

        return UserResponse.<ReviewDTO>builder()
                .success(true)
                .message("Rating submitted successfully")
                .data(reviewDTO)
                .build();
    }

    @Override
    public UserResponse<List<ReviewDTO>> getUserReviews(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<ReviewEntity> reviews = reviewRepository.findByRatedUserId(userId);
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(this::mapToReviewDTO)
                .collect(Collectors.toList());

        return UserResponse.<List<ReviewDTO>>builder()
                .success(true)
                .message("Reviews retrieved successfully")
                .data(reviewDTOs)
                .build();
    }

    @Override
    public UserResponse<VehicleDTO> getDriverVehicle(Long userId) {
        VehicleEntity vehicle = vehicleRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No vehicle found for user: " + userId));

        VehicleDTO vehicleDTO = mapToVehicleDTO(vehicle);

        return UserResponse.<VehicleDTO>builder()
                .success(true)
                .message("Vehicle retrieved successfully")
                .data(vehicleDTO)
                .build();
    }

    // Helper methods
    private void updateUserAverageRating(UserEntity user) {
        List<ReviewEntity> reviews = reviewRepository.findByRatedUserId(user.getUserId());

        if (!reviews.isEmpty()) {
            double average = reviews.stream()
                    .mapToInt(ReviewEntity::getRating)
                    .average()
                    .orElse(0.0);

            user.setAverageRating(Math.round(average * 100.0) / 100.0); // Round to 2 decimal places
            user.setTotalRatings(reviews.size());
            userRepository.save(user);
        }
    }

    private UserProfileDTO mapToUserProfileDTO(UserEntity user) {
        // Combine firstName and lastName for name, or use firstName if lastName is null
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") +
                (user.getLastName() != null ? " " + user.getLastName() : "").trim();
        if (fullName.isEmpty()) {
            fullName = user.getEmail() != null ? user.getEmail().split("@")[0] : "User";
        }

        return UserProfileDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .name(fullName)
                .phone(user.getPhoneNumber())
                .role(user.getRole())
                .driverStatus(user.getDriverStatus())
                .averageRating(user.getAverageRating())
                .totalRatings(user.getTotalRatings())
                .build();
    }

    private ReviewDTO mapToReviewDTO(ReviewEntity review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .raterUserId(review.getRaterUserId())
                .ratedUserId(review.getRatedUserId())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private VehicleDTO mapToVehicleDTO(VehicleEntity vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .model(vehicle.getModel())
                .plateNumber(vehicle.getPlateNumber())
                .color(vehicle.getColor())
                .userId(vehicle.getUser().getUserId())
                .build();
    }
}
