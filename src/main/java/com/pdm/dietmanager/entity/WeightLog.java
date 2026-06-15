package com.pdm.dietmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "weight_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "log_date"})
)
public class WeightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_log_id")
    private Long weightLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private WeightLog(UserProfile userProfile, LocalDate logDate, double weight) {
        this.userProfile = userProfile;
        this.logDate = logDate;
        this.weight = weight;
        this.createdAt = LocalDateTime.now();
    }

    public void changeWeight(double weight) {
        this.weight = weight;
    }
}
