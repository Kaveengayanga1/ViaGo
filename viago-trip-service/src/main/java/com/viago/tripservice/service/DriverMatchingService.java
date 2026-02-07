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
    public void removeDriver(Long driverId) {
        driverLocations.remove(driverId);
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
        int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    public int calculateETA(double driverLat, double driverLng, double pickupLat, double pickupLng) {
        double dist = calculateDistance(driverLat, driverLng, pickupLat, pickupLng);
        return (int) Math.ceil((dist / 40.0) * 60) + 2; // Assuming 40km/h
    }
    public double[] getDriverLocation(Long driverId) {
        return driverLocations.get(driverId);
    }
}