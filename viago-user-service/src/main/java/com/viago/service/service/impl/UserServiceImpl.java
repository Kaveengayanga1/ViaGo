package com.viago.service.service.impl;

import com.viago.service.dto.request.UserRegistrationDTO;
import com.viago.service.dto.request.UserUpdateDTO;
import com.viago.service.dto.response.UserResponseDTO;
import com.viago.service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponseDTO createUser(UserRegistrationDTO request) {
        // TODO: Implement repository logic
        return UserResponseDTO.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .preferredLanguage(request.getPreferredLanguage())
                .timezone(request.getTimezone())
                .isActive(true)
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .build();
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        // TODO: Implement repository logic to fetch user by ID
        return UserResponseDTO.builder()
                .userId(userId)
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        // TODO: Implement repository logic to fetch user by email
        return UserResponseDTO.builder()
                .email(email)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserUpdateDTO request) {
        // TODO: Implement repository logic to update user
        return UserResponseDTO.builder()
                .userId(userId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .profilePictureUrl(request.getProfilePictureUrl())
                .preferredLanguage(request.getPreferredLanguage())
                .timezone(request.getTimezone())
                .build();
    }

    @Override
    public void deactivateUser(Long userId) {
        // TODO: Implement repository logic to deactivate user
    }

    @Override
    public void activateUser(Long userId) {
        // TODO: Implement repository logic to activate user
    }

    @Override
    public void deleteUser(Long userId) {
        // TODO: Implement repository logic to delete user
    }
}
