package com.viago.tripservice.service;

import com.viago.tripservice.client.UserFeignClient;
import com.viago.tripservice.dto.UserDetailsDto;
import com.viago.tripservice.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserIntegrationService {

    private final UserFeignClient userFeignClient;

    public UserDetailsDto getUserDetails(Long userId) {
        try {
            UserResponseDTO userResponse = userFeignClient.getUserById(userId);
            return mapToUserDetailsDto(userResponse);
        } catch (Exception e) {
            log.error("Error fetching user details for user id {}: {}", userId, e.getMessage());
            // Fallback or return null handled by caller
            return null;
        }
    }

    // Keep old method name for compatibility if needed, or refactor caller
    public UserDetailsDto getDriverDetails(Long driverId) {
        return getUserDetails(driverId);
    }

    private UserDetailsDto mapToUserDetailsDto(UserResponseDTO dto) {
        if (dto == null)
            return null;

        UserDetailsDto user = new UserDetailsDto();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setVehicleNo(dto.getVehicleNo());
        user.setVehicleModel(dto.getVehicleModel());
        return user;
    }
}