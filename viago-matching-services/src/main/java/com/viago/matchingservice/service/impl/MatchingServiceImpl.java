package com.viago.matchingservice.service.impl;

import com.viago.matchingservice.dto.MatchingRequest;
import com.viago.matchingservice.service.MatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MatchingServiceImpl implements MatchingService {

    @Override
    public String findDriver(MatchingRequest request) {
        log.info("Finding driver for trip: {} at [{}, {}]", 
                request.getTripId(), request.getPickupLat(), request.getPickupLng());
        
        // Dummy logic: Return a random driver ID
        return "driver-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
