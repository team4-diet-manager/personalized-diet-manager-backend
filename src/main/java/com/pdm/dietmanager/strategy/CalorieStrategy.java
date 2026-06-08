package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.dto.request.CalorieRequest;
import com.pdm.dietmanager.enums.GoalType;

public interface CalorieStrategy {
    GoalType getGoalType();

    int calculate(CalorieRequest request);

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
