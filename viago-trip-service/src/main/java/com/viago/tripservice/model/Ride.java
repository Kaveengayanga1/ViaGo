package com.viago.tripservice.model;

import com.viago.tripservice.enums.PaymentMethod;
import com.viago.tripservice.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trips")
@Data

public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String riderId;

    private String driverId;

    // --- Pickup Location ---

    @Column(nullable = false)
    private Double pickupLat;
    @Column(nullable = false)
    private Double pickupLng;

    // --- Drop Location ---
    @Column(nullable = false)
    private Double dropLat;
    @Column(nullable = false)
    private Double dropLng;

    // --- Trip Info ---
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private BigDecimal distanceKm;
    private BigDecimal estimatedFare;

    // --- Timestamps ---
    @CreationTimestamp
    private LocalDateTime requestedAt;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
