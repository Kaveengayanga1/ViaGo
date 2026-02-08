package com.viago.tripservice.client;

import com.viago.tripservice.config.FeignClientConfig;
import com.viago.tripservice.dto.BatchUserRequestDTO;
import com.viago.tripservice.dto.BatchUserResponseDTO;
import com.viago.tripservice.dto.UserResponseDTO;
import com.viago.tripservice.dto.UserRoleResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "viago-user-service", configuration = FeignClientConfig.class)
public interface UserFeignClient {

    @GetMapping("/api/users/{userId}")
    UserResponseDTO getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/api/users/drivers/{driverId}")
    UserResponseDTO getDriverById(@PathVariable("driverId") Long driverId);

    @GetMapping("/api/users/riders/{riderId}")
    UserResponseDTO getRiderById(@PathVariable("riderId") Long riderId);

    @GetMapping("/api/users/{userId}/role")
    UserRoleResponseDTO getUserRole(@PathVariable("userId") Long userId);

    @PostMapping("/api/users/batch")
    BatchUserResponseDTO getUsersBatch(@RequestBody BatchUserRequestDTO request);
}
