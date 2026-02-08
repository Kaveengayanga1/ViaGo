package com.viago.service.service.impl;

import com.viago.service.dto.request.UserPreferenceUpdateDTO;
import com.viago.service.dto.response.UserPreferenceResponseDTO;
import com.viago.service.entity.UserPreferenceEntity;
import com.viago.service.exception.ResourceNotFoundException;
import com.viago.service.repository.UserPreferenceRepository;
import com.viago.service.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;

    @Override
    public UserPreferenceResponseDTO getUserPreferences(Long userId) {
        UserPreferenceEntity preference = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Preferences not found for user id: " + userId));

        return mapToDTO(preference);
    }

    @Override
    @Transactional
    public UserPreferenceResponseDTO updateUserPreferences(Long userId, UserPreferenceUpdateDTO request) {
        UserPreferenceEntity preference = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Preferences not found for user id: " + userId));

        if (request.getNotificationEnabled() != null) {
            preference.setNotificationEnabled(request.getNotificationEnabled());
        }
        if (request.getEmailNotificationEnabled() != null) {
            preference.setEmailNotificationEnabled(request.getEmailNotificationEnabled());
        }
        if (request.getSmsNotificationEnabled() != null) {
            preference.setSmsNotificationEnabled(request.getSmsNotificationEnabled());
        }
        if (request.getPushNotificationEnabled() != null) {
            preference.setPushNotificationEnabled(request.getPushNotificationEnabled());
        }
        if (request.getTheme() != null) {
            preference.setTheme(request.getTheme());
        }

        UserPreferenceEntity savedPreference = userPreferenceRepository.save(preference);
        log.info("User preferences updated for user: {}", userId);

        return mapToDTO(savedPreference);
    }

    @Override
    @Transactional
    public UserPreferenceResponseDTO createDefaultPreferences(Long userId) {
        if (userPreferenceRepository.findByUserId(userId).isPresent()) {
            log.warn("Preferences already exist for user: {}", userId);
            return getUserPreferences(userId);
        }

        UserPreferenceEntity preference = UserPreferenceEntity.builder()
                .userId(userId)
                .notificationEnabled(true)
                .emailNotificationEnabled(true)
                .smsNotificationEnabled(true)
                .pushNotificationEnabled(true)
                .theme("light")
                .build();

        UserPreferenceEntity savedPreference = userPreferenceRepository.save(preference);
        log.info("Default preferences created for user: {}", userId);

        return mapToDTO(savedPreference);
    }

    private UserPreferenceResponseDTO mapToDTO(UserPreferenceEntity entity) {
        return UserPreferenceResponseDTO.builder()
                .userId(entity.getUserId())
                .notificationEnabled(entity.getNotificationEnabled())
                .emailNotificationEnabled(entity.getEmailNotificationEnabled())
                .smsNotificationEnabled(entity.getSmsNotificationEnabled())
                .pushNotificationEnabled(entity.getPushNotificationEnabled())
                .theme(entity.getTheme())
                .build();
    }
}
