package com.viago.adminservices.client;

import com.viago.adminservices.config.FeignClientConfig;
import com.viago.adminservices.dto.UserDTO;
import com.viago.adminservices.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "viago-auth-service", configuration = FeignClientConfig.class)
public interface UserInterface {

    // 1. DELETE User
    // Mapping: DELETE /user/delete?userId=...&email=...
    @DeleteMapping("/user/delete")
    ResponseEntity<UserResponse<Object>> deleteUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "email", required = false) String email
    );

    // 2. UPDATE User
    // Mapping: PUT /user/update
    @PutMapping("/user/update")
    ResponseEntity<UserResponse> updateUser(@RequestBody UserDTO userDTO);

    // 3. UPDATE Status (Enable/Disable)
    // Mapping: PATCH /user/status?userId=...&enabled=...
    @PatchMapping("/user/status")
    ResponseEntity<UserResponse<Object>> updateUserStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("enabled") boolean enabled
    );

    // 4. Test Endpoint
    // Mapping: GET /user/hello
    @GetMapping("/user/hello")
    String sayHello();

    // 5. GET All Users (Pagination)
    // Mapping: GET /user/get-all-users?page=...&size=...
    @GetMapping("/user/get-all-users")
    ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    );

    // 6. GET User by Email
    // Mapping: GET /user/get-user-by-email?email=...
    @GetMapping("/user/get-user-by-email")
    ResponseEntity<UserResponse<Object>> getUserByEmail(@RequestParam("email") String email);

    // 7. GET Users by Role (Used for filtering RIDERS or DRIVERS)
    // Mapping: GET /user/get-user-by-role?role=...&page=...&size=...
    @GetMapping("/user/get-user-by-role")
    ResponseEntity<UserResponse> getUserListByRole(
            @RequestParam("role") String role,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    );
}
