package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.Food;
import com.pdm.dietmanager.enums.FoodGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
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

    @Schema(description = "1회 제공량 기준 당류(g)", example = "0")
    private final int sugarGrams;

    @Schema(description = "1회 제공량 기준 나트륨(mg)", example = "60")
    private final int sodiumMg;

    @Schema(description = "1회 제공량 기준 포화지방(g)", example = "1")
    private final int saturatedFatGrams;

    @Schema(description = "1회 제공량 기준 식이섬유(g)", example = "0")
    private final int fiberGrams;

    @Schema(description = "목표 기준 음식 적합도 신호등 등급(목표 미지정 시 null)", example = "GREEN")
    private final FoodGrade grade;

    private FoodResponse(Food food, FoodGrade grade) {
        this.foodId = food.getFoodId();
        this.name = food.getName();
        this.calories = food.getCalories();
        this.servingSize = food.getServingSize();
        this.proteinGrams = food.getProteinGrams();
        this.carbGrams = food.getCarbGrams();
        this.fatGrams = food.getFatGrams();
        this.sugarGrams = food.getSugarGrams();
        this.sodiumMg = food.getSodiumMg();
        this.saturatedFatGrams = food.getSaturatedFatGrams();
        this.fiberGrams = food.getFiberGrams();
        this.grade = grade;
    }

    public static FoodResponse from(Food food) {
        return new FoodResponse(food, null);
    }

    public static FoodResponse of(Food food, FoodGrade grade) {
        return new FoodResponse(food, grade);
    }
}
