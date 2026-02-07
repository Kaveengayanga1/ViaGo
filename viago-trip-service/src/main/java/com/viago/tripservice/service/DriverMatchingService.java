package com.viago.tripservice.service;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DriverMatchingService {
    private final Map<Long, double[]> driverLocations = new ConcurrentHashMap<>();

    public void updateLocation(Long driverId, double lat, double lng) {
        driverLocations.put(driverId, new double[]{lat, lng});
    }

    public List<Long> findNearbyDrivers(double pickupLat, double pickupLng) {
        return driverLocations.entrySet().stream()
                .filter(entry -> {
                    double[] loc = entry.getValue();
                    return calculateDistance(pickupLat, pickupLng, loc[0], loc[1]) <= 5.0;
                })
                .map(Map.Entry::getKey) // Driver IDs ටික ගන්නවා
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // ... (Formula implementation) ...
        return distance;
    }
}