package com.viago.auth.controller;

import com.viago.auth.dto.UserDTO;
import com.viago.auth.dto.response.UserResponse;
import com.viago.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<UserResponse<Object>> deleteUser(@RequestParam Long userId) {
        return userService.removeUser(userId);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }
    //for testing purpose
    @GetMapping("/hello")
    public String sayHello(){
        Date date = new Date(new java.util.Date().getTime());
        return "Hello "+date;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/get-user-by-email")
    public ResponseEntity<UserResponse<Object>> getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }
    @GetMapping("/get-user-by-role")
    public ResponseEntity<UserResponse> getUserListByRole(@RequestParam String role){
        log.info("controller passed");
        return userService.getUserListByRole(role);

    }

}
