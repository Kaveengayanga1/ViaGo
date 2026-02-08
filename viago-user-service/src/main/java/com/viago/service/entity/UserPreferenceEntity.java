package com.viago.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean notificationEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean emailNotificationEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean smsNotificationEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean pushNotificationEnabled = true;

    @Column(length = 50)
    private String theme; // e.g., "light", "dark"

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
