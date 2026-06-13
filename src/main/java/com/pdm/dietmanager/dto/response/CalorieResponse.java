package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.enums.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "권장 칼로리 계산 응답")
public class CalorieResponse {
    @Schema(description = "식단 관리 목표", example = "WEIGHT_LOSS")
    private final GoalType goalType;

    @Schema(description = "목표 기준 하루 권장 칼로리", example = "1595")
    private final int recommendedCalories;

    @Schema(description = "현재 계산 요청에서는 0으로 반환되는 일일 섭취 칼로리", example = "0")
    private final int dailyIntakeCalories;

    @Schema(description = "권장 칼로리와 섭취 칼로리의 차이", example = "1595")
    private final int calorieGap;

    public CalorieResponse(
            GoalType goalType,
            int recommendedCalories,
            int dailyIntakeCalories
    ) {
        this.goalType = goalType;
        this.recommendedCalories = recommendedCalories;
        this.dailyIntakeCalories = dailyIntakeCalories;
        this.calorieGap = recommendedCalories - dailyIntakeCalories;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public int getRecommendedCalories() {
        return recommendedCalories;
    }

    public int getDailyIntakeCalories() {
        return dailyIntakeCalories;
    }

    public int getCalorieGap() {
        return calorieGap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private GoalType goalType;
        private int recommendedCalories;
        private int dailyIntakeCalories;

        public Builder goalType(GoalType goalType) {
            this.goalType = goalType;
            return this;
        }

        public Builder recommendedCalories(int recommendedCalories) {
            this.recommendedCalories = recommendedCalories;
            return this;
        }

        public Builder dailyIntakeCalories(int dailyIntakeCalories) {
            this.dailyIntakeCalories = dailyIntakeCalories;
            return this;
        }

        public CalorieResponse build() {
            return new CalorieResponse(goalType, recommendedCalories, dailyIntakeCalories);
        }
    }
}
