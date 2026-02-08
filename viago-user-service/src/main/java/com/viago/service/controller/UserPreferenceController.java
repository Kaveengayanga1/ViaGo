package com.viago.service.controller;

import com.viago.service.dto.request.UserPreferenceUpdateDTO;
import com.viago.service.dto.response.UserPreferenceResponseDTO;
import com.viago.service.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * Get user preferences
     */
    @GetMapping
    public ResponseEntity<UserPreferenceResponseDTO> getUserPreferences(@PathVariable Long userId) {
        try {
            UserPreferenceResponseDTO preferences = userPreferenceService.getUserPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update user preferences
     */
    @PutMapping
    public ResponseEntity<UserPreferenceResponseDTO> updateUserPreferences(@PathVariable Long userId,
            @RequestBody UserPreferenceUpdateDTO request) {
        try {
            UserPreferenceResponseDTO preferences = userPreferenceService.updateUserPreferences(userId, request);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create default preferences for a user
     */
    @PostMapping
    public ResponseEntity<UserPreferenceResponseDTO> createDefaultPreferences(@PathVariable Long userId) {
        try {
            UserPreferenceResponseDTO preferences = userPreferenceService.createDefaultPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
