package com.viago.tripservice.controller;

import com.viago.tripservice.dto.request.TripRequest;
import com.viago.tripservice.dto.response.TripResponse;
import com.viago.tripservice.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequest request,
                                                   @RequestHeader("X-User-Id") String riderId) {
        return ResponseEntity.ok(tripService.createTrip(request, riderId));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> getTripById(@PathVariable String tripId) {
        return ResponseEntity.ok(tripService.getTripById(tripId));
    }
}
