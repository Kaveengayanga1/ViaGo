package com.viago.rideEngine.service;

import java.util.Map;

public interface PricingService {
    Object calculateFareEstimate(Map<String, Object> tripRequest);
    Object calculateFareEstimateWithRoute(Map<String, Object> tripRequest, Object route);
    Object calculateFinalFare(String tripId);
    Object getFareBreakdown(String tripId);
    Object applyDiscount(String tripId, String promoCode);
    Object getSurgePricing(double latitude, double longitude, double radiusKm);
}

