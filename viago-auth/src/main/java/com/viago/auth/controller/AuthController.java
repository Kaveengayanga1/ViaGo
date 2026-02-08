package com.viago.auth.controller;

import com.viago.auth.dto.UserDTO;
import com.viago.auth.dto.request.LoginRequest;
import com.viago.auth.dto.response.AuthResponse;
import com.viago.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
//Done
@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public AuthResponse signUpUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    }

    @PostMapping("/login")
    public AuthResponse loginUser(@RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }
    @GetMapping
    public String hello(){
        return "hello auth pipeline";
    }

    @GetMapping("/google/login")
    public void googleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Store flow type in session for routing handler
        HttpSession session = request.getSession();
        session.setAttribute("oauth2_flow", "login");
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/google/signup")
    public void googleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Store flow type in session for routing handler
        HttpSession session = request.getSession();
        session.setAttribute("oauth2_flow", "signup");
        response.sendRedirect("/oauth2/authorization/google");
    }

}
