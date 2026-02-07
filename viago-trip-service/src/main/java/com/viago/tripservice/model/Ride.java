package com.viago.tripservice.model;

import com.viago.tripservice.enums.RideStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rides")
@Getter // ⚠️ @Data වෙනුවට මෙය භාවිතා කරන්න
@Setter
@ToString
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long rideId;

    private Long riderId;
    private Long driverId;

    private double pickupLat;
    private double pickupLng;
    private String pickupAddress;
    private String dropAddress;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private double price;

    @CreationTimestamp
    private LocalDateTime createdTime;

    // Default Constructor
    public Ride() {}

    // ⚠️ Hibernate සඳහා නිවැරදි equals/hashCode (ID එක මත පමණක්)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return rideId != null && Objects.equals(rideId, ride.rideId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
