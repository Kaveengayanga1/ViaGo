package com.viago.adminservices.service;

import com.viago.adminservices.client.UserInterface;
import com.viago.adminservices.dto.UserDTO;
import com.viago.adminservices.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserInterface authServiceClient;

    public ResponseEntity<UserResponse> listUsers(String role, Integer page, Integer size) {
        if (StringUtils.hasText(role)) {
            return authServiceClient.getUserListByRole(role, page, size);
        }
        // When no role is specified, get all users
        // return authServiceClient.getAllUsers(page, size);
        return null;
    }

    public ResponseEntity<UserResponse<Object>> findByEmail(String email) {
        return authServiceClient.getUserByEmail(email);
    }

    public ResponseEntity<UserResponse> update(UserDTO userDTO) {
        return authServiceClient.updateUser(userDTO);
    }

    public ResponseEntity<UserResponse<Object>> delete(Long userId, String email) {
        return authServiceClient.deleteUser(userId, email);
    }

    public ResponseEntity<UserResponse<Object>> suspend(Long userId) {
        return authServiceClient.updateUserStatus(userId, false);
    }

    public ResponseEntity<UserResponse<Object>> unsuspend(Long userId) {
        return authServiceClient.updateUserStatus(userId, true);
    }
}
