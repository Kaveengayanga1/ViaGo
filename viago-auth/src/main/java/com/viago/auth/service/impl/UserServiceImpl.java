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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

        String identifier = loginRequest.getIdentifier();
        log.info("Login attempt for identifier: {}", identifier);

        // Find user by email or username
        UserEntity entity = userRepository.findUserEntityByEmailIgnoreCase(identifier);

        if (entity == null) {
            entity = userRepository.findUserEntityByUsernameIgnoreCase(identifier);
        }

        // If user does not exist
        if (entity == null) {
            log.warn("Login failed: user not found for identifier: {}", identifier);
            return AuthResponse.builder()
                    .success(false)
                    .message("user_not_found")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }

        // Authenticate credentials
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            identifier,
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            log.warn("Login failed for identifier {} - invalid credentials", identifier);
            return AuthResponse.builder()
                    .success(false)
                    .message("invalid_credential")
                    .jwtToken(null)
                    .refreshToken(null)
                    .tokenType(null)
                    .expiresIn(null)
                    .data(null)
                    .build();
        }

        // If authenticated, generate token
        if (authentication.isAuthenticated()) {
            UserDTO userDTO = objectMapper.convertValue(entity, UserDTO.class);
            userDTO.setPassword(null);

            String jwtToken = jwtService.generateJwtToken(userDTO);

            log.info("Login successful for userId={} | email={} | role={}",
                    userDTO.getUserId(), userDTO.getEmail(), userDTO.getRole());
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
                .message("un_successful")
                .jwtToken(null)
                .refreshToken(null)
                .tokenType(null)
                .expiresIn(null)
                .data(null)
                .build();
    }
    //    +------------------+
    //    | DELETE           |
    //    +------------------+
    //Done
    @Override
    public ResponseEntity<UserResponse<Object>> removeUser(Long userId) {
        log.info("Account delete attempt for userId: {}", userId);
        if (!userRepository.existsByUserId(userId)){
            log.info("Account delete failed: user not exists for userId= {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("user_not_exists")
                                    .build()
                    );

        }
        try{
            userRepository.deleteById(userId);
            log.info("Account delete success for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            UserResponse.builder()
                                    .success(true)
                                    .message("user_deleted")
                                    .build()
                    );
        }catch (Exception e){
            log.info("Account delete failed for userId: {} exception occurred", userId);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            UserResponse.builder()
                                    .success(false)
                                    .message("delete_exception")
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
        ArrayList<UserDTO> userDTOList = new ArrayList<>();

        Iterable<UserEntity> userIterable = userRepository.findAll();
        for (UserEntity userEntity : userIterable) {
            userDTOList.add(
                    UserDTO.builder()
                            .username(userEntity.getUsername())
                            .role(userEntity.getRole())
                            .email(userEntity.getEmail())
                            .userId(userEntity.getUserId())
                            .build());
        }
        return ResponseEntity.ok(
                UserResponse.builder()
                        .success(true)
                        .message("user_list")
                        .data(userDTOList)
                        .build()
        );
    }
    //Done
    @Override
    public ResponseEntity<UserResponse<Object>> getUserByEmail(String email) {
        UserEntity entity = userRepository.findUserEntityByEmailIgnoreCase(email);
        UserDTO userDTO = objectMapper.convertValue(entity, UserDTO.class);
        userDTO.setPassword(null);
        return ResponseEntity.ok(
                UserResponse.builder()
                        .success(true)
                        .message("user_list")
                        .data(userDTO)
                        .build()
        );
    }
    @Override
    public ResponseEntity<UserResponse> getUserListByRole(String role) {
        List<UserDTO> userList = userRepository.findByRole(Role.valueOf(role.toUpperCase()))
                .stream()
                .map(entity -> UserDTO.builder()
                        .userId(entity.getUserId())
                        .email(entity.getEmail())
                        .username(entity.getUsername())
                        .role(entity.getRole())
                        .build())
                .toList();
        return ResponseEntity.ok(
                UserResponse.builder()
                        .success(true)
                        .message("user_list")
                        .data(userList)
                        .build()
        );
    }

    @PostConstruct
    public void init() {
        log.info("User service url set to {}:{}", userServiceURL, userServicePort);
    }

}
