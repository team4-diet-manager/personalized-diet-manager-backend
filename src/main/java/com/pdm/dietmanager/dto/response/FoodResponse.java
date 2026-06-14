package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "음식 정보 응답")
public class FoodResponse {
    @Schema(description = "음식 ID", example = "1")
    private final Long foodId;

    @Schema(description = "음식명", example = "닭가슴살")
    private final String name;

    @Schema(description = "1회 제공량 기준 칼로리", example = "165")
    private final int calories;

    @Schema(description = "1회 제공량", example = "100g")
    private final String servingSize;

    @Schema(description = "1회 제공량 기준 단백질(g)", example = "31")
    private final int proteinGrams;

    @Schema(description = "1회 제공량 기준 탄수화물(g)", example = "0")
    private final int carbGrams;

    @Schema(description = "1회 제공량 기준 지방(g)", example = "4")
    private final int fatGrams;

    private FoodResponse(Food food) {
        this.foodId = food.getFoodId();
        this.name = food.getName();
        this.calories = food.getCalories();
        this.servingSize = food.getServingSize();
        this.proteinGrams = food.getProteinGrams();
        this.carbGrams = food.getCarbGrams();
        this.fatGrams = food.getFatGrams();
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

    public int getProteinGrams() {
        return proteinGrams;
    }

    public int getCarbGrams() {
        return carbGrams;
    }

    public int getFatGrams() {
        return fatGrams;
    }
}
