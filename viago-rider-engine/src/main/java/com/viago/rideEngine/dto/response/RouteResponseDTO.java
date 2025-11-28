package com.viago.rideEngine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponseDTO {
    private Double distance; // in kilometers
    private Integer duration; // in minutes
    private String polyline;
    private List<CoordinateDTO> waypoints;
    private String origin;
    private String destination;
}

