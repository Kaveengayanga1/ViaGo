package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreferenceResponseDTO {
    private Long preferenceId;
    private Long userId;
    private Boolean notificationEnabled;
    private Boolean emailNotificationEnabled;
    private Boolean smsNotificationEnabled;
    private Boolean pushNotificationEnabled;
    private String theme;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
