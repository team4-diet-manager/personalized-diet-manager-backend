package com.pdm.dietmanager.strategy;

import com.pdm.dietmanager.enums.ExerciseType;
import com.pdm.dietmanager.enums.Intensity;
import org.springframework.stereotype.Component;

@Component
public class WeightTrainingCalorieStrategy implements ExerciseCalorieStrategy {
    private static final double FLAT_MET = 5.0;

    @Override
    public ExerciseType getType() {
        return ExerciseType.WEIGHT_TRAINING;
    }

    @Override
    public int calculateBurnedCalories(double weightKg, int minutes, Intensity intensity) {
        // 근력운동: 세트 사이 휴식이 많아 강도 차이가 소모량에 거의 반영되지 않으므로,
        // 다른 종목과 달리 강도와 무관한 단일 MET를 사용한다.
        return byMet(FLAT_MET, weightKg, minutes);
    }
}
