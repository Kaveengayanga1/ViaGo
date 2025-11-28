package com.viago.rideEngine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationUpdateDTO {
    private Double latitude;
    private Double longitude;
    private Double heading;
    private Double speed;
    private String tripId;
}

