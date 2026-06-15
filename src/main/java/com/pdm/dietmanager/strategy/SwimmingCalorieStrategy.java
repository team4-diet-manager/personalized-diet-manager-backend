package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import org.springframework.stereotype.Component;

@Component
public class SwimmingCalorieStrategy implements ExerciseCalorieStrategy {
    @Override
    public ExerciseType getType() {
        return ExerciseType.SWIMMING;
    }

    @Override
    public int calculateBurnedCalories(double weightKg, int minutes, Intensity intensity) {
        // 수영: 영법·강도별 MET 단계.
        double met = switch (intensity) {
            case LOW -> 5.0;
            case MEDIUM -> 7.0;
            case HIGH -> 9.8;
        };
        return byMet(met, weightKg, minutes);
    }
}
