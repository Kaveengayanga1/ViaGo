package com.viago.service.controller;

import com.viago.service.dto.response.UserStatisticsResponseDTO;
import com.viago.service.service.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/statistics")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    @Autowired
    public UserStatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    /**
     * Get user statistics
     */
    @GetMapping
    public ResponseEntity<UserStatisticsResponseDTO> getUserStatistics(@PathVariable Long userId) {
        try {
            UserStatisticsResponseDTO statistics = userStatisticsService.getUserStatistics(userId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update trip count (internal use)
     */
    @PutMapping("/trips")
    public ResponseEntity<UserStatisticsResponseDTO> updateTripCount(@PathVariable Long userId,
            @RequestParam Integer tripCount) {
        try {
            UserStatisticsResponseDTO statistics = userStatisticsService.updateTripCount(userId, tripCount);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update total distance (internal use)
     */
    @PutMapping("/distance")
    public ResponseEntity<UserStatisticsResponseDTO> updateTotalDistance(@PathVariable Long userId,
            @RequestParam Double distance) {
        try {
            UserStatisticsResponseDTO statistics = userStatisticsService.updateTotalDistance(userId, distance);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update total spent (internal use)
     */
    @PutMapping("/spent")
    public ResponseEntity<UserStatisticsResponseDTO> updateTotalSpent(@PathVariable Long userId,
            @RequestParam Double amount) {
        try {
            UserStatisticsResponseDTO statistics = userStatisticsService.updateTotalSpent(userId, amount);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update rating (internal use)
     */
    @PutMapping("/rating")
    public ResponseEntity<UserStatisticsResponseDTO> updateRating(@PathVariable Long userId,
            @RequestParam Double rating) {
        try {
            UserStatisticsResponseDTO statistics = userStatisticsService.updateRating(userId, rating);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
