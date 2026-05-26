package com.pdm.dietmanager.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "calories", nullable = false)
    private int calories;

    @Column(name = "serving_size", nullable = false, length = 50)
    private String servingSize;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "food")
    private List<MealLog> mealLogs = new ArrayList<>();

    @Builder
    private Food(String name, int calories, String servingSize) {
        this.name = name;
        this.calories = calories;
        this.servingSize = servingSize;
        this.createdAt = LocalDateTime.now();
    }
}