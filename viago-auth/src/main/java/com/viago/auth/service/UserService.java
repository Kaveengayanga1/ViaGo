package com.viago.auth.service;

import com.viago.auth.dto.UserDTO;
import com.viago.auth.dto.request.LoginRequest;
import com.viago.auth.dto.response.AuthResponse;
import com.viago.auth.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public AuthResponse addUser(UserDTO userDTO);
    public AuthResponse loginUser(LoginRequest loginRequest);
    public ResponseEntity<UserResponse<Object>> removeUser(Long userId);
    public ResponseEntity<UserResponse> updateUser(UserDTO userDTO);
    public ResponseEntity<UserResponse> getAllUsers();
    public ResponseEntity<UserResponse<Object>> getUserByEmail(String email);
    public ResponseEntity<UserResponse> getUserListByRole(String role);
}
