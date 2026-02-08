package com.viago.tripservice.service;

import com.viago.tripservice.dto.UserDetailsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserIntegrationService {

    // Auth Service එක තිබුනා නම් RestTemplate ඕන වෙනවා
    // private final RestTemplate restTemplate;

    public UserDetailsDto getUserDetails(Long userId) {
        // --- MOCK DATA (Testing සඳහා) ---
        UserDetailsDto user = new UserDetailsDto();
        user.setId(userId);
        user.setName("Kamal Perera");      // දැන් හරි
        user.setPhone("077-1234567");      // දැන් setPhone වැඩ කරනවා
        user.setEmail("driver@example.com");
        user.setVehicleNo("CAB-5678");
        user.setVehicleModel("Toyota Prius");

        return user;
    }

    // පරණ නම තිබ්බ method එකත් තියාගමු (Controller එකේ අවුලක් නොවෙන්න)
    public UserDetailsDto getDriverDetails(Long driverId) {
        return getUserDetails(driverId);
    }
}