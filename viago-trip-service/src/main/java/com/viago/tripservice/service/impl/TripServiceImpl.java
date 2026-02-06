package com.viago.tripservice.service.impl;

import com.viago.tripservice.dto.request.TripRequest;
import com.viago.tripservice.dto.response.TripResponse;
import com.viago.tripservice.enums.PaymentMethod;
import com.viago.tripservice.enums.TripStatus;
import com.viago.tripservice.model.Trip;
import com.viago.tripservice.repository.TripRepository;
import com.viago.tripservice.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    @Override
    public TripResponse createTrip(TripRequest request, String riderId) {


        BigDecimal estimatedPrice = calculateDummyFare(request.getPickupLat(), request.getPickupLng(),
                request.getDropLat(), request.getDropLng());


        Trip trip = new Trip();
        trip.setRiderId(riderId);
        trip.setPickupLat(request.getPickupLat());
        trip.setPickupLng(request.getPickupLng());
        trip.setDropLat(request.getDropLat());
        trip.setDropLng(request.getDropLng());

        trip.setStatus(TripStatus.REQUESTED);
        trip.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        trip.setEstimatedFare(estimatedPrice);


        Trip savedTrip = tripRepository.save(trip);




        return mapToResponse(savedTrip);
    }

    @Override
    public TripResponse getTripById(String tripId) {
        Trip trip = tripRepository.findById(Long.parseLong(tripId))
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        return mapToResponse(trip);
    }

    // --- Helper Methods ---

    private TripResponse mapToResponse(Trip trip) {
        return TripResponse.builder()
                .tripId(String.valueOf(trip.getId()))
                .status(trip.getStatus())
                .estimatedFare(trip.getEstimatedFare())
                .requestedAt(trip.getRequestedAt())
                .build();
    }

    private BigDecimal calculateDummyFare(double lat1, double lon1, double lat2, double lon2) {

        return BigDecimal.valueOf(250.00);
    }
}