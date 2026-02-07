package com.viago.tripservice.service;

import com.viago.tripservice.dto.UserDetailsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserIntegrationService {
    private final RestTemplate restTemplate;

    public UserIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDetailsDto getDriverDetails(Long driverId) {
        String url = "http://localhost:8081/api/v1/users/" + driverId + "/profile";
        return restTemplate.getForObject(url, UserDetailsDto.class);
    }

}
