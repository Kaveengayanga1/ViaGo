package com.viago.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalTrips = 0;

    @Column
    @Builder.Default
    private Double totalDistanceTraveled = 0.0; // in kilometers

    @Column
    @Builder.Default
    private Double totalAmountSpent = 0.0;

    @Column
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalRatingsReceived = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
