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
        try {
            // Real Call: return restTemplate.getForObject(AUTH_SERVICE_URL + "/" + driverId, UserDetailsDto.class);

            // --- DUMMY DATA FOR TESTING (Auth Service නැති විට) ---
            return new UserDetailsDto(
                    driverId, "Sampath Perera", "077-1234567", "Toyota Prius", "CAB-1234"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new UserDetailsDto(driverId, "Unknown Driver", "", "", "");
        }
    }
}
