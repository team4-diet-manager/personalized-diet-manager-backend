package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import org.springframework.stereotype.Component;

@Component
public class WalkingCalorieStrategy implements ExerciseCalorieStrategy {
    @Override
    public ExerciseType getType() {
        return ExerciseType.WALKING;
    }

    @Override
    public int calculateBurnedCalories(double weightKg, int minutes, Intensity intensity) {
        // 걷기: 강도(속도)에 따라 고정 MET 단계를 사용한다.
        double met = switch (intensity) {
            case LOW -> 2.8;
            case MEDIUM -> 3.5;
            case HIGH -> 5.0;
        };
        return byMet(met, weightKg, minutes);
    }
}
