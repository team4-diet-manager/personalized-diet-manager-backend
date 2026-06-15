package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import org.springframework.stereotype.Component;

@Component
public class CyclingCalorieStrategy implements ExerciseCalorieStrategy {
    @Override
    public ExerciseType getType() {
        return ExerciseType.CYCLING;
    }

    @Override
    public int calculateBurnedCalories(double weightKg, int minutes, Intensity intensity) {
        // 자전거: 속도 구간별 MET 단계.
        double met = switch (intensity) {
            case LOW -> 4.0;
            case MEDIUM -> 6.8;
            case HIGH -> 10.0;
        };
        return byMet(met, weightKg, minutes);
    }
}
