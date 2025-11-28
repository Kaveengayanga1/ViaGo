package com.viago.rideEngine.service;

import java.util.Map;

public interface RoutingService {
    Object getRoute(String origin, String destination);
    Object getRoute(String origin, String destination, String waypoints);
    Object getRouteWithCoordinates(Map<String, Object> routeRequest);
    Object getETA(String origin, String destination, String mode);
    Object getETAWithCoordinates(Map<String, Object> etaRequest);
    Object getTripRoute(String tripId);
    Object getDriverETA(String tripId);
    Object getAlternativeRoutes(Map<String, Object> routeRequest, int maxAlternatives);
    Object getDistance(Map<String, Object> routeRequest);
    Object getDuration(Map<String, Object> routeRequest);
}

