package com.viago.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreferenceUpdateDTO {
    private Boolean notificationEnabled;
    private Boolean emailNotificationEnabled;
    private Boolean smsNotificationEnabled;
    private Boolean pushNotificationEnabled;
    private String theme;
}
