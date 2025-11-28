package com.viago.rideEngine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponseDTO {
    private String userId;
    private String userType;
    private Double latitude;
    private Double longitude;
    private Double heading;
    private Double speed;
    private String tripId;
    private LocalDateTime timestamp;
}

