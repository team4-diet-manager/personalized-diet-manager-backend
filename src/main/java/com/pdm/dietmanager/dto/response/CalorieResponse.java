package com.pdm.dietmanager.dto.response;

import com.pdm.dietmanager.enums.GoalType;

public class CalorieResponse {
    private final GoalType goalType;
    private final int recommendedCalories;
    private final int dailyIntakeCalories;
    private final int calorieGap;

    private CalorieResponse(Builder builder) {
        this.goalType = builder.goalType;
        this.recommendedCalories = builder.recommendedCalories;
        this.dailyIntakeCalories = builder.dailyIntakeCalories;
        this.calorieGap = builder.recommendedCalories - builder.dailyIntakeCalories;
    }

    public static Builder builder() {
        return new Builder();
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
            return new CalorieResponse(this);
        }
    }
}
