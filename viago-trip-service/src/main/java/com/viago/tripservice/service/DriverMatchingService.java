package com.viago.tripservice.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DriverMatchingService {
    private static final Logger log = LoggerFactory.getLogger(DriverMatchingService.class);
    private final Map<Long, double[]> driverLocations = new ConcurrentHashMap<>();

    public void updateLocation(Long driverId, double lat, double lng) {
        driverLocations.put(driverId, new double[]{lat, lng});
        log.info("📍 Driver location stored: driverId={}, totalOnlineDrivers={}", 
                 driverId, driverLocations.size());
    }
    public void removeDriver(Long driverId) {
        driverLocations.remove(driverId);
        log.info("👋 Driver went offline: driverId={}, totalOnlineDrivers={}", 
                 driverId, driverLocations.size());
    }



    public List<Long> findNearbyDrivers(double pickupLat, double pickupLng) {
        log.info("🔍 Searching for drivers near: lat={}, lng={}, totalDriversOnline={}", 
                 pickupLat, pickupLng, driverLocations.size());
        
        List<Long> nearbyDrivers = driverLocations.entrySet().stream()
                .filter(entry -> {
                    double[] loc = entry.getValue();
                    double distance = calculateDistance(pickupLat, pickupLng, loc[0], loc[1]);
                    log.debug("📏 Evaluating driver {}: distance={} km", entry.getKey(), 
                             String.format("%.2f", distance));
                    return distance <= 5.0;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        log.info("✅ Nearby drivers found: {} drivers within 5km radius", nearbyDrivers.size());
        return nearbyDrivers;
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