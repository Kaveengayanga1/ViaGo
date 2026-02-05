package com.viago.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String preferredLanguage;
    private String timezone;
}
