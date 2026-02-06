package com.viago.tripservice.service;

import com.viago.tripservice.dto.request.TripRequest;
import com.viago.tripservice.dto.response.TripResponse;


public interface TripService {
    TripResponse createTrip(TripRequest request, String riderId);
    TripResponse getTripById(String tripId);

}
