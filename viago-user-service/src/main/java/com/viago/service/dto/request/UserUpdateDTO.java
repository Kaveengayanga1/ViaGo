package com.viago.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureUrl;
    private String preferredLanguage;
    private String timezone;
}
