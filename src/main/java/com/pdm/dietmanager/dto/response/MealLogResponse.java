package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.entity.MealLog;
import com.pdm.dietmanager.enums.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "식단 기록 응답")
public class MealLogResponse {
    @Schema(description = "식단 기록 ID", example = "1")
    private final Long mealLogId;

    @Schema(description = "사용자 프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "식단 기록 날짜", example = "2026-06-09")
    private final LocalDate mealDate;

    @Schema(description = "식사 구분", example = "LUNCH")
    private final MealType mealType;

    @Schema(description = "음식 ID", example = "1")
    private final Long foodId;

    @Schema(description = "음식명", example = "닭가슴살")
    private final String foodName;

    @Schema(description = "1회 제공량 기준 칼로리", example = "165")
    private final int calories;

    @Schema(description = "섭취 수량", example = "2")
    private final int quantity;

    @Schema(description = "총 섭취 칼로리", example = "330")
    private final int totalCalories;

    private MealLogResponse(MealLog mealLog) {
        this.mealLogId = mealLog.getMealLogId();
        this.profileId = mealLog.getUserProfile().getProfileId();
        this.mealDate = mealLog.getMealDate();
        this.mealType = mealLog.getMealType();
        this.foodId = mealLog.getFood().getFoodId();
        this.foodName = mealLog.getFood().getName();
        this.calories = mealLog.getFood().getCalories();
        this.quantity = mealLog.getQuantity();
        this.totalCalories = mealLog.getTotalCalories();
    }

    public static MealLogResponse from(MealLog mealLog) {
        return new MealLogResponse(mealLog);
    }
}
