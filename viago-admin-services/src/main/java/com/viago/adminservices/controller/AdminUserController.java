package com.viago.adminservices.controller;

import com.viago.adminservices.dto.UserDTO;
import com.viago.adminservices.dto.response.UserResponse;
import com.viago.adminservices.service.AdminUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS })
@Validated
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/test")
    public String test() {
        return "test success: viago-admin-services check pipeline 100";
    }

    @GetMapping
    public ResponseEntity<UserResponse> listUsers(@RequestParam(required = false) String role,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return adminUserService.listUsers(role, page, size);
    }

    @GetMapping("/{email}")
    public HttpEntity<UserResponse<Object>> findByEmail(@PathVariable @Email String email) {
        return adminUserService.findByEmail(email);
    }

    @PutMapping
    public HttpEntity<UserResponse> updateUser(@RequestBody @Valid UserDTO userDTO) {
        return adminUserService.update(userDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseEntity<UserResponse<Object>>> deleteUser(@PathVariable @NotNull Long userId) {
        return ResponseEntity.ok(adminUserService.delete(userId, null));
    }

    @DeleteMapping
    public ResponseEntity<ResponseEntity<UserResponse<Object>>> deleteUserByEmail(
            @RequestParam @NotBlank String email) {
        return ResponseEntity.ok(adminUserService.delete(null, email));
    }

    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<ResponseEntity<UserResponse<Object>>> suspendUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.suspend(userId));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ResponseEntity<UserResponse<Object>>> activateUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.unsuspend(userId));
    }
}
