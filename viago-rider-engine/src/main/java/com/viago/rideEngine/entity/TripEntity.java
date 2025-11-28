package com.viago.rideEngine.entity;

import com.viago.rideEngine.enums.TripStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Column(nullable = false)
    private Long passengerId;

    @Column
    private Long driverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TripStatus status = TripStatus.PENDING;

    // Origin location
    @Column(nullable = false)
    private Double originLatitude;

    @Column(nullable = false)
    private Double originLongitude;

    @Column(nullable = false, length = 500)
    private String originAddress;

    // Destination location
    @Column(nullable = false)
    private Double destinationLatitude;

    @Column(nullable = false)
    private Double destinationLongitude;

    @Column(nullable = false, length = 500)
    private String destinationAddress;

    // Trip details
    @Column
    private Double distance; // in kilometers

    @Column
    private Integer estimatedDuration; // in minutes

    @Column
    private Double estimatedFare;

    @Column
    private Double finalFare;

    @Column(length = 1000)
    private String routePolyline;

    // Timestamps
    @Column
    private LocalDateTime scheduledTime;

    @Column
    private LocalDateTime pickupTime;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime completedTime;

    @Column(length = 500)
    private String cancelReason;

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

