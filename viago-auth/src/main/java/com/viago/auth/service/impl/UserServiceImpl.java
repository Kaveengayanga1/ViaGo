package com.viago.auth.service.impl;


import com.viago.auth.dto.Role;
import com.viago.auth.dto.UserDTO;
import com.viago.auth.dto.request.LoginRequest;
import com.viago.auth.dto.response.AuthResponse;
import com.viago.auth.dto.response.UserResponse;
import com.viago.auth.entity.UserEntity;
import com.viago.auth.repository.UserRepository;
import com.viago.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ObjectMapper objectMapper;
    AuthenticationManager authenticationManager;
    JwtServiceImpl jwtService;
    RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceURL;
    @Value("${user-service.port}")
    private String userServicePort;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtServiceImpl jwtService,
                           RestTemplate restTemplate,
                           ObjectMapper objectMapper
                           ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;

        //log.info("User service url set to {}{}", userServiceURL, userServicePort);
    }
    //    +------------------+
    //    | Auth             |
    //    +------------------+
    //Done
    @Override
    public AuthResponse addUser(UserDTO userDTO) {
        log.info("Signup attempt for identifier: {}", userDTO.getEmail());

        if (userRepository.existsByEmailContainingIgnoreCase(userDTO.getEmail())) {
            log.info("Signup failed for email: {} already exists", userDTO.getEmail());
            return AuthResponse.builder()
                    .success(false)
                    .message("email_already_in_use")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .timestamp(OffsetDateTime.now())
                    .data(null)
                    .build();
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userDTO.getRole())
                .build();

//        UserProfileDTO userProfileDTO = UserProfileDTO.builder()
//                .authUserId(userDTO.getUserId())
//                .username(userDTO.getUsername())
//                .email(userDTO.getEmail())
//                .role(userDTO.getRole().name())
//                .build();

        try {
            userRepository.save(userEntity);
            log.info("User created for email: {} | userId: {} | role: {}", userEntity.getEmail(), userEntity.getUserId(), userDTO.getRole());

//            String url = userServiceURL + ":" + userServicePort + "/profile/create";
//            log.info("Use rest template to send profile using url: {}",url);
//            restTemplate.postForObject(url, userProfileDTO, UserProfileDTO.class);
//            log.info("User profile sent to create in User Service for authUserId={}", userEntity.getUserId());

//            return new UserResponse("signup","success");
            return AuthResponse.builder()
                    .success(true)
                    .message("Signup success")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();

        } catch (HttpClientErrorException e) {
            log.error("User Service returned 4xx error: {} - Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            return AuthResponse.builder()
                    .success(false)
                    .message("user_service_client_error")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }
        catch (HttpServerErrorException e) {
            log.error("User Service returned 5xx error: {} - Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            return AuthResponse.builder()
                    .success(false)
                    .message("user_service_server_error")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }
        catch (ResourceAccessException e) {
            log.error("Unable to reach User Service at {}:{} - {}",
                    userServiceURL, userServicePort, e.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("user_service_unreachable")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }
        catch (RestClientException e) {
            log.error("RestTemplate general error during signup: {}", e.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("user_service_error")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }
        catch (Exception e) {
            log.error("Unexpected error during signup: {}", e.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("internal_error")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }
    }
    //Done
    @Override
    public AuthResponse loginUser(LoginRequest loginRequest) {
        // Input validation
        String identifier = loginRequest.getIdentifier();
        String password = loginRequest.getPassword();
        
        if (identifier == null || identifier.trim().isEmpty()) {
            log.warn("Login failed: identifier is null or empty");
            return AuthResponse.builder()
                    .success(false)
                    .message("identifier_required")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .timestamp(OffsetDateTime.now())
                    .data(null)
                    .build();
        }
        
        if (password == null || password.trim().isEmpty()) {
            log.warn("Login failed: password is null or empty for identifier: {}", identifier);
            return AuthResponse.builder()
                    .success(false)
                    .message("password_required")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .timestamp(OffsetDateTime.now())
                    .data(null)
                    .build();
        }
        
        log.info("Login attempt for identifier: {}", identifier);

        // Find user by email or username in single optimized query
        Optional<UserEntity> userOptional = userRepository.findByEmailOrUsernameIgnoreCase(
                identifier.trim()
        );

        // If user does not exist
        if (userOptional.isEmpty()) {
            log.warn("Login failed: user not found for identifier: {}", identifier);
            return AuthResponse.builder()
                    .success(false)
                    .message("user_not_found")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .timestamp(OffsetDateTime.now())
                    .data(null)
                    .build();
        }

        UserEntity entity = userOptional.get();
        
        // CRITICAL FIX: Use entity's email for authentication instead of identifier
        // This fixes the bug where username login fails because CustomUserDetailService
        // only searches by email, not username
        String userEmail = entity.getEmail();

        // Authenticate credentials using the user's email
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userEmail,  // Use email instead of identifier
                            password
                    )
            );
        } catch (Exception ex) {
            log.warn("Login failed for identifier {} (email: {}) - invalid credentials: {}", 
                    identifier, userEmail, ex.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("invalid_credential")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .timestamp(OffsetDateTime.now())
                    .data(null)
                    .build();
        }

        // If authenticated, generate token
        if (authentication.isAuthenticated()) {
            UserDTO userDTO = objectMapper.convertValue(entity, UserDTO.class);
            userDTO.setPassword(null);

            String jwtToken = jwtService.generateJwtToken(userDTO);

            log.info("Login successful for userId={} | email={} | role={} | identifier={}",
                    userDTO.getUserId(), userDTO.getEmail(), userDTO.getRole(), identifier);
            return AuthResponse.builder()
                    .success(true)
                    .message("login_success")
                    .jwtToken(jwtToken)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .timestamp(OffsetDateTime.now())
                    .data(userDTO)
                    .build();
        }

        // safe fallback
        log.warn("Login unsuccessful for identifier {}", identifier);
        return AuthResponse.builder()
                .success(false)
                .message("authentication_failed")
                .jwtToken(null)
                .refreshToken(null)
                .tokenType(null)
                .expiresIn(null)
                .timestamp(OffsetDateTime.now())
                .data(null)
                .build();
    }
    //    +------------------+
    //    | DELETE           |
    //    +------------------+
    //Done
    @Override
    @Transactional
    public ResponseEntity<UserResponse<Object>> removeUser(Long userId) {
        // Input validation
        if (userId == null) {
            log.warn("Account delete failed: userId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("user_id_required")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        log.info("Account delete attempt for userId: {}", userId);
        
        if (!userRepository.existsByUserId(userId)){
            log.warn("Account delete failed: user does not exist for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("user_not_found")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        try{
            userRepository.deleteById(userId);
            log.info("Account delete success for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            UserResponse.builder()
                                    .success(true)
                                    .message("user_deleted")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }catch (Exception e){
            log.error("Account delete failed for userId: {} - exception: {}", userId, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("delete_exception")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
    }
    
    @Override
    @Transactional
    public ResponseEntity<UserResponse<Object>> removeUserByEmail(String email) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            log.warn("Account delete failed: email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("email_required")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        log.info("Account delete attempt for email: {}", email);
        
        if (!userRepository.existsByEmailIgnoreCase(email.trim())){
            log.warn("Account delete failed: user does not exist for email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("user_not_found")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        try{
            userRepository.deleteByEmailIgnoreCase(email.trim());
            log.info("Account delete success for email: {}", email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            UserResponse.builder()
                                    .success(true)
                                    .message("user_deleted")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }catch (Exception e){
            log.error("Account delete failed for email: {} - exception: {}", email, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("delete_exception")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
    }
    //Done
    @Override
    public ResponseEntity<UserResponse> updateUser(UserDTO userDTO) {
        Optional<UserEntity> existingUserEntity = userRepository.findByEmailIgnoreCase(userDTO.getEmail());
        if (existingUserEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("user_not_exists")
                                    .build()
                    );
        }
        UserEntity userEntity = objectMapper.convertValue(existingUserEntity.get(), UserEntity.class);
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        try {
            userRepository.save(userEntity);
            return ResponseEntity.ok(
                    UserResponse.builder()
                            .success(true)
                            .message("user_updated")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("exception_occurred")
                                    .build()
                    );
        }
    }

    //    +------------------+
    //    | GET              |
    //    +------------------+
    //Done
    @Override
    public ResponseEntity<UserResponse> getAllUsers() {
        // Default implementation without pagination for backward compatibility
        return getAllUsers(null, null);
    }
    
    @Override
    public ResponseEntity<UserResponse> getAllUsers(Integer page, Integer size) {
        log.info("Get all users request - page: {}, size: {}", page, size);
        
        List<UserDTO> userDTOList;
        
        // Use pagination if page and size are provided
        if (page != null && size != null && page >= 0 && size > 0) {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserEntity> userPage = userRepository.findAll(pageable);
            
            userDTOList = userPage.getContent()
                    .stream()
                    .map(userEntity -> UserDTO.builder()
                            .userId(userEntity.getUserId())
                            .username(userEntity.getUsername())
                            .email(userEntity.getEmail())
                            .role(userEntity.getRole())
                            .build())
                    .toList();
            
            log.info("Retrieved {} users (page {} of {}, total: {})", 
                    userDTOList.size(), page, userPage.getTotalPages(), userPage.getTotalElements());
        } else {
            // Fallback to non-paginated for backward compatibility
            Iterable<UserEntity> userIterable = userRepository.findAll();
            userDTOList = new ArrayList<>();
            for (UserEntity userEntity : userIterable) {
                userDTOList.add(
                        UserDTO.builder()
                                .userId(userEntity.getUserId())
                                .username(userEntity.getUsername())
                                .email(userEntity.getEmail())
                                .role(userEntity.getRole())
                                .build());
            }
            log.info("Retrieved {} users (no pagination)", userDTOList.size());
        }
        
        return ResponseEntity.ok(
                UserResponse.builder()
                        .success(true)
                        .message("user_list")
                        .data(userDTOList)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }
    //Done
    @Override
    public ResponseEntity<UserResponse<Object>> getUserByEmail(String email) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            log.warn("Get user by email failed: email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("email_required")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        log.info("Get user by email request: {}", email);
        
        Optional<UserEntity> entityOptional = userRepository.findByEmailIgnoreCase(email.trim());
        
        if (entityOptional.isEmpty()) {
            log.warn("User not found for email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("user_not_found")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        UserEntity entity = entityOptional.get();
        UserDTO userDTO = objectMapper.convertValue(entity, UserDTO.class);
        userDTO.setPassword(null);
        
        log.info("User found for email: {} | userId: {}", email, userDTO.getUserId());
        return ResponseEntity.ok(
                UserResponse.builder()
                        .success(true)
                        .message("user_found")
                        .data(userDTO)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }
    @Override
    public ResponseEntity<UserResponse> getUserListByRole(String role) {
        // Default implementation without pagination for backward compatibility
        return getUserListByRole(role, null, null);
    }
    
    @Override
    public ResponseEntity<UserResponse> getUserListByRole(String role, Integer page, Integer size) {
        // Input validation
        if (role == null || role.trim().isEmpty()) {
            log.warn("Get user list by role failed: role is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("role_required")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        log.info("Get user list by role request: {} (page: {}, size: {})", role, page, size);
        
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role provided: {}", role);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("invalid_role")
                                    .timestamp(OffsetDateTime.now())
                                    .build()
                    );
        }
        
        List<UserDTO> userList;
        
        // Use pagination if page and size are provided
        if (page != null && size != null && page >= 0 && size > 0) {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserEntity> userPage = userRepository.findByRole(roleEnum, pageable);
            
            userList = userPage.getContent()
                    .stream()
                    .map(entity -> UserDTO.builder()
                            .userId(entity.getUserId())
                            .email(entity.getEmail())
                            .username(entity.getUsername())
                            .role(entity.getRole())
                            .build())
                    .toList();
            
            log.info("Retrieved {} users with role {} (page {} of {}, total: {})", 
                    userList.size(), role, page, userPage.getTotalPages(), userPage.getTotalElements());
        } else {
            // Fallback to non-paginated for backward compatibility
            List<UserEntity> entities = userRepository.findByRole(roleEnum);
            userList = entities.stream()
                    .map(entity -> UserDTO.builder()
                            .userId(entity.getUserId())
                            .email(entity.getEmail())
                            .username(entity.getUsername())
                            .role(entity.getRole())
                            .build())
                    .toList();
            
            log.info("Retrieved {} users with role {} (no pagination)", userList.size(), role);
        }
        
        return ResponseEntity.ok(
                UserResponse.builder()
                        .success(true)
                        .message("user_list")
                        .data(userList)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    @PostConstruct
    public void init() {
        log.info("User service url set to {}:{}", userServiceURL, userServicePort);
    }

}
