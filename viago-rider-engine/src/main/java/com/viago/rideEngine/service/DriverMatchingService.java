package com.viago.rideEngine.service;

public interface DriverMatchingService {
    Object matchDriverForTrip(String tripId);
    Object setDriverAvailability(String driverId, Boolean available);
    Object acceptTrip(String driverId, String tripId);
    Object rejectTrip(String driverId, String tripId, String reason);
    Object getNearbyTripRequests(String driverId, double radiusKm);
}