package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.dto.response.MacroNutrients;
import com.pdm.dietmanager.enums.GoalType;

public interface CalorieStrategy {
    GoalType getGoalType();

    int calculate(CalorieRequest request);

    /**
     * 목표별 탄수화물·단백질·지방 섭취 비율(합이 1.0). 전략마다 다르게 정의한다.
     */
    MacroRatio macroRatio();

    /**
     * 권장 칼로리와 목표별 매크로 비율을 조합해 탄단지 권장 그램을 계산한다.
     */
    default MacroNutrients calculateMacros(int recommendedCalories) {
        MacroRatio ratio = macroRatio();
        return MacroNutrients.of(recommendedCalories, ratio.protein(), ratio.carb(), ratio.fat());
    }

    record MacroRatio(double protein, double carb, double fat) {
    }

    default double calculateBmr(CalorieRequest request) {
        double bmr = (10 * request.getWeight())
                + (6.25 * request.getHeight())
                - (5 * request.getAge());

        return switch (request.getGender()) {
            case MALE -> bmr + 5;
            case FEMALE -> bmr - 161;
        };
    }

    default double calculateTdee(CalorieRequest request) {
        return calculateBmr(request) * request.getActivityLevel().getMultiplier();
    }
}
