package com.viago.adminservices.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viago.adminservices.config.AuthServiceProperties;
import com.viago.adminservices.dto.UserDTO;
import com.viago.adminservices.dto.response.UserResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AuthServiceProperties properties;

    public UserResponse<List<UserDTO>> getUsers(Integer page, Integer size) {
        return doGet("/user/get-all-users", listType(), pageSizeQuery(page, size));
    }

    public UserResponse<List<UserDTO>> getUsersByRole(String role, Integer page, Integer size) {
        LinkedMultiValueMap<String, String> params = pageSizeQuery(page, size);
        params.add("role", role);
        return doGet("/user/get-user-by-role", listType(), params);
    }

    public UserResponse<UserDTO> getUserByEmail(String email) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);
        return doGet("/user/get-user-by-email", singleType(), params);
    }

    public UserResponse<?> deleteUser(Long userId, String email) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Optional.ofNullable(userId).ifPresent(id -> params.add("userId", id.toString()));
        Optional.ofNullable(email).ifPresent(value -> params.add("email", value));
        return doExchange("/user/delete", HttpMethod.DELETE, null, voidType(), params);
    }

    public UserResponse<UserDTO> updateUser(UserDTO userDTO) {
        return doExchange("/user/update", HttpMethod.PUT, userDTO, singleType(), new LinkedMultiValueMap<>());
    }

    public UserResponse<?> updateUserStatus(Long userId, boolean enabled) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", userId.toString());
        params.add("enabled", Boolean.toString(enabled));
        return doExchange("/user/status", HttpMethod.PATCH, null, voidType(), params);
    }

    private LinkedMultiValueMap<String, String> pageSizeQuery(Integer page, Integer size) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Optional.ofNullable(page).ifPresent(p -> params.add("page", p.toString()));
        Optional.ofNullable(size).ifPresent(s -> params.add("size", s.toString()));
        return params;
    }

    private <T> UserResponse<T> doGet(String path, JavaType type, LinkedMultiValueMap<String, String> params) {
        return doExchange(path, HttpMethod.GET, null, type, params);
    }

    private <T> UserResponse<T> doExchange(String path,
                                           HttpMethod method,
                                           Object body,
                                           JavaType dataType,
                                           LinkedMultiValueMap<String, String> params) {
        String url = UriComponentsBuilder.fromUriString(properties.getBaseUrl())
                .path(path)
                .queryParams(params)
                .toUriString();
        try {
            HttpEntity<?> entity = body != null ? new HttpEntity<>(body) : HttpEntity.EMPTY;
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
            return deserialize(response.getBody(), dataType);
        } catch (Exception ex) {
            log.error("Auth service call failed [{} {}]: {}", method, path, ex.getMessage());
            UserResponse<T> fallback = new UserResponse<>();
            fallback.setSuccess(false);
            fallback.setMessage("auth_service_unavailable");
            return fallback;
        }
    }

    private <T> UserResponse<T> deserialize(String body, JavaType dataType) throws java.io.IOException {
        JavaType responseType = objectMapper.getTypeFactory()
                .constructParametricType(UserResponse.class, dataType);
        return objectMapper.readValue(body, responseType);
    }

    private JavaType singleType() {
        return objectMapper.getTypeFactory().constructType(UserDTO.class);
    }

    private JavaType listType() {
        return objectMapper.getTypeFactory()
                .constructCollectionType(List.class, UserDTO.class);
    }

    private JavaType voidType() {
        return objectMapper.getTypeFactory().constructType(Object.class);
    }
}

