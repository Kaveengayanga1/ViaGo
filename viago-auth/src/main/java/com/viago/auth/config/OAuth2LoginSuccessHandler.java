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
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public OAuth2LoginSuccessHandler(JwtServiceImpl jwtService, 
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
        String providerId = oAuth2User.getAttribute("sub"); // Google's user ID
        
        if (email == null || email.isEmpty()) {
            log.warn("OAuth2 login failed: email is missing");
            handleFailure(response, "oauth2_email_missing", "Email not found in OAuth2 user");
            return;
        }
        
        // Find user by email and provider
        Optional<UserEntity> userOptional = userRepository.findByEmailAndProvider(email, "google");
        
        if (userOptional.isEmpty()) {
            log.warn("OAuth2 login failed: user not found with email {} and provider google", email);
            handleFailure(response, "oauth2_user_not_found", "User not found. Please sign up first.");
            return;
        }
        
        UserEntity userEntity = userOptional.get();
        
        // Validate user has RIDER role
        if (userEntity.getRole() != Role.RIDER) {
            log.warn("OAuth2 login failed: user {} does not have RIDER role", email);
            handleFailure(response, "oauth2_invalid_role", "Only RIDER role users can login via Google OAuth");
            return;
        }
        
        // Update providerId if needed
        if (userEntity.getProviderId() == null || !userEntity.getProviderId().equals(providerId)) {
            userEntity.setProviderId(providerId);
            userRepository.save(userEntity);
        }
        
        // Convert to DTO and generate JWT
        UserDTO userDTO = objectMapper.convertValue(userEntity, UserDTO.class);
        userDTO.setPassword(null);
        
        String jwtToken = jwtService.generateJwtToken(userDTO);
        
        log.info("OAuth2 login successful for userId={} | email={} | role={}", 
                userDTO.getUserId(), userDTO.getEmail(), userDTO.getRole());
        
        // Create response
        AuthResponse authResponse = AuthResponse.builder()
                .success(true)
                .message("oauth2_login_success")
                .jwtToken(jwtToken)
                .refreshToken(null)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour
                .timestamp(OffsetDateTime.now())
                .data(userDTO)
                .build();
        
        // Return JSON response
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
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));
        response.getWriter().flush();
    }
}

