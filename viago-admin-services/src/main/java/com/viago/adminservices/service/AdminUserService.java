package com.viago.adminservices.service;

import com.viago.adminservices.client.AuthServiceClient;
import com.viago.adminservices.dto.UserDTO;
import com.viago.adminservices.dto.response.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AuthServiceClient authServiceClient;

    public UserResponse<List<UserDTO>> listUsers(String role, Integer page, Integer size) {
        if (StringUtils.hasText(role)) {
            return authServiceClient.getUsersByRole(role, page, size);
        }
        return authServiceClient.getUsers(page, size);
    }

    public UserResponse<UserDTO> findByEmail(String email) {
        return authServiceClient.getUserByEmail(email);
    }

    public UserResponse<UserDTO> update(UserDTO userDTO) {
        return authServiceClient.updateUser(userDTO);
    }

    public UserResponse<?> delete(Long userId, String email) {
        return authServiceClient.deleteUser(userId, email);
    }

    public UserResponse<?> suspend(Long userId) {
        return authServiceClient.updateUserStatus(userId, false);
    }

    public UserResponse<?> unsuspend(Long userId) {
        return authServiceClient.updateUserStatus(userId, true);
    }
}

