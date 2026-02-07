package com.viago.tripservice.controller;

import com.viago.tripservice.dto.request.TripRequest;
import com.viago.tripservice.dto.response.TripResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;


    @PostMapping("/request")
    public ResponseEntity<TripResponse> requestTrip(
            @RequestBody TripRequest request,
            @RequestHeader("loggedInUser") String riderId
    ) {
        TripResponse response = tripService.createTrip(request, riderId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable String tripId) {
        TripResponse response = tripService.getTripById(tripId);
        return ResponseEntity.ok(response);
    }
}
