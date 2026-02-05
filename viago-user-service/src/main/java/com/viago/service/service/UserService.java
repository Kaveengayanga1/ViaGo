package com.viago.service.service;

import com.viago.service.dto.request.UserRegistrationDTO;
import com.viago.service.dto.request.UserUpdateDTO;
import com.viago.service.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRegistrationDTO request);

    UserResponseDTO getUserById(Long userId);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO updateUser(Long userId, UserUpdateDTO request);

    void deactivateUser(Long userId);

    void activateUser(Long userId);

    void deleteUser(Long userId);
}
