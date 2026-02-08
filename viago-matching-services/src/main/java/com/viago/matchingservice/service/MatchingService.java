package com.viago.matchingservice.service;

import com.viago.matchingservice.dto.MatchingRequest;

public interface MatchingService {
    String findDriver(MatchingRequest request);
}
