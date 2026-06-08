package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.Food;

public class FoodResponse {
    private final Long foodId;
    private final String name;
    private final int calories;
    private final String servingSize;

    private FoodResponse(Food food) {
        this.foodId = food.getFoodId();
        this.name = food.getName();
        this.calories = food.getCalories();
        this.servingSize = food.getServingSize();
    }

    public static FoodResponse from(Food food) {
        return new FoodResponse(food);
    }

    public Long getFoodId() {
        return foodId;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public String getServingSize() {
        return servingSize;
    }
}
