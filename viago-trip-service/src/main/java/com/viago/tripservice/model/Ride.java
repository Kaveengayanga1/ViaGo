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

public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rideId;

    private Long riderId;
    private Long driverId;

    // Location Data
    private double pickupLat;
    private double pickupLng;
    private String pickupAddress;
    private String dropAddress;

    @Enumerated(EnumType.STRING)
    private RideStatus status; // SEARCHING, ACCEPTED, ARRIVED, IN_PROGRESS, COMPLETED

    private double price;
    private LocalDateTime createdTime;
}
