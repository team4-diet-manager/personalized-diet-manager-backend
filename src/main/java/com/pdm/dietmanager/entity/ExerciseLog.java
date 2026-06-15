package com.pdm.dietmanager.entity;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
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
@Table(name = "exercise_log")
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_log_id")
    private Long exerciseLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(name = "exercise_date", nullable = false)
    private LocalDate exerciseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", nullable = false, length = 20)
    private ExerciseType exerciseType;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "intensity", nullable = false, length = 10)
    private Intensity intensity;

    @Column(name = "burned_calories", nullable = false)
    private int burnedCalories;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private ExerciseLog(UserProfile userProfile, LocalDate exerciseDate, ExerciseType exerciseType,
                        int durationMinutes, Intensity intensity, int burnedCalories) {
        this.userProfile = userProfile;
        this.exerciseDate = exerciseDate;
        this.exerciseType = exerciseType;
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
        this.burnedCalories = burnedCalories;
        this.createdAt = LocalDateTime.now();
    }
}
