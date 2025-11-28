package com.viago.rideEngine.service;

import java.util.Map;

public interface TripService {
    Object createTrip(Map<String, Object> tripRequest);
    Object getTripById(String tripId);
    Object getUserTrips(String userId, String status);
    Object getUserTrips(String userId, String status, int page, int size);
    Object updateTripStatus(String tripId, String status);
    Object cancelTrip(String tripId, String reason);
    Object getActiveTrip(String userId);
    Object completeTrip(String tripId);
    Object startTrip(String tripId, String driverId);
    Object getDriverActiveTrip(String driverId);
    Object getDriverTrips(String driverId, String status, int page, int size);
}

