package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import org.springframework.stereotype.Component;

@Component
public class RunningCalorieStrategy implements ExerciseCalorieStrategy {
    private static final double BASE_MET = 7.0;

    @Override
    public ExerciseType getType() {
        return ExerciseType.RUNNING;
    }

    @Override
    public int calculateBurnedCalories(double weightKg, int minutes, Intensity intensity) {
        // 달리기: 페이스(강도)에 비례해 기준 MET를 배수로 끌어올린다.
        double factor = switch (intensity) {
            case LOW -> 1.0;
            case MEDIUM -> 1.3;
            case HIGH -> 1.6;
        };
        return byMet(BASE_MET * factor, weightKg, minutes);
    }
}
