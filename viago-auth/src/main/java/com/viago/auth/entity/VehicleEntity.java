package com.viago.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "driver")
@ToString(exclude = "driver")
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vehicleId;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(nullable = false)
    private String model;

    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false, unique = true)
    private UserEntity driver;
}

