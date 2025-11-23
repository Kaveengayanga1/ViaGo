package com.viago.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viago.auth.dto.Role;
import com.viago.auth.dto.UserDTO;
import com.viago.auth.dto.response.AuthResponse;
import com.viago.auth.entity.UserEntity;
import com.viago.auth.repository.UserRepository;
import com.viago.auth.service.impl.JwtServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;

@Component
@Slf4j
public class OAuth2SignupSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public OAuth2SignupSuccessHandler(JwtServiceImpl jwtService, 
                                      UserRepository userRepository,
                                      ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response, 
                                       Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub"); // Google's user ID
        
        if (email == null || email.isEmpty()) {
            log.warn("OAuth2 signup failed: email is missing");
            handleFailure(response, "oauth2_email_missing", "Email not found in OAuth2 user");
            return;
        }
        
        // Check if user already exists
        Optional<UserEntity> existingUser = userRepository.findByEmailIgnoreCase(email);
        
        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            // If user exists but doesn't have Google provider, link it
            if (user.getProvider() == null || !user.getProvider().equals("google")) {
                log.info("Linking Google OAuth to existing user: {}", email);
                user.setProvider("google");
                user.setProviderId(providerId);
                userRepository.save(user);
                
                // Generate JWT and return
                UserDTO userDTO = objectMapper.convertValue(user, UserDTO.class);
                userDTO.setPassword(null);
                String jwtToken = jwtService.generateJwtToken(userDTO);
                
                AuthResponse authResponse = AuthResponse.builder()
                        .success(true)
                        .message("oauth2_account_linked")
                        .jwtToken(jwtToken)
                        .refreshToken(null)
                        .tokenType("Bearer")
                        .expiresIn(3600L)
                        .timestamp(OffsetDateTime.now())
                        .data(userDTO)
                        .build();
                
                sendResponse(response, authResponse);
                return;
            } else {
                log.warn("OAuth2 signup failed: user already exists with Google provider: {}", email);
                handleFailure(response, "oauth2_user_exists", "User already exists. Please login instead.");
                return;
            }
        }
        
        // Generate username from name or email
        String username = generateUsername(email, name);
        
        // Create new user with RIDER role
        UserEntity newUser = UserEntity.builder()
                .email(email)
                .username(username)
                .password(null) // OAuth2 users don't have passwords
                .role(Role.RIDER) // Only RIDER role for OAuth2 users
                .provider("google")
                .providerId(providerId)
                .enabled(true)
                .build();
        
        userRepository.save(newUser);
        log.info("OAuth2 signup successful: created new user with email={} | username={} | role=RIDER", 
                email, username);
        
        // Convert to DTO and generate JWT
        UserDTO userDTO = objectMapper.convertValue(newUser, UserDTO.class);
        userDTO.setPassword(null);
        
        String jwtToken = jwtService.generateJwtToken(userDTO);
        
        // Create response
        AuthResponse authResponse = AuthResponse.builder()
                .success(true)
                .message("oauth2_signup_success")
                .jwtToken(jwtToken)
                .refreshToken(null)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour
                .timestamp(OffsetDateTime.now())
                .data(userDTO)
                .build();
        
        sendResponse(response, authResponse);
    }

    private String generateUsername(String email, String name) {
        if (name != null && !name.isEmpty()) {
            // Use name and make it unique by appending part of email
            String baseUsername = name.toLowerCase().replaceAll("[^a-z0-9]", "");
            String emailPart = email.split("@")[0];
            return baseUsername + "_" + emailPart.substring(0, Math.min(4, emailPart.length()));
        }
        // Fallback to email username part
        return email.split("@")[0];
    }

    private void sendResponse(HttpServletResponse response, AuthResponse authResponse) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));
        response.getWriter().flush();
    }

    private void handleFailure(HttpServletResponse response, String message, String description) throws IOException {
        AuthResponse authResponse = AuthResponse.builder()
                .success(false)
                .message(message)
                .jwtToken(null)
                .refreshToken(null)
                .tokenType(null)
                .expiresIn(null)
                .timestamp(OffsetDateTime.now())
                .data(null)
                .build();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));
        response.getWriter().flush();
    }
}

