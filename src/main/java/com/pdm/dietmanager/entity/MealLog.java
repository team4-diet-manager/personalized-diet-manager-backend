package com.pdm.dietmanager.entity;

import com.pdm.dietmanager.enums.MealType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "meal_log")
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_log_id")
    private Long mealLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    private MealType mealType;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_calories", nullable = false)
    private int totalCalories;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private MealLog(UserProfile userProfile, Food food, LocalDate mealDate,
                    MealType mealType, int quantity) {
        this.userProfile = userProfile;
        this.food = food;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.quantity = quantity;
        this.totalCalories = food.getCalories() * quantity;
        this.createdAt = LocalDateTime.now();
    }

    public void update(Food food, int quantity, MealType mealType) {
        this.food = food;
        this.quantity = quantity;
        this.mealType = mealType;
        this.totalCalories = food.getCalories() * quantity;
    }
}
