package com.viago.rideEngine.service;

import java.util.Map;

public interface LocationService {
    Object updateDriverLocation(String driverId, Map<String, Object> location);
    Object updatePassengerLocation(String passengerId, Map<String, Object> location);
    Object getDriverLocation(String driverId);
    Object getPassengerLocation(String passengerId);
    Object getTripLocations(String tripId);
    Object getNearbyDrivers(double latitude, double longitude, double radiusKm);
    Object getDriverLocationHistory(String driverId, String startTime, String endTime);
    Object subscribeToLocationUpdates(String tripId, String userId);
}

