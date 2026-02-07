package com.viago.matchingservice.controller;

import com.viago.matchingservice.dto.MatchingRequest;
import com.viago.matchingservice.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("/find-driver")
    public ResponseEntity<String> findDriver(@RequestBody MatchingRequest request) {
        String driverId = matchingService.findDriver(request);
        return ResponseEntity.ok(driverId);
    }
}
