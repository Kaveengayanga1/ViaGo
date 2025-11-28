package com.viago.rideEngine.controller;

import com.viago.rideEngine.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/routing")
public class RoutingController {

    private final RoutingService routingService;

    @Autowired
    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }

    /**
     * Get route between origin and destination
     */
    @GetMapping("/route")
    public ResponseEntity<?> getRoute(@RequestParam String origin,
                                      @RequestParam String destination,
                                      @RequestParam(required = false) String waypoints) {
        try {
            Object route = routingService.getRoute(origin, destination, waypoints);
            return ResponseEntity.ok(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get route with coordinates
     */
    @PostMapping("/route")
    public ResponseEntity<?> getRouteWithCoordinates(@RequestBody Map<String, Object> routeRequest) {
        try {
            Object route = routingService.getRouteWithCoordinates(routeRequest);
            return ResponseEntity.ok(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get ETA (Estimated Time of Arrival)
     */
    @GetMapping("/eta")
    public ResponseEntity<?> getETA(@RequestParam String origin,
                                    @RequestParam String destination,
                                    @RequestParam(required = false) String mode) {
        try {
            Object eta = routingService.getETA(origin, destination, mode);
            return ResponseEntity.ok(eta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get ETA with coordinates
     */
    @PostMapping("/eta")
    public ResponseEntity<?> getETAWithCoordinates(@RequestBody Map<String, Object> etaRequest) {
        try {
            Object eta = routingService.getETAWithCoordinates(etaRequest);
            return ResponseEntity.ok(eta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get route for a trip
     */
    @GetMapping("/trips/{tripId}/route")
    public ResponseEntity<?> getTripRoute(@PathVariable String tripId) {
        try {
            Object route = routingService.getTripRoute(tripId);
            return ResponseEntity.ok(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get ETA for driver to reach passenger
     */
    @GetMapping("/trips/{tripId}/driver-eta")
    public ResponseEntity<?> getDriverETA(@PathVariable String tripId) {
        try {
            Object eta = routingService.getDriverETA(tripId);
            return ResponseEntity.ok(eta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get alternative routes
     */
    @PostMapping("/alternatives")
    public ResponseEntity<?> getAlternativeRoutes(@RequestBody Map<String, Object> routeRequest,
                                                    @RequestParam(required = false, defaultValue = "3") int maxAlternatives) {
        try {
            Object routes = routingService.getAlternativeRoutes(routeRequest, maxAlternatives);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get route distance
     */
    @PostMapping("/distance")
    public ResponseEntity<?> getDistance(@RequestBody Map<String, Object> routeRequest) {
        try {
            Object distance = routingService.getDistance(routeRequest);
            return ResponseEntity.ok(distance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get route duration
     */
    @PostMapping("/duration")
    public ResponseEntity<?> getDuration(@RequestBody Map<String, Object> routeRequest) {
        try {
            Object duration = routingService.getDuration(routeRequest);
            return ResponseEntity.ok(duration);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

