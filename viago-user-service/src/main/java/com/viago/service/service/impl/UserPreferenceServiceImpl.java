package com.viago.service.service.impl;

import com.viago.service.dto.request.UserPreferenceUpdateDTO;
import com.viago.service.dto.response.UserPreferenceResponseDTO;
import com.viago.service.service.UserPreferenceService;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    @Override
    public UserPreferenceResponseDTO getUserPreferences(Long userId) {
        // TODO: Implement repository logic to fetch preferences
        return UserPreferenceResponseDTO.builder()
                .userId(userId)
                .notificationEnabled(true)
                .emailNotificationEnabled(true)
                .smsNotificationEnabled(true)
                .pushNotificationEnabled(true)
                .theme("light")
                .build();
    }

    @Override
    public UserPreferenceResponseDTO updateUserPreferences(Long userId, UserPreferenceUpdateDTO request) {
        // TODO: Implement repository logic to update preferences
        return UserPreferenceResponseDTO.builder()
                .userId(userId)
                .notificationEnabled(request.getNotificationEnabled())
                .emailNotificationEnabled(request.getEmailNotificationEnabled())
                .smsNotificationEnabled(request.getSmsNotificationEnabled())
                .pushNotificationEnabled(request.getPushNotificationEnabled())
                .theme(request.getTheme())
                .build();
    }

    @Override
    public UserPreferenceResponseDTO createDefaultPreferences(Long userId) {
        // TODO: Implement repository logic to create default preferences
        return UserPreferenceResponseDTO.builder()
                .userId(userId)
                .notificationEnabled(true)
                .emailNotificationEnabled(true)
                .smsNotificationEnabled(true)
                .pushNotificationEnabled(true)
                .theme("light")
                .build();
    }
}
