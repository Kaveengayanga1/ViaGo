package com.viago.adminservices.controller;

import com.viago.adminservices.dto.UserDTO;
import com.viago.adminservices.dto.response.UserResponse;
import com.viago.adminservices.service.AdminUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@Validated
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<UserResponse<List<UserDTO>>> listUsers(@RequestParam(required = false) String role,
                                                                 @RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(adminUserService.listUsers(role, page, size));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse<UserDTO>> findByEmail(@PathVariable @Email String email) {
        return ResponseEntity.ok(adminUserService.findByEmail(email));
    }

    @PutMapping
    public ResponseEntity<UserResponse<UserDTO>> updateUser(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(adminUserService.update(userDTO));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponse<?>> deleteUser(@PathVariable @NotNull Long userId) {
        return ResponseEntity.ok(adminUserService.delete(userId, null));
    }

    @DeleteMapping
    public ResponseEntity<UserResponse<?>> deleteUserByEmail(@RequestParam @NotBlank String email) {
        return ResponseEntity.ok(adminUserService.delete(null, email));
    }

    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<UserResponse<?>> suspendUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.suspend(userId));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<UserResponse<?>> activateUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.unsuspend(userId));
    }
}

