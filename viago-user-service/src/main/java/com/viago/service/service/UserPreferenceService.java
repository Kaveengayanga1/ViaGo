package com.viago.service.service;

import com.viago.service.dto.request.UserPreferenceUpdateDTO;
import com.viago.service.dto.response.UserPreferenceResponseDTO;

public interface UserPreferenceService {
    UserPreferenceResponseDTO getUserPreferences(Long userId);

    UserPreferenceResponseDTO updateUserPreferences(Long userId, UserPreferenceUpdateDTO request);

    UserPreferenceResponseDTO createDefaultPreferences(Long userId);
}
