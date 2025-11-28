package com.viago.rideEngine.entity;

import com.viago.rideEngine.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    @Column(nullable = false, unique = true)
    private Long userId; // Reference to auth service user ID

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String licenseNumber;

    @Column
    private String vehicleModel;

    @Column
    private String vehiclePlateNumber;

    @Column
    private String vehicleColor;

    @Column
    private Integer vehicleYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DriverStatus status = DriverStatus.OFFLINE;

    @Column
    private Double currentLatitude;

    @Column
    private Double currentLongitude;

    @Column
    private Boolean isAvailable;

    @Column
    private Double rating;

    @Column
    private Integer totalTrips;

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

