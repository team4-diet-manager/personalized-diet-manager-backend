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

    @Column(name = "protein_grams", nullable = false)
    private int proteinGrams;

    @Column(name = "carb_grams", nullable = false)
    private int carbGrams;

    @Column(name = "fat_grams", nullable = false)
    private int fatGrams;

    @Column(name = "sugar_grams", nullable = false)
    private int sugarGrams;

    @Column(name = "sodium_mg", nullable = false)
    private int sodiumMg;

    @Column(name = "saturated_fat_grams", nullable = false)
    private int saturatedFatGrams;

    @Column(name = "fiber_grams", nullable = false)
    private int fiberGrams;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "food")
    private List<MealLog> mealLogs = new ArrayList<>();

    @Builder
    private Food(String name, int calories, String servingSize,
                 int proteinGrams, int carbGrams, int fatGrams,
                 int sugarGrams, int sodiumMg, int saturatedFatGrams, int fiberGrams) {
        this.name = name;
        this.calories = calories;
        this.servingSize = servingSize;
        this.proteinGrams = proteinGrams;
        this.carbGrams = carbGrams;
        this.fatGrams = fatGrams;
        this.sugarGrams = sugarGrams;
        this.sodiumMg = sodiumMg;
        this.saturatedFatGrams = saturatedFatGrams;
        this.fiberGrams = fiberGrams;
        this.createdAt = LocalDateTime.now();
    }
}