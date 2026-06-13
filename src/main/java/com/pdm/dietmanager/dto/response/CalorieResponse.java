package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.enums.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "권장 칼로리 계산 응답")
public class CalorieResponse {
    @Schema(description = "식단 관리 목표", example = "WEIGHT_LOSS")
    private final GoalType goalType;

    @Schema(description = "목표 기준 하루 권장 칼로리", example = "1595")
    private final int recommendedCalories;

    @Schema(description = "목표별 권장 탄단지 그램")
    private final MacroNutrients macros;

    public CalorieResponse(GoalType goalType, int recommendedCalories, MacroNutrients macros) {
        this.goalType = goalType;
        this.recommendedCalories = recommendedCalories;
        this.macros = macros;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public int getRecommendedCalories() {
        return recommendedCalories;
    }

    public MacroNutrients getMacros() {
        return macros;
    }
}
