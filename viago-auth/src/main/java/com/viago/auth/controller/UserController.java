package com.viago.auth.controller;

import com.viago.auth.dto.UserDTO;
import com.viago.auth.dto.response.UserResponse;
import com.viago.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
//Done
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserResponse<Object>> deleteUser(@RequestParam(required = false) Long userId,
                                                           @RequestParam(required = false) String email) {
        // Support deletion by userId or email
        if (userId != null) {
            return userService.removeUser(userId);
        } else if (email != null && !email.trim().isEmpty()) {
            return userService.removeUserByEmail(email);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserResponse.builder()
                            .success(false)
                            .message("user_id_or_email_required")
                            .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @PatchMapping("/status")
    public ResponseEntity<UserResponse<Object>> updateUserStatus(@RequestParam Long userId,
                                                                 @RequestParam boolean enabled) {
        return userService.updateUserStatus(userId, enabled);
    }
    //for testing purpose
    @GetMapping("/hello")
    public String sayHello(){
        Date date = new Date(new java.util.Date().getTime());
        return "Hello "+date;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<UserResponse> getAllUsers(@RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size){
        return userService.getAllUsers(page, size);
    }
    @GetMapping("/get-user-by-email")
    public ResponseEntity<UserResponse<Object>> getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }
    @GetMapping("/get-user-by-role")
    public ResponseEntity<UserResponse> getUserListByRole(@RequestParam String role,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size){
        log.info("Get user by role request - role: {}, page: {}, size: {}", role, page, size);
        return userService.getUserListByRole(role, page, size);
    }

}
