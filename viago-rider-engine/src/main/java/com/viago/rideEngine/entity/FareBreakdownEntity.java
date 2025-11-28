package com.viago.rideEngine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fare_breakdowns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FareBreakdownEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breakdownId;

    @Column(nullable = false)
    private Long tripId;

    @Column(nullable = false)
    private Double baseFare;

    @Column
    private Double distanceFare;

    @Column
    private Double timeFare;

    @Column
    private Double surgeMultiplier;

    @Column
    private Double surgeAmount;

    @Column
    private Double serviceFee;

    @Column
    private Double tax;

    @Column
    private Double discountAmount;

    @Column
    private String promoCode;

    @Column(nullable = false)
    private Double totalFare;

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

