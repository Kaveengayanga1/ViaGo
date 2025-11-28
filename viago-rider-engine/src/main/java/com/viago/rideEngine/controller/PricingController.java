package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.PricingService;
import com.viago.rideEngine.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final PricingService pricingService;
    private final RoutingService routingService;

    @Autowired
    public PricingController(PricingService pricingService, RoutingService routingService) {
        this.pricingService = pricingService;
        this.routingService = routingService;
    }

    /**
     * Get fare estimate for a trip
     */
    @PostMapping("/estimate")
    public ResponseEntity<?> getFareEstimate(@RequestBody Map<String, Object> tripRequest) {
        try {
            Object estimate = pricingService.calculateFareEstimate(tripRequest);
            return ResponseEntity.ok(estimate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get fare estimate with route details
     */
    @PostMapping("/estimate-with-route")
    public ResponseEntity<?> getFareEstimateWithRoute(@RequestBody Map<String, Object> tripRequest) {
        try {
            // Get route information
            Object route = routingService.getRoute(
                    tripRequest.get("origin").toString(),
                    tripRequest.get("destination").toString()
            );
            
            // Calculate fare with route details
            Object estimate = pricingService.calculateFareEstimateWithRoute(tripRequest, route);
            return ResponseEntity.ok(estimate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get final fare for a completed trip
     */
    @GetMapping("/trips/{tripId}/final-fare")
    public ResponseEntity<?> getFinalFare(@PathVariable String tripId) {
        try {
            Object fare = pricingService.calculateFinalFare(tripId);
            return ResponseEntity.ok(fare);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get fare breakdown for a trip
     */
    @GetMapping("/trips/{tripId}/breakdown")
    public ResponseEntity<?> getFareBreakdown(@PathVariable String tripId) {
        try {
            Object breakdown = pricingService.getFareBreakdown(tripId);
            return ResponseEntity.ok(breakdown);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Apply promo code or discount
     */
    @PostMapping("/trips/{tripId}/apply-discount")
    public ResponseEntity<?> applyDiscount(@PathVariable String tripId,
                                            @RequestBody Map<String, String> discountRequest) {
        try {
            Object result = pricingService.applyDiscount(tripId, discountRequest.get("promoCode"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get surge pricing information for an area
     */
    @GetMapping("/surge")
    public ResponseEntity<?> getSurgePricing(@RequestParam double latitude,
                                             @RequestParam double longitude,
                                             @RequestParam(required = false, defaultValue = "1.0") double radiusKm) {
        try {
            Object surgeInfo = pricingService.getSurgePricing(latitude, longitude, radiusKm);
            return ResponseEntity.ok(surgeInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

